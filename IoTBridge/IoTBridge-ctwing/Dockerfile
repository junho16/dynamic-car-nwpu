# =====================================================================
# 1. Build with: docker build -t xuanji/iotbridge-ctwing:1.0.0 .
# 2. Run with: docker run -p 8082:8082 -d --name iotbridge-ctwing xuanji/iotbridge-ctwing:1.0.0
# @see: https://spring.io/guides/gs/spring-boot-docker/#_containerize_it
# =====================================================================
FROM openjdk:8-jdk-alpine
RUN echo "https://mirrors.aliyun.com/alpine/v3.8/main/" > /etc/apk/repositories \
  && echo "https://mirrors.aliyun.com/alpine/v3.8/community/" >> /etc/apk/repositories \
  && apk add --no-cache tzdata \
  && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo Asia/Shanghai > /etc/timezone \
  && apk del tzdata
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","cps.IoTBridgeCTWing.IoTBridgeCTWingApplication"]
