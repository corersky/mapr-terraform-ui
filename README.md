How to build and run
====================

Modify src/main/resources/application.yml

Pass required paths.

Run 'terraform init' in Terraform project dir, if required.

```
./gradlew clean build
```

java -jar build/libs/maprdeploy-<version>.jar



Generate SSL certificate:
https://www.baeldung.com/spring-boot-https-self-signed-certificate

keytool -genkeypair -alias maprdeployer -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore maprdeployer.p12 -validity 3650