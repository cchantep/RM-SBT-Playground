# ReactiveMongo SBT Playground

Can work with any database, even if not initialized/empty.

## Usage

```scala
./run.sh
scala> import Playground._
scala> rm connect "mongodb://host:27017,host:27018,host:27019/foo"
scala> rm exit
```

> The command `sbt console` + `import Playground._` can also be used.

**DB resolution:**

```scala
scala> rm database
```

Must display a result of the following form.

    resN: scala.util.Try[Unit] = Success(())

This can be executed multiple times, to check the DB resolution, according the ReplicaSet events.

```scala
scala> rm database
res1: scala.util.Try[Unit] = Success(())
scala> rm database
res1: scala.util.Try[Unit] = Success(())
```

**Dummy collection operation:**

```scala
scala> rm dummyFind
```

Must display a result of the following form.

    resN: scala.util.Try[Unit] = Success(())

This can be executed many times, in order to repeat the dummy collection operation.

```scala
scala> rm database
res1: scala.util.Try[Unit] = Success(())
scala> rm database
res1: scala.util.Try[Unit] = Success(())
```

**Log:**

All the ReactiveMongo traces are logged to `logs/reactivemongo.log`.

**Exit:**

```scala
scala> rm exit
```

## Select version of ReactiveMongo

    RM_VERSION="..." sbt console

## Docker

A [Docker image](https://hub.docker.com/r/cchantep/rm-sbt-playground) is also provided:

```
# With default ReactiveMongo version
docker run --rm -it cchantep/rm-sbt-playground:0.1.1

# With specific ReactiveMongo version
docker run -e RM_VERSION=... --rm -it cchantep/rm-sbt-playground:0.1.1
```