package org.cjoakim.cosmos.altgraph.web.forms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Instances of this class represent the several fields of the Graph HTML FORM
 * that are HTTP POSTed to the GraphController.
 *
 * Chris Joakim, Microsoft, July 2022
 */

@Data
@Slf4j
public class GraphForm {

    public static final int DEFAULT_DEPTH = 1;

    private String subjectName;

    private String graphDepth;

    private String cacheOpts;

    private String elapsedMs;

    private String sessionId;

    /**
     * The subjectName can be a delimited String "l:tedious", "tedious", "a:TJ...", or "m:xxxx".
     * Return the porting of the value after the colon, if present.
     */
    public String getSubjectValue() {

        if (getSubjectName() == null) {
            return null;
        }
        int colonIdx = getSubjectName().indexOf(':');
        if (colonIdx > 0) {
            return getSubjectName().substring(colonIdx + 1).trim();
        }
        else {
            return getSubjectName().trim();
        }
    }

    public boolean isLibrarySearch() {

        if (isAuthorSearch()) {
            return false;
        }
        if (isMaintainerSearch()) {
            return false;
        }
        return true;
    }

    public boolean isAuthorSearch() {

        if (getSubjectName() == null) {
            return false;
        }
        return getSubjectName().trim().toLowerCase().startsWith("a:");
    }

    public boolean isMaintainerSearch() {

        if (getSubjectName() == null) {
            return false;
        }
        return getSubjectName().trim().toLowerCase().startsWith("m:");
    }

    public int getDepthAsInt() {

        try {
            return Integer.parseInt(graphDepth);
        }
        catch (NumberFormatException e) {
            log.error("non-integer depth value: " + graphDepth + ".  returning the default");
            return DEFAULT_DEPTH;
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
