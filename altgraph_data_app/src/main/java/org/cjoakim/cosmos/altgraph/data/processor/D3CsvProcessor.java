package org.cjoakim.cosmos.altgraph.data.processor;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cjoakim.cosmos.altgraph.data.DataAppConstants;
import org.cjoakim.cosmos.altgraph.data.graph.D3CsvBuilder;
import org.cjoakim.cosmos.altgraph.data.graph.Graph;
import org.cjoakim.cosmos.altgraph.data.io.FileUtil;
import org.cjoakim.cosmos.altgraph.data.model.Library;
import org.springframework.stereotype.Component;

/**
 * Instances of this class are used for ad-hoc testing and development
 * of D3 CSV generation functionality within the (batch) DataCommandLineApp,
 *
 * Chris Joakim, Microsoft, July 2022
 */

@Component
@NoArgsConstructor
@Slf4j
public class D3CsvProcessor implements ConsoleAppProcess, DataAppConstants {

    public void process() throws Exception {

        Graph graph = readCapturedGraph();
        log.warn("graph: " + graph.asJson(true));

        String sessionId = "123"; // + System.currentTimeMillis();
        D3CsvBuilder builder = new D3CsvBuilder(graph);
        builder.buildBillOfMaterialCsv(sessionId, 2);
        builder.finish();
        //log.warn("" + builder.asJson(true));

        FileUtil fu = new FileUtil();
        fu.writeJson(builder, D3_CSV_BUILDER_FILE, true, true);
    }

    private Graph readCapturedGraph() throws Exception {

        return new FileUtil().readGraph("data/graph/graph.json");
    }

    private Library readTediousRootTriple() throws Exception {

        return new FileUtil().readLibrary("data/refined/tedious.json");
    }
}
