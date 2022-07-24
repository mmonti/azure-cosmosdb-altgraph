package org.cjoakim.cosmos.altgraph.data.repository;

import org.cjoakim.cosmos.altgraph.data.model.Triple;

import java.util.ArrayList;

/**
 * This interface was created to extend the TripleRepository, which in turn extends
 * CosmosRepository<Triple, String> from the CosmosDB Spring Data SDK.
 *
 * This demonstrates how to leverage more of the power of the CosmosDB SQL syntax, by using
 * "Criteria" objects and an Autowired "CosmosTemplate" object.
 *
 * See class TripleRepositoryExtensionsImpl in this package, which implements this interface.
 *
 * Chris Joakim, Microsoft, July 2022
 */
public interface TripleRepositoryExtensions {
    public Iterable<Triple> findByTenantAndLobAndSubjectLabelsIn(String tenant, String lob, ArrayList<String> values);
}
