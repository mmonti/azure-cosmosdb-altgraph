package org.cjoakim.cosmos.altgraph.data.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.cjoakim.cosmos.altgraph.data.model.Library;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 *
 * Chris Joakim, Microsoft, July 2022
 */

@Component
@Repository
public interface LibraryRepository extends CosmosRepository<Library, String> {

    Iterable<Library> findByPkAndTenant(String pk, String tenant);

    Iterable<Library> findByPkAndTenantAndDoctype(String pk, String tenant, String doctype);

}
