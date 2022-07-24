package org.cjoakim.cosmos.altgraph.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;

/**
 * Instances of this class represent a NPM (Node.js Package Manager) Library.
 *
 * Chris Joakim, Microsoft, July 2022
 */

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@EqualsAndHashCode(callSuper=false)
public class Library extends Entity {

    public Library() {
        super();
    }

    private String name;
    private String desc;
    private String[] keywords;
    private HashMap<String, String> dependencies;
    private HashMap<String, String> devDependencies;
    private String author;
    private String[] maintainers;
    private String version;
    private String[] versions;
    private String homepage;

    private int library_age_days;
    private int version_age_days;
}
