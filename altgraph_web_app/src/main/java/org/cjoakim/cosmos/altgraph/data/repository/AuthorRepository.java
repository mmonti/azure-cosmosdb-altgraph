package org.cjoakim.cosmos.altgraph.data.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.cjoakim.cosmos.altgraph.data.model.Author;
import org.cjoakim.cosmos.altgraph.data.model.Library;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 *
 * Chris Joakim, Microsoft, July 2022
 */

@Component
@Repository
public interface AuthorRepository extends CosmosRepository<Author, String> {

    Iterable<Author> findByLabel(String label);
}
