#!/bin/sh

mvn clean package

cp target/aes-256-unzipper-executable.jar assets/html

ls -ltr assets/html

