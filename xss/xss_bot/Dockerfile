FROM debian:buster-slim

RUN sed -i "s@http://.*deb.debian.org@http://mirrors.aliyun.com@g" /etc/apt/sources.list
RUN sed -i "s@http://.*security.debian.org@http://mirrors.aliyun.com@g" /etc/apt/sources.list
RUN apt-get update && apt-get -y dist-upgrade && \
    apt-get install -y xinetd python3 python3-pip python3-selenium chromium-driver &&\
    rm -rf /var/lib/apt/lists/*

RUN useradd -m ctf

WORKDIR /home/ctf

RUN mkdir /home/ctf/bin
COPY ./ctf.xinetd /etc/xinetd.d/ctf
COPY ./start.sh /start.sh
RUN echo "Blocked by ctf_xinetd" > /etc/banner_fail

RUN chmod +x /start.sh
COPY ./bin/ /home/ctf/
RUN chown -R root:ctf /home/ctf && \
    chmod -R 750 /home/ctf && \
    echo "flag{flaggggggggggg}" > /flag &&\
    chmod 777 /flag

CMD ["/start.sh"]

EXPOSE 9999
