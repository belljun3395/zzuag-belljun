# Pinpoint Agent Installation


## PinPoint Agent Installation Link

https://github.com/pinpoint-apm/pinpoint/releases/download/v2.3.3/pinpoint-agent-2.3.3.tar.gz

## PinPoint Agent Run Command

```
java -jar -javaagent:${PINPOIN_AGENT_PATH}\
-Dpinpoint.agentId=${AGENT_ID}\
-Dpinpoint.applicationName=${APPLICATION_NAME}\
-Dpinpoint.profiler.profiles.active=${PINPOIN_PROFILES}\
${JAR_FILE}
```


## PinPoint Agent profiles configuration

PintPoint Agent has default profiles "local", "release".

If you set any profiles it follows spring boot profiles. (Maybe?????)

So If you want to set custom profiles you can set it like this.

1. Move to the directory where the pinpoint-agent is located.
2. Move to profiles directory.
3. Create a new directory with the name of the profile you want to create.
4. Create log4j2.xml file in the created directory.
5. Configure pinpoint.config file to use the created profile.


## Pinpoint Agent settings to connect Pinpoint Collector

Open pinpoint.config file or pinpoint-root.config

Set below properties to connect pinpoint collector.

```
profiler.transport.grpc.collector.ip=${PINPOINT_COLLECTOR_IP}
profiler.collector.ip=${PINPOINT_COLLECTOR_IP}
```
