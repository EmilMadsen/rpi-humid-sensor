package dk.emad.humidsensor.service;

import dk.emad.humidsensor.sensor.DHT22;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Persist current humidity and temperature readings with given interval
 */

@Service
public class PersistinatorService {
    private static final Logger log = LoggerFactory.getLogger(PersistinatorService.class);

    private DHT22 dht22;
    private final TaskExecutor taskExecutor;

    public PersistinatorService(TaskExecutor taskExecutor) {
        this.dht22 = new DHT22();
        this.taskExecutor = taskExecutor;
    }

    @PostConstruct
    public void init() {
        log.info("init sensor");
        taskExecutor.execute(dht22);
    }

    @Scheduled(fixedDelay = 5000)
    public void doReading() {
        log.info("dht22 data: humid: {}, temp: {}", dht22.getHumidity(), dht22.getTemperature());
        // TODO: store data via api
    }

}
