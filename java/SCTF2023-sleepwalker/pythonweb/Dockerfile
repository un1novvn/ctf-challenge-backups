FROM python:3.8.16-slim-bullseye
ENV FLASK_APP=app.py FLASK_ENV=production
RUN sed -i "s@http://deb.debian.org@http://mirrors.aliyun.com@g" /etc/apt/sources.list
RUN cat /etc/apt/sources.list
RUN rm -Rf /var/lib/apt/lists/*
RUN apt clean&&apt update && apt install -y xxd
RUN groupadd ctfgroup
RUN useradd ctf -s /bin/bash -g ctfgroup
EXPOSE 5000

WORKDIR /app

USER root
COPY app /app
RUN chmod +x /app/start.sh
RUN pip install \
	-i http://mirrors.aliyun.com/pypi/simple/ \
	--trusted-host mirrors.aliyun.com \
	-r /app/requirements.txt
USER ctf
CMD ["./start.sh"]