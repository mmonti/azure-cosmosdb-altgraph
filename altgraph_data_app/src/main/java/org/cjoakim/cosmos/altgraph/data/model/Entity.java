package org.cjoakim.cosmos.altgraph.data.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Version;

/**
 * This is superclass of classes Author, Library, and Maintainer.
 *
 * Chris Joakim, Microsoft, July 2022
 */
@Data
@NoArgsConstructor
@Slf4j
@Container(containerName="altgraph")
@JsonIgnoreProperties(value = {"cacheKey", "graphKey", "_etag"}, ignoreUnknown = true)
public class Entity {

    private String doctype;
    private String label;
    private String id;

    @PartitionKey
    private String pk;

    @Version
    String _etag;

    private String tenant;
    private String lob;
    private String cacheKey;
    private String graphKey;

    public void populateCacheKey() {
        cacheKey = doctype + "|" + label;
    }

    public String calculateGraphKey() {

        StringBuffer sb = new StringBuffer();
        sb.append(this.getDoctype());
        sb.append("^");
        sb.append(this.getLabel());
        sb.append("^");
        sb.append(this.getId());
        sb.append("^");
        sb.append(this.getPk());
        return sb.toString();
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
