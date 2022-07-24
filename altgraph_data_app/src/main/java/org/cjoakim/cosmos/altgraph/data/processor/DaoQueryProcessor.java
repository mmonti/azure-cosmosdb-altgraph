package org.cjoakim.cosmos.altgraph.data.processor;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cjoakim.cosmos.altgraph.data.DataAppConfiguration;
import org.cjoakim.cosmos.altgraph.data.DataAppConstants;
import org.cjoakim.cosmos.altgraph.data.dao.CosmosDAO;
import org.cjoakim.cosmos.altgraph.data.graph.TripleQueryStruct;
import org.cjoakim.cosmos.altgraph.data.io.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Instances of this class are used for ad-hoc testing and development
 * of Data Access Object (CosmosDB Java SDK) functionality within the
 * (batch) DataCommandLineApp,
 *
 * Chris Joakim, Microsoft, July 2022
 */

@Component
@NoArgsConstructor
@Slf4j
public class DaoQueryProcessor implements ConsoleAppProcess, DataAppConstants {

    @Value("${spring.cloud.azure.cosmos.endpoint}")
    public String uri;

    //@Value("${azure.cosmos.key}")
    @Value("${spring.cloud.azure.cosmos.key}")
    public String key;

    @Value("${spring.cloud.azure.cosmos.database}")
    private String dbName;

    private CosmosDAO dao = new CosmosDAO();

    public void process() throws Exception {

        try {
            String tenant = DataAppConfiguration.getTenant();
            log.warn("process, tenant: " + tenant);
            log.warn("process, uri:    " + uri);

            dao.initialize(uri, key, dbName);

            StringBuffer sb = new StringBuffer();
            sb.append("select * from c where c.doctype = 'triple'");
            sb.append(" and c.tenant = '123'");
            sb.append(" and c.lob = 'npm'");
            sb.append(" and c.subjectType = 'library'");
            sb.append(" and c.objectType = 'library'");
            //sb.append(" and c.predicate = 'uses_lib'");

            TripleQueryStruct allTriplesStruct = new TripleQueryStruct();
            allTriplesStruct.setContainerName("altgraph");
            allTriplesStruct.setSql(sb.toString());
            TripleQueryStruct result = dao.queryTriples(allTriplesStruct);
            //log.warn(result.asJson(true));

            FileUtil fu = new FileUtil();
            fu.writeJson(result, TRIPLE_QUERY_STRUCT_FILE, true, true);

            TripleQueryStruct struct2 = fu.readTripleQueryStruct(TRIPLE_QUERY_STRUCT_FILE);
            log.warn(struct2.asJson(true));
            log.warn("struct type:    " + struct2.getStructType());
            log.warn("documents size: " + struct2.getDocuments().size());

//    SELECT * FROM c
//    where c.doctype = 'library'
//    and c.pk = 'express'
//    and c.tenant = '123'
//    {
//        "doctype": "library",
//            "label": "express",
//            "id": "f5dd5a11-1d2c-41ed-97ea-08dcdeaf64e5",
//            "pk": "express",
//            "tenant": "123",
//            "cacheKey": "library|express",
//            "name": "express",
//            "desc": "Fast, unopinionated, minimalist web framework"

        }
        finally {
            dao.close();
        }
    }
}
