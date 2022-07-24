package org.cjoakim.cosmos.altgraph.data.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Version;

import java.util.ArrayList;

/**
 * Instances of this class represent the conceptual equivalent of an RDF Triplestore "Triple".
 * RDF triples contain a subject, predicate, and object.  Similarly, this class contains
 * several subject, predicate, and object attributes to enable the efficient representation
 * and navigation of the graph.  The size of the Triple object is designed to be as small
 * as possible to enable fast and efficient in-memory processing.
 *
 * Chris Joakim, Microsoft, July 2022
 */

@Data
@NoArgsConstructor
@Slf4j
@Container(containerName="altgraph")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Triple {

    private String id;

    @PartitionKey
    private String pk;

    @Version
    String _etag;

    private String tenant;
    private String lob;
    private String doctype;

    private String subjectType;
    private String subjectLabel;
    private String subjectId;
    private String subjectPk;
    private String subjectKey;

    private ArrayList<String> subjectTags = new ArrayList<String>();

    private String predicate;

    private String objectType;
    private String objectLabel;
    private String objectId;
    private String objectPk;
    private String objectKey;

    private ArrayList<String> objectTags = new ArrayList<String>();

    public void setKeyFields() {
        subjectKey = subjectType + "^" + subjectLabel + "^" + subjectId + "^" + subjectPk;
        objectKey  = objectType + "^" + objectLabel + "^" + objectId + "^" + objectPk;
    }

    public void addSubjectTag(String tag) {

        if (tag != null) {
            subjectTags.add(tag.trim());
        }
    }

    public void addObjectTag(String tag) {

        if (tag != null) {
            objectTags.add(tag.trim());
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
