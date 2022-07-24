package org.cjoakim.cosmos.altgraph.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Instances of this class represent a NPM (Node.js Package Manager) library Author.
 *
 * Chris Joakim, Microsoft, July 2022
 */

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class Author extends Entity {

    private String authorName;

    public Author() {

        super();
    }
}
