FROM cchantep/sbt:1.4.7-jdk8u265-b01-alpine-slim-akka

RUN apk update && \
apk add --no-cache tzdata curl bash git && \
git clone https://github.com/cchantep/RM-SBT-Playground.git && \
cd RM-SBT-Playground && \
RM_VERSION=1.0.0 sbt compile

WORKDIR /RM-SBT-Playground

ENTRYPOINT sbt console
