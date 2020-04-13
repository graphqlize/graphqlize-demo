FROM openjdk:8

COPY ./build/distributions/graphqlize-boot-*.tar /tmp/graphqlize-boot.tar

WORKDIR /app

RUN mkdir -p /app && tar -xf /tmp/graphqlize-boot.tar --strip 1 && rm -rf /tmp/*.tar && ls -la . && ls -la /app/bin

ENTRYPOINT [ "/app/bin/graphqlize-boot" ]
