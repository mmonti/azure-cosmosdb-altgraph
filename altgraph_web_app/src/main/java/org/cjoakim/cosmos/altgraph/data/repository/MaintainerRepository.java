package org.cjoakim.cosmos.altgraph.data.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.cjoakim.cosmos.altgraph.data.model.Maintainer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 *
 * Chris Joakim, Microsoft, July 2022
 */

@Component
@Repository
public interface MaintainerRepository extends CosmosRepository<Maintainer, String> {

}
