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

package uk.gov.hmrc.mobilehelptosave.repos

import org.scalatest.TestSuite
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import uk.gov.hmrc.mobilehelptosave.support.MongoTestCollectionsDropAfterAll
import uk.gov.hmrc.mongo.ReactiveRepository

trait MongoRepositoryISpec[A <: Any, ID <: Any] extends RepositorySpec[A, ID] with MongoTestCollectionsDropAfterAll with GuiceOneAppPerSuite { this: TestSuite =>
  final override implicit lazy val app: Application = appBuilder
    .build()
  override val repo: ReactiveRepository[A, ID] with TestableRepository[A, ID]
}
