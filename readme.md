# Screen Casting App
Java app that can capture the screen of the current machine and exposes them via HTTP.

## Build 
```
mvn clean package
```

## Run
```
java -jar target/screen-casting-app.jar
```

## Configuration

The screen-casting-app listens on port 9999 by default. One can customize the port by setting the  
`server.port` system property, e.g. via `-Dserver.port=1234`. 
