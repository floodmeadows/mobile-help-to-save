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

import reactivemongo.api.ReadPreference
import reactivemongo.api.commands.{DefaultWriteResult, WriteConcern, WriteResult}
import reactivemongo.core.errors.GenericDatabaseException

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

trait FakeRepository[A <: Any, ID <: Any] extends TestableRepository[A, ID] {
  protected val store: mutable.Map[ID, A] = mutable.Map[ID, A]()

  protected def idOf(entity: A): ID

  override def findById(id: ID, readPreference: ReadPreference)(implicit ec: ExecutionContext): Future[Option[A]] =
    Future successful store.get(id)

  override def insert(entity: A)(implicit ec: ExecutionContext): Future[WriteResult] = Future {
    if (store.contains(idOf(entity))) {
      throw GenericDatabaseException("already exists", Some(11000))
    } else {
      store.put(idOf(entity), entity)
      DefaultWriteResult(ok = true, 1, Seq.empty, None, None, None)
    }
  }

  override def removeById(id: ID, writeConcern: WriteConcern)(implicit ec: ExecutionContext): Future[WriteResult] = Future successful {
    val removed = store.remove(id).isDefined

    DefaultWriteResult(ok = true, n = if (removed) 1 else 0, Seq.empty, None, None, None)
  }

  override def ensureIndexes(implicit ec: ExecutionContext): Future[Seq[Boolean]] = Future successful Seq.empty
}
