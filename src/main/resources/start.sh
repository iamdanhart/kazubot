#!/bin/bash

java -Ddiscord.token=${DISCORD_TOKEN} \
     -Dxivapi.token=${XIVAPI_TOKEN} \
     -jar target/Kazubot-1.0-SNAPSHOT.jar