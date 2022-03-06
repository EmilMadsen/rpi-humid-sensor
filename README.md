# rpi-humid-sensor
Spring Boot service using pi4j library to watch input on GPIO pin on raspberry pi.   

### docker pi
build image for pi:  
```docker build -t emilmadsen/rpi-humid-sensor -f DockerfilePi .```  
push to dockerhub   
```docker push emilmadsen/rpi-humid-sensor```   
run on pi:  
```docker run --restart always --privileged emilmadsen/rpi-humid-sensor```


### ouroboros (watch+redeploy changes)
https://github.com/pyouroboros/ouroboros  
```docker run -d --restart always --name ouroboros -v /var/run/docker.sock:/var/run/docker.sock pyouroboros/ouroboros```
