FROM eclipse/ubuntu_jdk8
USER root
COPY SleepWalker.jar /opt/app/
ENV GZCTF_FLAG=SYCTF{wo_shi_da_shuai_ge}
COPY on.sh /
RUN chmod +x /on.sh
WORKDIR /opt/app
EXPOSE 8080
CMD ["/on.sh"]
