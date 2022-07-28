package org.cjoakim.cosmos.altgraph.data.graph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.cjoakim.cosmos.altgraph.data.model.Author;
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

    public Graph buildAuthorGraph(Author author) {

        String authorName = author.getAuthorName();
        String rootKey = author.getGraphKey();
        graph.setRootNode(author.getGraphKey());
        log.warn("buildAuthorGraph, author: " + author.getAuthorName() + ", rootKey: " + rootKey);

        for (int i = 0; i < getStruct().getDocuments().size(); i++) {
            Triple t = getStruct().getDocuments().get(i);
            ArrayList<String> tags = t.getSubjectTags();
            for (int tidx = 0; tidx < tags.size(); tidx++) {
                String value = tags.get(tidx);
                if (value.startsWith("author")) {
                    if (value.contains(authorName)) {
                        graph.update(t.getSubjectKey(), t.getObjectKey(), t.getPredicate());
                    }
                }
            }
        }
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
            for (int i = 0; i < getStruct().getDocuments().size(); i++) {
                // match for subject key, add object key
                Triple t = getStruct().getDocuments().get(i);
                if (t.getSubjectType().equals("library")) {
                    String subjectKey = t.getSubjectKey();
                    if (currentKeys.contains(subjectKey)) {
                        int changes = graph.update(subjectKey, t.getObjectKey(), t.getPredicate());
                        newNodesThisIteration = newNodesThisIteration + changes;
                    }
                }
            }
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
