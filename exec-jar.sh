#!/bin/sh

#if [ $# -lt 2 ]; then
#  echo "usage: >`basename $0` [filename] [password]"
#  exit 0
#fi

cp sample-multi.zip target/

MODULE_NAME='aes-256-unzipper-executable'
cd target

BASEDIR=`pwd`
ZIPFILE=$BASEDIR/sample-multi.zip
ZIPPASS='passw0rd'

echo "java -jar ${MODULE_NAME}.jar $ZIPFILE $ZIPPASS"
java -jar ${MODULE_NAME}.jar $ZIPFILE $ZIPPASS

