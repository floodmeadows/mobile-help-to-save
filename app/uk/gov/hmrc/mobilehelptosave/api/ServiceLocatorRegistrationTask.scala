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

package uk.gov.hmrc.mobilehelptosave.api

import java.util.concurrent.TimeUnit.SECONDS

import akka.actor.ActorSystem
import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.api.connector.ServiceLocatorConnector
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ServiceLocatorRegistrationTask @Inject()(actorSystem: ActorSystem, connector: ServiceLocatorConnector)(implicit executionContext: ExecutionContext) {
  actorSystem.scheduler.scheduleOnce(delay = FiniteDuration(10, SECONDS)) {
    register
  }

  def register: Future[Boolean] = {
    implicit val hc: HeaderCarrier = HeaderCarrier()
    connector.register
  }
}
