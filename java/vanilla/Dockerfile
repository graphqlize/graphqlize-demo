FROM openjdk:8

COPY build/distributions/vanilla-*.tar /tmp/vanilla.tar

WORKDIR /app

RUN mkdir -p /app && tar -xf /tmp/vanilla.tar --strip 1 && rm -rf /tmp/*.tar && ls -la . && ls -la /app/bin

ENTRYPOINT [ "/app/bin/vanilla" ]
