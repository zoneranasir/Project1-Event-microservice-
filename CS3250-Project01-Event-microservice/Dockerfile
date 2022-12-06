FROM --platform=linux/amd64 amazoncorretto:18-al2-full

WORKDIR /app/swdev

# Install required dependencies
RUN yum -y update && yum -y --setopt=skip_missing_names_on_install=False install \
    ca-certificates \
    dos2unix \
    git-core \
    gnupg \
    maven \
    nasm \
    postgresql \
    postgresql-contrib \
    python-pytest \
    tar \
    unzip \
    wget \
    zip

# Install Heroku CLI
RUN curl https://cli-assets.heroku.com/install.sh | sh

# Install the Spring Boot CLI
RUN curl -s "https://get.sdkman.io" | bash
RUN bash -c -i 'sdk install springboot'

COPY scripts/wait.sh /app/wait.sh
RUN dos2unix /app/wait.sh && chmod a+x /app/wait.sh
RUN mv /root /root.orig

ENV PS1='\u@\h \[\033[32m\]\w\[\033[33m\]\[\033[00m\] $ '

ENTRYPOINT ["/app/wait.sh"]
