FROM ubuntu:22.04
RUN rm /bin/sh && ln -s /bin/bash /bin/sh

RUN apt-get update
RUN apt-get install -y build-essential curl unzip zip wget inotify-tools iputils-ping

RUN apt-get install -y vim

RUN apt-get install -y nginx
COPY nginx/default /etc/nginx/sites-available/default
RUN mkdir -p /etc/nginx/ssl/local.api.spring-template.com
COPY local-ssl/local.api.spring-template.com/cert.pem /etc/nginx/ssl/local.api.spring-template.com/cert.pem
COPY local-ssl/local.api.spring-template.com/key.pem /etc/nginx/ssl/local.api.spring-template.com/key.pem
COPY local-ssl/minica.pem /usr/local/share/ca-certificates/minica.crt
RUN update-ca-certificates

RUN curl -s "https://get.sdkman.io" | bash
RUN source /root/.sdkman/bin/sdkman-init.sh \
    && sdk install java 20.0.1-amzn \
    && sdk use java 20.0.1-amzn

RUN wget https://mirrors.estointernet.in/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz \
    && tar -xvf apache-maven-3.6.3-bin.tar.gz \
    && mv apache-maven-3.6.3 /opt/apache-maven-3.6.3

ENV M2_HOME='/opt/apache-maven-3.6.3'
ENV PATH="$M2_HOME/bin:$PATH"
ENV JAVA_HOME=/root/.sdkman/candidates/java/20.0.1-amzn

COPY ./ /opt/api
WORKDIR /opt/api

CMD ["sh", "./docker/start.sh"]