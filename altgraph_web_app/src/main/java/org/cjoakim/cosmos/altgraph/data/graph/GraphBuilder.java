package org.cjoakim.cosmos.altgraph.data.graph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.cjoakim.cosmos.altgraph.data.model.Entity;
import org.cjoakim.cosmos.altgraph.data.model.Triple;

import java.util.ArrayList;

/**
 * Instances of this class are used to build an in-memory Graph, from a given root,
 * and given a set of Triples in a given TripleQueryStruct.
 *
 * Chris Joakim, Microsoft, July 2022
 */

@Data
@JsonIgnoreProperties("struct")
@Slf4j
public class GraphBuilder {

    private Entity rootEntity;  // the starting point of the graph
    private TripleQueryStruct struct;  // DB query results of the pertinent Triples
    Graph graph;  // the resulting graph given the above two
    int structIterations = 0;


    public GraphBuilder(Entity rootEntity, TripleQueryStruct struct) {

        super();
        this.rootEntity = rootEntity;
        this.struct = struct;
        this.graph = new Graph();
    }

    public Graph buildLibraryGraph(int maxIterations) {

        String rootKey = rootEntity.getGraphKey();
        log.warn("buildLibraryGraph, rootKey: " + rootKey);
        graph.setRootNode(rootKey);

        collectLibraryGraph(maxIterations);  // iterate the triples and build the graph from them

        graph.finish();
        return graph;
    }

    private void collectLibraryGraph(int maxIterations) {

        log.warn("collectLibraryGraph, maxIterations: " + maxIterations);
        boolean continueToCollect = true;
        int iterations = 0;
        int newNodesThisIteration = 0;

        while (continueToCollect) {
            iterations++;
            newNodesThisIteration = 0;
            ArrayList<String> currentKeys = graph.getCurrentKeys();
            //log.warn("collect_start_of_iteration : " + iterations + ", currentKeys: " + currentKeys.size());
            for (int ck = 0; ck < currentKeys.size(); ck++) {
                //log.warn("collect_current_key, iteration " + iterations + ": " + currentKeys.get(ck));
            }

            for (int i = 0; i < getStruct().getDocuments().size(); i++) {
                // match for subject key, add object key
                Triple t = getStruct().getDocuments().get(i);
                String subjectKey = t.getSubjectKey();
                //log.warn("collect_subjectKey: " + subjectKey);

                if (currentKeys.contains(subjectKey)) {
                    int changes = graph.update(subjectKey, t.getObjectKey(), t.getPredicate());
                    newNodesThisIteration = newNodesThisIteration + changes;
                }
            }
            //log.warn("newNodesThisIteration: " + newNodesThisIteration + " for iteration: " + iterations);

            // terminate the while-loop as necessary
            if (newNodesThisIteration < 1) {
                continueToCollect = false;
                //log.error("collect() terminating with no new nodes");
            }
            else {
                if (iterations >= maxIterations) {
                    continueToCollect = false;  // possible infinite loop, eject!
                    log.error("collect() bailing out at maxIterations " + maxIterations);
                }
            }
        }

    }

    public String asJson(boolean pretty) throws Exception {

        try {
            ObjectMapper mapper = new ObjectMapper();
            if (pretty) {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
            }
            else {
                return mapper.writeValueAsString(this);
            }
        }
        catch (JsonProcessingException e) {
            return null;
        }
    }
}
