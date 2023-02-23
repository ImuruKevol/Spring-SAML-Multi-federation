#!/bin/bash

KEYSTORE_FILE=samlKeystore.jks
KEYSTORE_URL= # ex: sample.co.kr
KEYSTORE_ALIAS= # ex: testsp
KEYSTORE_PASSWORD= # ex: 1q2w3e4r5

keytool -delete -alias $KEYSTORE_ALIAS -keystore $KEYSTORE_FILE -storepass $KEYSTORE_PASSWORD

keytool -genkey -alias $KEYSTORE_ALIAS -validity 3650 -keyalg RSA -sigalg SHA256withRSA -keysize 2048 -keystore $KEYSTORE_FILE \
-keypass $KEYSTORE_PASSWORD -storepass $KEYSTORE_PASSWORD \
-dname "CN=$KEYSTORE_URL,OU=SDEV,O=SEASON,L=SS,S=SJ,C=KR"
