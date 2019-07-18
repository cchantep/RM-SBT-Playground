import scala.util.{ Failure, Success, Try }

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import reactivemongo.api.DefaultDB

class Playground {
  import reactivemongo.bson.BSONDocument
  import reactivemongo.api.{ QueryOpts, MongoDriver, MongoConnection }

  private lazy val driver = new MongoDriver

  private var con = Option.empty[(MongoConnection, String)]
  private var lastDb = Option.empty[DefaultDB]

  def lastDatabase: DefaultDB = lastDb.get

  def connect(uri: String): Unit = driver.synchronized {
    con.foreach(_._1.askClose()(5.seconds))

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

  def databaseLoop(): Try[Unit] =
    databaseLoop(Long.MaxValue, 1000L/* 1s */, 5.seconds)

  def databaseLoop(count: Long): Try[Unit] =
    databaseLoop(count, 1000L/* 1s */, 5.seconds)

  def databaseLoop(count: Long, pause: Long): Try[Unit] =
    databaseLoop(count, pause, 5.seconds)

  def databaseLoop(count: Long, pause: Long, timeout: Duration): Try[Unit] =
    databaseLoop(count, pause, timeout, () => Success({}))

  @annotation.tailrec
  final def databaseLoop(count: Long, pause: Long, timeout: Duration, f: () => Try[Unit]): Try[Unit] = if (count == 0) Success({}) else {
    database(timeout) match {
      case e @ Failure(_) => e

      case Success(_) => f() match {
        case e @ Failure(_) => e
        case _ => {
          Thread.sleep(pause)

          databaseLoop(count - 1, pause, timeout, f)
        }
      }
    }
  }

  def dummyFind(): Try[Unit] = dummyFind(5.seconds)

  def dummyFind(timeout: Duration): Try[Unit] = lastDb match {
    case Some(db) => Try(Await.result(db.collection("bar").
        find(BSONDocument.empty).one[BSONDocument].map(_.isDefined), timeout))

    case _ => Failure(new RuntimeException(
      "Database is not available; First call `database`."))
  }

  def dummyFindLoop(): Try[Unit] =
    dummyFindLoop(Long.MaxValue, 1000L /* 1s */, 5.seconds)

  def dummyFindLoop(count: Long): Try[Unit] =
    dummyFindLoop(count, 1000L /* 1s */, 5.seconds)

  def dummyFindLoop(count: Long, pause: Long): Try[Unit] =
    dummyFindLoop(count, pause, 5.seconds)

  def dummyFindLoop(count: Long, pause: Long, timeout: Duration): Try[Unit] =
    if (count == 0) Success({}) else dummyFind(timeout) match {
      case e @ Failure(_) => e

      case Success(_) => dummyFindLoop(count - 1, pause, timeout)
    }

  def tailableFind(): Unit = lastDb match {
    case Some(db) => db.collection("bar").find(BSONDocument.empty).
        options(QueryOpts().tailable.awaitData).
        cursor[BSONDocument]().fold({}) { (_, doc) =>
          println(s"doc = ${BSONDocument pretty doc}")
        }

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
