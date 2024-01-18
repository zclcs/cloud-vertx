FROM 192.168.33.10:3000/library/java-17:1.0.0

LABEL authors="zclcs"

ENV APP_DIR /app

ENV APP_CONF conf

ENV APP_FILE platform-1.0-fat.jar

WORKDIR $APP_DIR
COPY build/libs/$APP_FILE $APP_DIR/$APP_FILE
COPY src/main/resources/conf $APP_DIR/$APP_CONF

CMD java \
    -server                                           \
    -XX:+UseNUMA                                      \
    -XX:+UseParallelGC                                \
    -Dvertx.disableDnsResolver=true                   \
    -Dvertx.disableMetrics=true                       \
    -Dvertx.disableH2c=true                           \
    -Dvertx.disableWebsockets=true                    \
    -Dvertx.flashPolicyHandler=false                  \
    -Dvertx.threadChecks=false                        \
    -Dvertx.disableContextTimings=true                \
    -Dvertx.disableTCCL=true                          \
    -Dvertx.disableHttpHeadersValidation=true         \
    -Dvertx.eventLoopPoolSize=$((`grep --count ^processor /proc/cpuinfo`)) \
    -Dio.netty.buffer.checkBounds=false               \
    -Dio.netty.buffer.checkAccessible=false           \
    -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.Log4j2LogDelegateFactory \
    -Dlog4j2.contextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector \
    -Dlog4j2.asyncQueueFullPolicy=Discard             \
    -Dlog4j2.discardThreshold=INFO                    \
    -jar                                              \
    $APP_FILE                                         \
    --conf                                            \
    $APP_CONF/config.json                             \
    --options                                         \
    $APP_CONF/vertx.json