FROM openjdk:8-jdk

ENV DEBIAN_FRONTEND noninteractive

RUN useradd ctf && mkdir /opt/app

COPY readflag /readflag
COPY flag /flag
RUN chmod 0400 /flag
RUN chmod 4555 /readflag
COPY noMap.jar /opt/app
COPY neepurasp.jar /opt/app
WORKDIR /opt/app

EXPOSE 8090
USER ctf

CMD ["java", "-javaagent:/opt/app/neepurasp.jar", "-jar", "/opt/app/noMap.jar"]