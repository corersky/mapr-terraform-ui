How to build and run
====================

Modify src/main/resources/applciation.yml

Pass required paths.

Run 'terraform init' in Terraform project dir, if required.

```
gradle clean build
```

java -jar build/libs/maprdeploy-<version>.jar