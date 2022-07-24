package org.cjoakim.cosmos.altgraph.data.processor;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cjoakim.cosmos.altgraph.data.DataAppConstants;
import org.cjoakim.cosmos.altgraph.data.graph.Graph;
import org.cjoakim.cosmos.altgraph.data.graph.GraphBuilder;
import org.cjoakim.cosmos.altgraph.data.graph.GraphNode;
import org.cjoakim.cosmos.altgraph.data.graph.TripleQueryStruct;
import org.cjoakim.cosmos.altgraph.data.io.FileUtil;
import org.cjoakim.cosmos.altgraph.data.model.Library;
import org.springframework.stereotype.Component;

/**
 * Instances of this class are used for ad-hoc testing and development
 * of Graph generation functionality within the (batch) DataCommandLineApp,
 *
 * Chris Joakim, Microsoft, July 2022
 */
@Component
@NoArgsConstructor
@Slf4j
public class GraphProcessor implements ConsoleAppProcess, DataAppConstants {

    public void process() throws Exception {

        FileUtil fu = new FileUtil();
        TripleQueryStruct struct = fu.readTripleQueryStruct(TRIPLE_QUERY_STRUCT_FILE);
        //log.warn(struct.asJson(true));
        log.warn("TripleQueryStruct documents size: " + struct.getDocuments().size());

        Library rootLibrary = readTediousRootTriple();
        log.warn("rootLibrary: " + rootLibrary.asJson(true));

        GraphBuilder builder = new GraphBuilder(rootLibrary, struct);
        Graph graph = builder.build(50);
        fu.writeJson(graph, GRAPH_JSON_FILE, true, true);

        log.warn("Graph root lib dependencies is correct: " + verifyGraph(rootLibrary, graph));
    }

    private Library readExpressRootTriple() throws Exception {

        return new FileUtil().readLibrary("data/refined/express.json");
    }

    private Library readTediousRootTriple() throws Exception {

        return new FileUtil().readLibrary("data/refined/tedious.json");
    }

    /**
     * Compare the set of dependencies in the given Library document vs the set
     * of dependencies calculated in the Graph, as created by class GraphBuilder.
     */
    private boolean verifyGraph(Library lib, Graph graph) throws Exception {
        int expectedLibsFound = 0;

        log.warn("verifyGraph, Library: " + lib.asJson(true));

        String rootKey = lib.getGraphKey();
        log.warn("verifyGraph, rootKey: " + rootKey);

        GraphNode rootNode = graph.getGraphMap().get(rootKey);
        log.warn("verifyGraph, Graph rootNode: " + rootNode.asJson(true));

        Object[] libKeys = lib.getDependencies().keySet().toArray();
        Object[] nodeKeys = rootNode.getAdjacentNodes().keySet().toArray();

        for (int i = 0; i < libKeys.length; i++) {
            String libName = (String) libKeys[i];
            String expectedKeyPrefix = "library^" + libName + "^"; // library^depd^bb9c0d1f-52b0-470c-abe4-1681a03b8aa3^depd

            for (int nk = 0; nk < nodeKeys.length; nk++) {
                String nodeDepLibKey = (String) nodeKeys[nk];
                if (nodeDepLibKey.startsWith(expectedKeyPrefix)) {
                    expectedLibsFound++;
                    log.warn("verifyGraph, found: " + expectedKeyPrefix);
                }
            }
        }
        log.warn("verifyGraph, Library dep count:   " + libKeys.length);
        log.warn("verifyGraph, GraphNode dep count: " + nodeKeys.length);
        log.warn("verifyGraph, expectedLibsFound:   " + expectedLibsFound);
        if (libKeys.length == nodeKeys.length) {
            if (libKeys.length == expectedLibsFound) {
                return true;
            }
        }
        return false;
    }
}
