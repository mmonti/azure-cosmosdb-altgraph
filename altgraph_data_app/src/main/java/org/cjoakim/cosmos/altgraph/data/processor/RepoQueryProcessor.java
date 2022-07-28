package org.cjoakim.cosmos.altgraph.data.processor;

import com.azure.spring.data.cosmos.core.CosmosTemplate;
import lombok.extern.slf4j.Slf4j;
import org.cjoakim.cosmos.altgraph.data.DataAppConfiguration;
import org.cjoakim.cosmos.altgraph.data.DataAppConstants;
import org.cjoakim.cosmos.altgraph.data.graph.TripleQueryStruct;
import org.cjoakim.cosmos.altgraph.data.io.FileUtil;
import org.cjoakim.cosmos.altgraph.data.model.Library;
import org.cjoakim.cosmos.altgraph.data.model.Triple;
import org.cjoakim.cosmos.altgraph.data.repository.AuthorRepository;
import org.cjoakim.cosmos.altgraph.data.repository.LibraryRepository;
import org.cjoakim.cosmos.altgraph.data.repository.MaintainerRepository;
import org.cjoakim.cosmos.altgraph.data.repository.TripleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Instances of this class are used for ad-hoc testing and development
 * of CosmosDB Spring Data Repository functionality within the (batch)
 * DataCommandLineApp,
 *
 * Chris Joakim, Microsoft, July 2022
 */

@Component
@Slf4j
public class RepoQueryProcessor implements ConsoleAppProcess, DataAppConstants {

    private AuthorRepository authorRepository = null;

    private LibraryRepository libraryRepository = null;

    private MaintainerRepository maintainerRepository = null;

    private TripleRepository tripleRepository = null;

    private CosmosTemplate template;

    @Autowired
    public RepoQueryProcessor(
            AuthorRepository ar,
            LibraryRepository lr,
            MaintainerRepository mr,
            TripleRepository tr,
            CosmosTemplate t) {
        super();
        this.authorRepository = ar;
        this.libraryRepository = lr;
        this.maintainerRepository = mr;
        this.tripleRepository = tr;
        this.template = t;
        log.warn("RepoQueryProcessor autowired constructor called");
    }

