How to build and run
====================

Modify src/main/resources/application.yml

Pass required paths.

Run 'terraform init' in Terraform project dir, if required.

```
./gradlew clean build
```

java -jar build/libs/maprdeploy-<version>.jar