package org.cjoakim.cosmos.altgraph.data.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cjoakim.cosmos.altgraph.data.DataAppConfiguration;
import org.cjoakim.cosmos.altgraph.data.DataAppConstants;
import org.cjoakim.cosmos.altgraph.data.model.Author;
import org.cjoakim.cosmos.altgraph.data.model.Library;
import org.cjoakim.cosmos.altgraph.data.model.Maintainer;
import org.cjoakim.cosmos.altgraph.data.model.Triple;
import org.cjoakim.cosmos.altgraph.data.repository.AuthorRepository;
import org.cjoakim.cosmos.altgraph.data.repository.LibraryRepository;
import org.cjoakim.cosmos.altgraph.data.repository.MaintainerRepository;
import org.cjoakim.cosmos.altgraph.data.repository.TripleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * An instance of this class is created and executed from the DataCommandLineApp
 * main class to load CosmosDB from the previously transformed data.
 *
 * Chris Joakim, Microsoft, July 2022
 */

@Component
@NoArgsConstructor
@Slf4j
public class CosmosDbLoader implements ConsoleAppProcess, DataAppConstants {
    @Autowired
    private AuthorRepository authorRepository = null;
    @Autowired
    private LibraryRepository libraryRepository = null;
    @Autowired
    private MaintainerRepository maintainerRepository = null;
    @Autowired
    private TripleRepository tripleRepository = null;
    private boolean doWrites = false;

    public void process() throws Exception {

        doWrites = DataAppConfiguration.booleanArg(DO_WRITES_FLAG);
        log.warn("process, doWrites: " + doWrites);

        loadAuthors();
        loadMaintainers();
        loadLibraries();
        loadTriples();
    }

    private void loadLibraries() throws Exception {
        log.warn("loadLibraries...");

        try {
            String infile = LIBRARIES_FILE;
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Library>>(){});
            List<Library> entities = objectReader.readValue(Paths.get(infile).toFile());
            log.warn("infile read: " + infile + ", count: " + entities.size());

            for (int i = 0; i < entities.size(); i++) {
                Library entity = entities.get(i);
                log.warn("entity: " + entity.asJson(true));
                if (doWrites) {
                    try {
                        Object result = libraryRepository.save(entity);
                    }
                    catch (Exception e) {
                        log.error("error loading document: " + entity.asJson(true));
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAuthors() throws Exception {

        log.warn("loadAuthors...");
        try {
            String infile = AUTHORS_FILE;
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Author>>(){});
            List<Author> entities = objectReader.readValue(Paths.get(infile).toFile());
            log.warn("infile read: " + infile + ", count: " + entities.size());

            for (int i = 0; i < entities.size(); i++) {
                Author entity = entities.get(i);
                log.warn("entity: " + entity.asJson(true));
                if (doWrites) {
                    try {
                        authorRepository.save(entity);
                    }
                    catch (Exception e) {
                        log.error("error loading document: " + entity.asJson(true));
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMaintainers() throws Exception {

        log.warn("loadMaintainers...");

        try {
            String infile = MAINTAINERS_FILE;
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Maintainer>>(){});
            List<Maintainer> entities = objectReader.readValue(Paths.get(infile).toFile());
            log.warn("infile read: " + infile + ", count: " + entities.size());

            for (int i = 0; i < entities.size(); i++) {
                Maintainer entity = entities.get(i);
                log.warn("entity: " + entity.asJson(true));
                if (doWrites) {
                    try {
                        Object result = maintainerRepository.save(entity);
                    }
                    catch (Exception e) {
                        log.error("error loading document: " + entity.asJson(true));
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTriples() throws Exception {

        try {
            String infile = TRIPLES_FILE;
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Triple>>(){});
            List<Triple> triples = objectReader.readValue(Paths.get(infile).toFile());
            log.warn("infile read: " + infile + ", count: " + triples.size());

            for (int i = 0; i < triples.size(); i++) {
                Triple entity = triples.get(i);
                entity.setKeyFields();
                entity.set_etag("");
                log.warn("entity: " + entity.asJson(true));
                if (doWrites) {
                    try {
                        tripleRepository.save(entity);
                        log.warn("doc " + i + " saved");
                    }
                    catch (Throwable t) {
                        log.error("error loading document: " + entity.asJson(true));
                        t.printStackTrace();
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
