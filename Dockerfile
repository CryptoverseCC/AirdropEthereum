FROM openjdk:8-jdk-alpine as builder
ENV a=1
RUN mkdir /workspace
WORKDIR /workspace
ADD gradle /workspace/gradle
ADD gradlew /workspace/gradlew
RUN ./gradlew init

ADD build.gradle /workspace/build.gradle
RUN ./gradlew check

ADD src /workspace/src
RUN ./gradlew build

FROM openjdk:8-jdk-alpine

COPY --from=builder /workspace/build/libs/app.jar .

ENTRYPOINT ["java","-jar","app.jar"]
