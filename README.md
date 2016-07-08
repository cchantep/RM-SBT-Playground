# ReactiveMongo SBT Playground

Can work with any database, even if not initialized/empty.

    sbt console
    scala> import Playground._
    scala> rm connect "mongodb://host:27017,host:27018,host:27019/foo"
    scala> rm exit

**DB resolution:**

    scala> rm database

Must display a result of the following form.

    resN: scala.util.Try[Unit] = Success(())

This can be executed multiple times, to check the DB resolution, according the ReplicaSet events.

    scala> rm database
    res1: scala.util.Try[Unit] = Success(())
    scala> rm database
    res1: scala.util.Try[Unit] = Success(())

**Dummy collection operation:**

    scala> rm dummyFind

Must display a result of the following form.

    resN: scala.util.Try[Unit] = Success(())

This can be executed many times, in order to repeat the dummy collection operation.

    scala> rm database
    res1: scala.util.Try[Unit] = Success(())
    scala> rm database
    res1: scala.util.Try[Unit] = Success(())

**Exit:**

    scala> rm exit