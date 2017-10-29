# Screen Casting App
Java app that captures screenshots of the current machine and exposes them via HTTP.

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
`server.port` system property.   
```
-Dserver.port=1234
```   

The display to grab can be configured via the `screencaster.grabbing.screenNo` system property.
The default `-1` selects the primary screen.

To select the second screen use:
```
-Dscreencaster.grabbing.screenNo=1
```

The image quality can be adjusted via the `screencaster.grabbing.quality` system property.  
The value must be a float between `0.0` and `1.0`.  The default quality is `0.7`.
```
-Dscreencaster.grabbing.quality=0.5
```