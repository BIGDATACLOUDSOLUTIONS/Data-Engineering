FROM ubuntu:latest

ARG MAVEN_VERSION=3.8.6
ARG USER_HOME_DIR="/root"
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries
ARG SBT_VERSION=3.8.5

RUN apt update

#Install Java 8
RUN echo "y" | apt install openjdk-8-jdk

# Define commonly used JAVA_HOME variable
RUN export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

# Install CURL
RUN echo "y" | apt install curl

#Install Vi Editor
 RUN echo "y" | apt install vim 

#Install zip
 RUN echo "y" | apt install zip

#Install unzip
RUN apt install unzip

# Install Maven.
RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
 && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
 && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
 && rm -f /tmp/apache-maven.tar.gz \
 && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

# Define working directory.
WORKDIR /data

# Define default command.
CMD ["mvn", "--version"]

RUN apt update
RUN apt install apt-transport-https curl gnupg -yqq
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | gpg --no-default-keyring --keyring gnupg-ring:/etc/apt/trusted.gpg.d/scalasbt-release.gpg --import
RUN chmod 644 /etc/apt/trusted.gpg.d/scalasbt-release.gpg
RUN apt update
RUN echo "y" | apt install sbt
