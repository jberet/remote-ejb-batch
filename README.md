# remote-ejb-batch
Start batch job execution from WildFly remote ejb client

## Add application user to WildFly
```
$JBOSS_HOME/bin/add-user.sh -a -u user1 -p user1
```

## Build the application
```
mvn clean package
```

## Deploy the webapp to a running WildFly server
```text
mvn wildfly:deploy
```

## Run client program
```
# to run client program as a guest without providing username:
mvn exec:exec

# to run client program as user1:
mvn exec:exec -Duser=user1 -Dpassword=user1
```

## Undeploy the webapp from WildFly server
```text
mvn wildfly:undeploy
```