    public void process() throws Exception {

        String tenant = DataAppConfiguration.getTenant();
        String lob    = DataAppConfiguration.getLineOfBusiness();
        log.warn("process, tenant: " + tenant);

        ArrayList<String> libsOfInterest = new ArrayList<String>();
        libsOfInterest.add("tedious");
        libsOfInterest.add("adal-node");
        libsOfInterest.add("express");
        libsOfInterest.add("m26-js");

        FileUtil fu = new FileUtil();

        // Find the Libraries
        if (true) {
            for (int i = 0; i < libsOfInterest.size(); i++) {
                String libName = libsOfInterest.get(i);
                Iterable<Library> iterable = libraryRepository.findByPkAndTenant(libName, tenant);
                iterable.forEach(doc -> {
                    try {
                        String name = doc.getName();
                        String outfile = "data/refined/" + name + ".json";
                        fu.writeJson(doc, outfile, true, true);
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }

        // Find the corresponding Triples
        if (false) {
            for (int i = 0; i < libsOfInterest.size(); i++) {
                String libName = libsOfInterest.get(i);
                Iterable<Triple> iterable = tripleRepository.findByTenantAndSubjectLabel(tenant, libName);
                iterable.forEach(doc -> {
                    try {
                        System.out.println("doc: " + doc.asJson(true));
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }

        if (false) {
            // Find with more complex SQL in Repository method
            ArrayList<String> labels = new  ArrayList<String>();
            labels.add("@azure/cosmos");
            labels.add("@azure/cosmos-sign");
            labels.add("@azure/amqp-common");

            for (int i = 0; i < libsOfInterest.size(); i++) {
                String libName = libsOfInterest.get(i);
                Iterable<Triple> iterable = tripleRepository.getByTenantAndSubjectLabels(tenant, labels);
                iterable.forEach(doc -> {
                    try {
                        System.out.println("doc: " + doc.asJson(true));
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }

        if (false) {
            long count = tripleRepository.getNumberOfDocsWithSubjectLabel("m26-js");
            log.warn("count of getNumberOfDocsWithSubjectLabel: " + count);
        }

        if (false) {
            String subject = "library";
            TripleQueryStruct struct = new TripleQueryStruct();
            struct.setSql("dynamic");
            struct.start();

            String pk = "triple|" + tenant; // "pk": "triple|123"'
            Iterable<Triple> iterable = tripleRepository.getByPkLobAndSubjects(pk, lob, subject, subject);
            Iterator<Triple> it = iterable.iterator();
            long docCount = 0;
            while (it.hasNext()) {
                Triple t = it.next();
                struct.addDocument(t);
                docCount++;
            }
            struct.stop();
            log.warn("getByTenantLobAndSubjects count: " + docCount);
            fu.writeJson(struct, TRIPLE_QUERY_STRUCT_FILE, true, true);

            TripleQueryStruct struct2 = fu.readTripleQueryStruct(TRIPLE_QUERY_STRUCT_FILE);
            log.warn("struct type:    " + struct2.getStructType());
            log.warn("documents size: " + struct2.getDocuments().size());
        }

        if (true) {
            TripleQueryStruct struct = new TripleQueryStruct();
            struct.setSql("dynamic");
            struct.start();

            ArrayList<String> subjectTypes = new ArrayList<String>();
            subjectTypes.add("author");
            subjectTypes.add("library");

            String pk = "triple|" + tenant; // "pk": "triple|123"'
            Iterable<Triple> iterable = tripleRepository.getByPkTenantAndSubjectTypes(pk, tenant, subjectTypes);
            Iterator<Triple> it = iterable.iterator();
            long docCount = 0;
            while (it.hasNext()) {
                Triple t = it.next();
                struct.addDocument(t);
                docCount++;
            }
            struct.stop();
            log.warn("getByTenantAndSubjectTypes count: " + docCount);
            fu.writeJson(struct, TRIPLE_QUERY_STRUCT_FILE, true, true);

            TripleQueryStruct struct2 = fu.readTripleQueryStruct(TRIPLE_QUERY_STRUCT_FILE);
            log.warn("struct type:    " + struct2.getStructType());
            log.warn("documents size: " + struct2.getDocuments().size());
        }

        if (false) {
            Iterable<Library> iterable = libraryRepository.findByPkAndTenantAndDoctype(
                    "tedious", tenant, "library");
            Iterator<Library> it = iterable.iterator();
            long docCount = 0;
            while (it.hasNext()) {
                Library lib = it.next();
                System.out.println(lib.asJson(true));
                docCount++;
            }
            log.warn("findByTenantAndDoctypeAndPk count: " + docCount);
        }

        if (true) {
            // Test the extension method in TripleRepositoryExtensions
            TripleQueryStruct struct = new TripleQueryStruct();
            ArrayList<String> subjectLabels = new ArrayList<String>();
            subjectLabels.add("m26-js");
            subjectLabels.add("tcx-js");
            Iterable<Triple> iterable = tripleRepository.findByTenantAndLobAndSubjectLabelsIn(
                    tenant, lob, subjectLabels);
            Iterator<Triple> it = iterable.iterator();
            long docCount = 0;
            while (it.hasNext()) {
                Triple t = it.next();
                System.out.println(t.asJson(true));
                docCount++;
            }
            log.warn("findBySubjectLabelsIn count: " + docCount);
        }

        if (true) {
            // example of using CosmosTemplate outside of a Repository
            log.warn("template: " + template);
            long count = template.count("altgraph");
            log.warn("doc count in altgraph container: " + count);
        }

        long count = tripleRepository.countAllTriples();
        log.warn("count ofcountAllTriples: " + count);
    }
}
