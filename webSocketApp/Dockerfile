FROM gradle:jdk17-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build

FROM gradle:jdk17-alpine AS application

## Add app user
ARG APPLICATION_USER=appuser
RUN adduser -u 10000 -D $APPLICATION_USER
#
## Configure working directory
RUN mkdir /app && \
   chown -R $APPLICATION_USER /app

## Install aws-cli
RUN apk --update add aws-cli
#
USER 10000
#
WORKDIR /app

##Configure AWA-CLI
RUN aws configure set aws_access_key_id MYAWSACCESSKEYID
RUN aws configure set aws_secret_access_key MYAWSSECRETACCESSKEY
RUN aws configure set region us-east-1
RUN aws configure set output json

#
COPY --from=build --chown=10000:10000 /app/build/libs/webSocketApp-0.0.1-SNAPSHOT.jar /app/application.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app/application.jar", "--CLOUD_AWS_ENDPOINT=http://localstack:4566" ]