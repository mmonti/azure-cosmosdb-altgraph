package org.cjoakim.cosmos.altgraph.data;

import lombok.extern.slf4j.Slf4j;
import org.cjoakim.cosmos.altgraph.data.processor.*;
import org.cjoakim.cosmos.altgraph.data.repository.TripleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * This is the entry=point for this Spring Boot application.
 * It is a "console app" due to the CommandLineRunner interface.
 *
 * Chris Joakim, Microsoft, July 2022
 */

@SpringBootApplication
@Slf4j
public class DataCommandLineApp implements CommandLineRunner, DataAppConstants {

    @Autowired private ApplicationContext applicationContext;
    @Autowired private TripleRepository tripleRepository;
    @Autowired private CosmosDbLoader cosmosDbLoader;
    @Autowired private RepoQueryProcessor repoQueryProcessor;
    @Autowired private DaoQueryProcessor daoQueryProcessor;
    @Autowired private D3CsvProcessor d3CsvProcessor;
    @Autowired private CacheProcessor cacheProcessor;
    @Autowired private RedisPocProcessor redisPocProcessor;

    @Autowired
    private GraphProcessor graphProcessor;

    public static void main(String[] args) {
        DataAppConfiguration.setCommandLineArgs(args);
        log.warn("main method...");
        SpringApplication.run(DataCommandLineApp.class, args);
    }

    public void run(String[] args) throws Exception {
        log.warn("run method...");
        DataAppConfiguration.setCommandLineArgs(args);
        String processType = args[0];
        ConsoleAppProcess processor = null;
        log.warn("run, processType: " + processType);

        try {
            switch (processType) {
                case "transform_raw_data":
                    processor = new RawDataTransformer();
                    processor.process();
                    break;
                case "load_cosmos":
                    cosmosDbLoader.process();
                    break;
                case "springdata_queries":
                    repoQueryProcessor.process();
                    break;
                case "dao_queries":
                    daoQueryProcessor.process();
                    break;
                case "build_graph":
                    graphProcessor.process();
                    break;
                case "build_d3_csv":
                    d3CsvProcessor.process();
                    break;
                case "test_cache":
                    cacheProcessor.process();
                    break;
                case "test_redis":
                    redisPocProcessor.process();
                    break;
                default:
                    log.error("unknown CLI process name: " + processType);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally {
            log.warn("spring app exiting");
            SpringApplication.exit(this.applicationContext);
            log.warn("spring app exit completed");
        }
    }
}
