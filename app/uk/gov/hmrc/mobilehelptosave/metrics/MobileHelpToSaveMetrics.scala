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

package uk.gov.hmrc.mobilehelptosave.metrics

import com.codahale.metrics.Counter
import com.google.inject.{Inject, Singleton}

@Singleton
class MobileHelpToSaveMetrics @Inject() (val metrics: com.kenshoo.play.metrics.Metrics) {

  def counter(name: String): Counter = metrics.defaultRegistry.counter(name)

  val invitationCounter: Counter = counter("invitation")
  val taxCreditsBrokerCallCounter: Counter = counter("taxCreditsBrokerCall")
  val taxCreditsCacheHitCounter: Counter = counter("taxCreditsCacheHit")

}
