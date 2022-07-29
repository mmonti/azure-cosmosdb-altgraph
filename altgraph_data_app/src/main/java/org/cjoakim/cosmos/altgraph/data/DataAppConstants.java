package org.cjoakim.cosmos.altgraph.data;

/**
 * This interface implements constants or hardcoded values used in the App.
 *
 * Chris Joakim, Microsoft, July 2022
 */
public interface DataAppConstants {

    // Command-line args
    public static final String VERBOSE_FLAG    = "--verbose";
    public static final String SILENT_FLAG     = "--silent";
    public static final String PRETTY_FLAG     = "--pretty";

    public static final String TENANT_FLAG     = "--tenant";

    public static final String CONTAINER_FLAG  = "--container";
    public static final String DO_WRITES_FLAG  = "--do-writes";

    public static final String LOB_FLAG        = "--lob";

    public static final String DELETE_ALL      = "--delete-all";

    // Document Types
    public static final String DOCTYPE_LIBRARY     = "library";
    public static final String DOCTYPE_AUTHOR      = "author";
    public static final String DOCTYPE_MAINTAINER  = "maintainer";
    public static final String DOCTYPE_TRIPLE      = "triple";

    // Default config values

    public static final String DEFAULT_CONTAINER   = "altgraph";
    public static final String DEFAULT_TENANT      = "123";
    public static final String LOB_NPM_LIBRARIES   = "npm";

    public static final String CACHE_WITH_LOCAL_DISK = "disk";
    public static final String CACHE_WITH_REDIS      = "redis";

    // Filenames
    public static final String RAW_LIBRARIES_FILE = "data/raw/aggregated_libraries.json";

    public static final String LIBRARIES_FILE     = "data/refined/libraries.json";
    public static final String AUTHORS_FILE       = "data/refined/authors.json";
    public static final String MAINTAINERS_FILE   = "data/refined/maintainers.json";
    public static final String TRIPLES_FILE       = "data/refined/triples.json";

    public static final String TRIPLE_QUERY_STRUCT_FILE = "data/struct/TripleQueryStruct.json";

    public static final String LIBRARY_GRAPH_JSON_FILE = "data/graph/library_graph.json";
    public static final String AUTHOR_GRAPH_JSON_FILE  = "data/graph/author_graph.json";
    public static final String GRAPH_NODES_CSV_FILE    = "data/graph/nodes.csv";
    public static final String GRAPH_EDGES_CSV_FILE    = "data/graph/edges.csv";
    public static final String D3_CSV_BUILDER_FILE     = "data/struct/D3CsvBuilder.json";
}
