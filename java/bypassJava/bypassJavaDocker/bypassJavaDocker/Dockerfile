FROM openjdk:8u342-jdk-slim

COPY readflag /readflag

RUN useradd ctf && chmod u+s /bin/cat && chmod u+s /readflag && chmod 755 /readflag

COPY simpleRASP.jar /home/ctf/simpleRASP.jar

COPY bypassJava-0.0.1-SNAPSHOT.jar /home/ctf/bypassJava-0.0.1-SNAPSHOT.jar

COPY start.sh /start.sh

RUN chmod +x /start.sh
# sed -i 's/deb.debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list && apt update -y

CMD [ "/bin/bash" ,"/start.sh" ]