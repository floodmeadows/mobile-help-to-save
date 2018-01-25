/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.mobilehelptosave.services

import javax.inject.{Inject, Named, Singleton}

import cats.data.OptionT
import cats.instances.future._
import org.joda.time.DateTimeZone
import reactivemongo.core.errors.DatabaseException
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.mobilehelptosave.connectors.HelpToSaveConnector
import uk.gov.hmrc.mobilehelptosave.domain.{InternalAuthId, Invitation, UserDetails, UserState}
import uk.gov.hmrc.mobilehelptosave.repos.InvitationRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserService @Inject() (
  helpToSaveConnector: HelpToSaveConnector,
  surveyService: SurveyService,
  invitationRepository: InvitationRepository,
  clock: Clock,
  @Named("helpToSave.enabled") enabled: Boolean,
  @Named("helpToSave.dailyInvitationCap") dailyInvitationCap: Int
) {

  def userDetails(internalAuthId: InternalAuthId)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[UserDetails]] = whenEnabled {
    val enrolledFO = helpToSaveConnector.enrolmentStatus()
    val wantsToBeContactedFO = surveyService.userWantsToBeContacted()
    (for {
      enrolled <- OptionT(enrolledFO)
      wantsToBeContacted <- OptionT(wantsToBeContactedFO)
      state <- OptionT.liftF(determineState(internalAuthId, enrolled, wantsToBeContacted))
    } yield {
      UserDetails(state = state)
    }).value
  }

  private def whenEnabled[T](body: => Future[Option[T]]) =
    if (enabled) {
      body
    } else {
      Future successful None
    }

  private def determineState(internalAuthId: InternalAuthId, enrolled: Boolean, wantsToBeContacted: Boolean)(implicit ec: ExecutionContext): Future[UserState.Value] =
    if (enrolled) {
      Future successful UserState.Enrolled
    } else {
      if (wantsToBeContacted) {
        determineInvitedState(internalAuthId)
      }
      else {
        Future successful UserState.NotEnrolled
      }
    }

  private def determineInvitedState(internalAuthId: InternalAuthId)(implicit ec: ExecutionContext): Future[UserState.Value] =
    invitationRepository.findById(internalAuthId).flatMap { invitationO =>
      if (invitationO.isDefined) {
        Future.successful(UserState.Invited)
      } else {
        invitationRepository.countCreatedSince(startOfTodayUtc()).flatMap { alreadyCreatedTodayCount =>
          if (alreadyCreatedTodayCount >= dailyInvitationCap) {
            Future.successful(UserState.NotEnrolled)
          } else {
            invitationRepository.insert(Invitation(internalAuthId, clock.now()))
              .map(_ => UserState.InvitedFirstTime)
              .recover {
                case e: DatabaseException if invitationRepository.isDuplicateKey(e) =>
                  UserState.Invited
              }
          }
        }
      }
    }

  private def startOfTodayUtc() = clock.now().withZone(DateTimeZone.UTC).withTimeAtStartOfDay()
}