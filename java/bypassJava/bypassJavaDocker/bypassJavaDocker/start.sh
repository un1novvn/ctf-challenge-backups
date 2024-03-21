#!/bin/bash
rm -rf /start.sh
echo $FLAG > /flag
export FLAG=no_flag
chmod 700 /flag
chmod -R 755 /home/ctf
chmod -R 755 /home/ctf
su - ctf -c "/usr/local/openjdk-8/bin/java -jar /home/ctf/bypassJava-0.0.1-SNAPSHOT.jar" &
sleep 10
su - ctf -c "/usr/local/openjdk-8/bin/java -jar /home/ctf/simpleRASP.jar /home/ctf/bypassJava-0.0.1-SNAPSHOT.jar" &
tail -f /dev/null
