import scala.util.{ Failure, Try }

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import reactivemongo.api.DefaultDB

class Playground {
  import reactivemongo.bson.BSONDocument
  import reactivemongo.api.{ MongoDriver, MongoConnection }

  private lazy val driver = new MongoDriver

  private var con = Option.empty[(MongoConnection, String)]
  private var lastDb = Option.empty[DefaultDB]

  def connect(uri: String): Unit = driver.synchronized {
    con.foreach(_._1.close())

    con = Some(
      MongoConnection.parseURI(uri).filter(_.db.isDefined).map { dbUri =>
        driver.connection(dbUri) -> dbUri.db.get
      }.get)
  }

  def database(): Try[Unit] = database(5.seconds)

  def database(timeout: Duration): Try[Unit] = con match {
    case Some((con, db)) => Try {
      val database = Await.result(con.database(db), timeout)
      lastDb = Some(database)
    }

    case _ => Failure(new RuntimeException(
      "Connection is not available; First call `connect`."))
  }

  def dummyFind(): Try[Unit] = dummyFind(5.seconds)

  def dummyFind(timeout: Duration): Try[Unit] = lastDb match {
    case Some(db) => Try(Await.result(db.collection("bar").
        find(BSONDocument.empty).one[BSONDocument].map(_.isDefined), timeout))

    case _ => Failure(new RuntimeException(
      "Database is not available; First call `database`."))
  }

  def close(): Unit = driver.close()
}

object Playground {
  val rm = new Playground {
    def exit() = {
      close()
      sys.exit(0)
    }
  }
}
