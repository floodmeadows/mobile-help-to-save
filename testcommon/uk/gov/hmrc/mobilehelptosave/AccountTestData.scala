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

package uk.gov.hmrc.mobilehelptosave

import org.joda.time.{LocalDate, YearMonth}
import uk.gov.hmrc.mobilehelptosave.connectors.HelpToSaveAccount
import uk.gov.hmrc.mobilehelptosave.domain.{Account, Blocking, BonusTerm}

trait AccountTestData {

  protected val accountReturnedByHelpToSaveJsonString: String =
    """
      |{
      |  "openedYearMonth": "2018-01",
      |  "accountNumber": "1000000000001",
      |  "isClosed": false,
      |  "blocked": {
      |    "unspecified": false
      |  },
      |  "balance": 123.45,
      |  "paidInThisMonth": 27.88,
      |  "canPayInThisMonth": 22.12,
      |  "maximumPaidInThisMonth": 50,
      |  "thisMonthEndDate": "2018-04-30",
      |  "bonusTerms": [
      |    {
      |      "bonusEstimate": 90.99,
      |      "bonusPaid": 90.99,
      |      "endDate": "2019-12-31",
      |      "bonusPaidOnOrAfterDate": "2020-01-01"
      |    },
      |    {
      |      "bonusEstimate": 12,
      |      "bonusPaid": 0,
      |      "endDate": "2021-12-31",
      |      "bonusPaidOnOrAfterDate": "2022-01-01"
      |    }
      |  ]
      |}
    """.stripMargin

  /** A HelpToSaveAccount object containing the same data as [[accountReturnedByHelpToSaveJsonString]] */
  protected val helpToSaveAccount: HelpToSaveAccount = HelpToSaveAccount(
    accountNumber = "1000000000001",
    openedYearMonth = new YearMonth(2018, 1),
    isClosed = false,
    blocked = Blocking(false),
    balance = BigDecimal("123.45"),
    paidInThisMonth = BigDecimal("27.88"),
    canPayInThisMonth = BigDecimal("22.12"),
    maximumPaidInThisMonth = 50,
    thisMonthEndDate = new LocalDate(2018, 4, 30),
    bonusTerms = Seq(
      BonusTerm(
        bonusEstimate = BigDecimal("90.99"),
        bonusPaid = BigDecimal("90.99"),
        endDate = new LocalDate(2019, 12, 31),
        bonusPaidOnOrAfterDate = new LocalDate(2020, 1, 1)
      ),
      BonusTerm(
        bonusEstimate = 12,
        bonusPaid = 0,
        endDate = new LocalDate(2021, 12, 31),
        bonusPaidOnOrAfterDate = new LocalDate(2022, 1, 1)
      )
    ),
    closureDate = None,
    closingBalance = None
  )

  /** An Account object containing the same data as [[accountReturnedByHelpToSaveJsonString]] */
  protected val account: Account = Account(
    number = "1000000000001",
    openedYearMonth = new YearMonth(2018, 1),
    isClosed = false,
    blocked = Blocking(false),
    balance = BigDecimal("123.45"),
    paidInThisMonth = BigDecimal("27.88"),
    canPayInThisMonth = BigDecimal("22.12"),
    maximumPaidInThisMonth = 50,
    thisMonthEndDate = new LocalDate(2018, 4, 30),
    bonusTerms = Seq(
      BonusTerm(
        bonusEstimate = BigDecimal("90.99"),
        bonusPaid = BigDecimal("90.99"),
        endDate = new LocalDate(2019, 12, 31),
        bonusPaidOnOrAfterDate = new LocalDate(2020, 1, 1)
      ),
      BonusTerm(
        bonusEstimate = 12,
        bonusPaid = 0,
        endDate = new LocalDate(2021, 12, 31),
        bonusPaidOnOrAfterDate = new LocalDate(2022, 1, 1)
      )
    ),
    closureDate = None,
    closingBalance = None
  )

  protected val closedAccountReturnedByHelpToSaveJsonString: String =
    """
      |{
      |  "openedYearMonth": "2018-03",
      |  "accountNumber": "1000000000002",
      |  "isClosed": true,
      |  "blocked": {
      |    "unspecified": false
      |  },
      |  "balance": 0,
      |  "paidInThisMonth": 0,
      |  "canPayInThisMonth": 50,
      |  "maximumPaidInThisMonth": 50,
      |  "thisMonthEndDate": "2018-04-30",
      |  "bonusTerms": [
      |    {
      |      "bonusEstimate": 7.50,
      |      "bonusPaid": 0,
      |      "endDate": "2020-02-29",
      |      "bonusPaidOnOrAfterDate": "2020-03-01"
      |    },
      |    {
      |      "bonusEstimate": 0,
      |      "bonusPaid": 0,
      |      "endDate": "2022-02-28",
      |      "bonusPaidOnOrAfterDate": "2022-03-01"
      |    }
      |  ],
      |  "closureDate": "2018-04-09",
      |  "closingBalance": 10
      |}
    """.stripMargin

  // invalid because required field isClosed is omitted
  protected val accountReturnedByHelpToSaveInvalidJsonString: String =
    """
      |{
      |  "openedYearMonth": "2017-11",
      |  "accountNumber": "1000000000001",
      |  "blocked": {
      |    "unspecified": false
      |  },
      |  "balance": 249.45,
      |  "paidInThisMonth": 27.88,
      |  "canPayInThisMonth": 22.12,
      |  "maximumPaidInThisMonth": 50,
      |  "thisMonthEndDate": "2018-04-30",
      |  "bonusTerms": [
      |    {
      |      "bonusEstimate": 125,
      |      "bonusPaid": 0,
      |      "endDate": "2019-10-31",
      |      "bonusPaidOnOrAfterDate": "2019-11-01"
      |    },
      |    {
      |      "bonusEstimate": 0,
      |      "bonusPaid": 0,
      |      "endDate": "2021-10-31",
      |      "bonusPaidOnOrAfterDate": "2021-11-01"
      |    }
      |  ]
      |}
    """.stripMargin
}
