package org.cjoakim.cosmos.altgraph.data.processor;

import lombok.extern.slf4j.Slf4j;
import org.cjoakim.cosmos.altgraph.data.DataAppConfiguration;
import org.cjoakim.cosmos.altgraph.data.DataAppConstants;
import org.cjoakim.cosmos.altgraph.data.cache.Cache;
import org.cjoakim.cosmos.altgraph.data.graph.TripleQueryStruct;
import org.cjoakim.cosmos.altgraph.data.io.FileUtil;
import org.cjoakim.cosmos.altgraph.data.model.Library;
import org.cjoakim.cosmos.altgraph.data.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * Instances of this class are used for ad-hoc testing and development
 * of Cache functionality within the (batch) DataCommandLineApp,
 *
 * Chris Joakim, Microsoft, July 2022
 */

@Component
@Slf4j
public class CacheProcessor implements ConsoleAppProcess, DataAppConstants {

    private Cache cache = null;
    private LibraryRepository libraryRepository = null;

    @Autowired
    public CacheProcessor(Cache c, LibraryRepository lr) {
        super();
        this.cache = c;
        this.libraryRepository = lr;
        log.warn("CacheProcessor autowired constructor called");
    }

    public void process() throws Exception {

        Library lib = readLibrary("express");
        TripleQueryStruct triples = (new FileUtil()).readTripleQueryStruct("data/samples/TripleQueryStruct.json");
        log.warn("triples loaded, count: " + triples.getDocuments().size());

        // First do disk-based caching - Library, then Triples
        cache.toggleToDisk();

        String json = lib.asJson(false);
        log.warn("read express from the DB, json size: " + json.length());
        cache.putLibrary(lib);
        Library lib2 = cache.getLibrary("express");
        log.warn(lib2.asJson(true));

        cache.putTriples(triples);
        log.warn("triples written to disk cache, count: " + triples.getDocuments().size());
        TripleQueryStruct struct2 = cache.getTriples();
        log.warn("triples loaded from disk cache, count: " + struct2.getDocuments().size());

        // Next do redis-based caching - Library, then Triples
        cache.toggleToRedis();

        cache.putLibrary(lib);
        Library lib3 = cache.getLibrary("express");
        log.warn(lib3.asJson(true));

        long t1 = System.currentTimeMillis();
        cache.putTriples(triples);
        long t2 = System.currentTimeMillis();
        log.warn("triples written to redis cache, count: " + triples.getDocuments().size() + " in " + (t2 - t1));

        t1 = System.currentTimeMillis();
        TripleQueryStruct struct3 = cache.getTriples();
        t2 = System.currentTimeMillis();
        log.warn("triples loaded from redis cache, count: " + struct3.getDocuments().size() + " in " + (t2 - t1));
    }

    private Library readLibrary(String libName) {

        Iterable<Library> iterable = libraryRepository.findByPkAndTenantAndDoctype(
                libName, DataAppConfiguration.getTenant(), "library");
        Iterator<Library> it = iterable.iterator();
        while (it.hasNext()) {
            return it.next();
        }
        return null;
    }
}
