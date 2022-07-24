# AltGraph - Presentation

<p align="center">
    <img src="img/spacer-50.png">
</p>

### Chris Joakim, Microsoft, CosmosDB Global Black Belt (GBB)

### https://www.linkedin.com/in/chris-joakim-4859b89

<p align="center">
    <img src="img/spacer-50.png">
</p>

# Presentation Outline

## Introduction

### - Previous CosmosDB Live TV Sessions

### - Real-world Use Cases

## Perception: How you See the Problem often determines your solution

### - Sample Database Diagrams
### - Types of Databases 

## Think Differently: Why another Graph Implementation?

## Design
    
## Demonstration of the Reference Application

<p align="center">
    <img src="img/spacer-500.png">
</p>

---

## Introduction

### Previous CosmosDB Live TV Sessions

  - [Kushagra Thapar - Spring Data, 2022/02/03](https://www.youtube.com/watch?v=SUJecDgKZQM)
  - [Mark Heckler - Spring Boot, 2022/06/23](https://www.youtube.com/watch?v=4fSvyQw6luE)
    - [Spring Boot: Up and Running](https://www.oreilly.com/library/view/spring-boot-up/9781492076971/)
  - [List of Episodes](https://www.youtube.com/playlist?list=PLmamF3YkHLoKMzT3gP4oqHiJbjMaiiLEh)

### Real-World Use-Cases

  - **Manufacturing Bill-of-Material (BOM)**
  - **Social Network Systems** - People, Messages, Posts, Tags, etc
  - **Knowledge Graphs**

<p align="center">
    <img src="img/spacer-500.png">
</p>

## Perception: How you See the Problem often determines your solution

**What solution would you use if you diagrammed the problem like this?**

<p>&nbsp;</p>

<p align="center">
    <img src="img/AdventureWorksLT-ERD.png" width="75%">
</p>

<p align="center">
    <img src="img/spacer-200.png">
</p>

---

**Likewise, what solution would you use if you diagrammed the problem like this?**

<p>&nbsp;</p>

<p align="center">
    <img src="img/sample-graph.png" width="80%">
</p>

<p align="center">
    <img src="img/spacer-100.png">
</p>

## Types of Databases

### - Relational - Many similar products; ANSI standard
### - Graph - Several dissimilar products, in the LPG and RDF categories
### - NoSQL - Several dissimilar products, including CosmosDB SQL API

<p align="center">
    <img src="img/spacer-500.png">
</p>

---


## Think Differently: Why another Graph Implementation?

There ARE other options to RDF, LPG, and Relational - **the General Purpose CosmosDB SQL API**

### - Fast execution speed, and lower CosmosDB RU costs

### - Lower barrier to entry for new apps: conceptual simplicity, based on SQL

### - Reusable design.  Zero to POC in days.

### - Enables better integration with the rest of Azure

<p align="center">
    <img src="img/architecture.png">
</p>

<p align="center">
    <img src="img/spacer-500.png">
</p>

## Design Foundations

### The concept of RDF "Triples"

[Triplestores](https://en.wikipedia.org/wiki/Triplestore) on Wikipedia.

<p align="center">
    <img src="img/rdf-triple.png" width="70%">
</p>

#### Examples - Subject, Predicate, Object

```
Microsoft    is_a             Technology Company
Java         is_a             Programming Language
C#           is_a             Programming Language
CosmosDB     is_a             Database System
CosmosDB     is_a             NoSQL Database System
CosmosDB     has_a_sdk_for    Java
CosmosDB     has_a_sdk_for    C#
Chris        works_at         Microsoft
Chris        has_role         GBB

... typically millions of other granular triples like this in a DB ...
```

### The concept of an "Index" (as in a Book; not indexing in a DB)

**Indexes enable you to do fast lookups and direct reads.**

<p align="center">
    <img src="img/book-index.png" width="50%">
</p>

<p align="center">
    <img src="img/spacer-100.png">
</p>

### CosmosDB Partitioning and Partition Keys

<p align="center">
    <img src="img/partitions1.png" width="80%">
</p>

<p align="center">
    <img src="img/spacer-100.png">
</p>

### CosmosDB Indexing and Composite Indexes

Index individual attributes, and index **sets of attributes** (i.e. - composite) to match your queries.

### CosmosDB "Point Reads"

Read by Document ID and Partition Key for fastest speed and lowest cost.

### In-Memory Processing is much faster than DB Processing

Traversing an in-memory data structure is **1000s of times faster** than reading a DB or disk.

### Use Caching to increase performance and reduce costs

- Local Disk
- [Azure Redis Cache](https://azure.microsoft.com/en-us/services/cache/)
- [CosmosDB Integrated Cache](https://docs.microsoft.com/en-us/azure/cosmos-db/integrated-cache)

### The Spring Boot framework and Spring Data

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data](https://spring.io/projects/spring-data)
- [Spring Boot Dependency Injection - Autowiring, Convention over Configuration](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html)
  - Similar to the "magick" in [Ruby on Rails](https://rubyonrails.org)
- [Project Lombok](https://projectlombok.org) - greatly eliminate boilerplate code

### Open-source Graph Visualization with JavaScript and D3.js 

- [D3.js](https://d3js.org)

<p align="center">
    <img src="img/spacer-500.png">
</p>

## Design Implementation

### Use a Single Container in CosmosDB

- Name: **altgraph**
- Partition key: **/pk**
- Partition key can include a **tenant** value for multi-tenant applications
- [Hierarchical partition keys (preview)](https://docs.microsoft.com/en-us/azure/cosmos-db/hierarchical-partition-keys) can be used
- Use a **doctype** attribute to distinguish the types of documents
  - **author, maintainer, library, triple**
- Synapse Link is optional; it enables analytics and batch processing in Azure Synapse
  - [My Synapse Link GitHub Repo](https://github.com/cjoakim/azure-cosmosdb-synapse-link)

### NPM Library Documents, doctype = 'library'

Sample NPM Library document.  The **dependencies** array is what we'll build the triples and graph with.

```
{
  "doctype": "library",
  "label": "mssql",
  "id": "2aa4fc9e-7cd5-41a7-a521-b303ff184303",
  "pk": "mssql",
  "_etag": "\"0f0094ae-0000-0100-0000-62d9c53a0000\"",
  "tenant": "123",
  "lob": "npm",
  "cacheKey": "library|mssql",
  "graphKey": "library^mssql^2aa4fc9e-7cd5-41a7-a521-b303ff184303^mssql",
  "name": "mssql",
  "desc": "Microsoft SQL Server client for Node.js.",
  "keywords": [
    "database",
    "mssql",
    "sql",
    "server",
    "msnodesql",
    "sqlserver",
    "tds",
    "node-tds",
    "tedious",
    "node-sqlserver",
    "sqlserver",
    "msnodesqlv8",
    "azure",
    "node-mssql"
  ],
  "dependencies": {
    "debug": "^3.2.6",
    "generic-pool": "^3.6.1",
    "tedious": "^4.2.0"
  },
  "devDependencies": {
    "standard": "^11.0.1",
    "mocha": "^5.2.0"
  },
  "author": "Patrik Simek (https://patriksimek.cz)",
  "maintainers": [
    "arthurschreiber <schreiber.arthur@googlemail.com>",
    "dhensby <npm@dhensby.co.uk>",
    "patriksimek <patrik@patriksimek.cz>",
    "wmorgan <will+npm@willmorgan.co.uk>"
  ],
  "version": "5.1.0",
  "versions": [
    "0.2.0",
    "0.2.1",
    "0.2.2",
    ... many versions omitted here ...
    "6.0.0-beta.1"
  ],
  "homepage": "https://github.com/tediousjs/node-mssql#readme",
  "library_age_days": 2211,
  "version_age_days": 135
}
```

### Triple Documents - an enhanced version of RDF Triples, , doctype = 'triple'

- They are **small documents**; sample below is 1119 bytes as JSON
- They all reside in the same CosmosDB **logical partition**, therefore same **physical partition**, too.
  In this example, 123 is the tenant ID.
```
        "pk": "triple|123",
```
- Partition Key design **enables very fast reads of the entire index (as in book)** of your graph.
- An array of **in-memory Triples can be traversed/navigated 1000s of times faster** than a DB or disk.
- The Triples contain the **id/pk coordinates of the adjacent Entities for fast Point-Read lookups**
```
        "subjectId": "2aa4fc9e-7cd5-41a7-a521-b303ff184303",
        "subjectPk": "mssql",
```
- Enables **19 million** triples per tenant in your graph.  Python shell calculation shown below:
```
>>> gb
1073741824.0
>>> (gb * 20.0) / 1119.0
19191096.050044682
```

#### Sample Triple Document - note the several subject and object fields, plus predicate 

- These triples are analogous to **Edges** in a **LPG** graph, not granular triples as in RDF
- **lob** attribute - for having multiple and distinct **lines of business** in your graph
- **subjectTags** and **objectTags** - optional; enables you to peek into critical values of adjacent nodes
- The many Triples for your graph can be read into one **in-memory data structure**
```
    {
        "id": "47142cd6-c55c-4ce6-84f7-a356a87d0991",
        "pk": "triple|123",
        "_etag": "\"0f007db6-0000-0100-0000-62d9c5830000\"",
        "tenant": "123",
        "lob": "npm",
        "doctype": "triple",
        "subjectType": "library",
        "subjectLabel": "mssql",
        "subjectId": "2aa4fc9e-7cd5-41a7-a521-b303ff184303",
        "subjectPk": "mssql",
        "subjectKey": "library^mssql^2aa4fc9e-7cd5-41a7-a521-b303ff184303^mssql",
        "subjectTags": [
            "author|Patrik Simek (https://patriksimek.cz)"
        ],
        "predicate": "uses_lib",
        "objectType": "library",
        "objectLabel": "debug",
        "objectId": "eeb33106-3d98-4d39-a62d-791fe5565226",
        "objectPk": "debug",
        "objectKey": "library^debug^eeb33106-3d98-4d39-a62d-791fe5565226^debug",
        "objectTags": [
            "author|TJ Holowaychuk <tj@vision-media.ca>"
        ],
        "_rid": "gklzANbLiusnBwAAAAAAAA==",
        "_self": "dbs/gklzAA==/colls/gklzANbLius=/docs/gklzANbLiusnBwAAAAAAAA==/",
        "_attachments": "attachments/",
        "_ts": 1658439043
    }
```

### Primary Java Classes

See the **web_app** directory for these files.

Since the primary purpose of this presention relates to the **database design** the important
Java implementation classes are just listed and briefly described here.

#### Cache.java - implements caching logic, to local disk or Azure Redis Cache
#### D3CsvBuilder.java - Creates node and edge CSV files for D3.js
#### Graph.java - An in-memory graph created from a TripleQueryStruct
#### GraphBuilder.java - Builds a graph by iterating an in-memory TripleQueryStruct
#### TripleQueryStruct.java - Represents an Array of the Triples for your graph data
#### Library.java - An NPM library document
#### Triple.java - One Triple document
#### LibraryRepository.java - Spring Data Repository for Libraries
#### TripleRepository.java - Spring Data Repository for Libraries
#### TripleRepositoryExtensions.java - Extensions of the Repository for more complex SQL
#### TripleRepositoryExtensionsImpl.java
#### GraphController.java - The primary Controller, handles interaction with the UI

#### Gradle 

This project uses [Gradle](https://gradle.org) as the build and dependency management tool; see the build.gradle files
in this repo.  I find that Gradle is much simpler and less verbose than Apache Maven and the pom.xml file.

### TripleRepository

**The last @Query, below, is what fetches the Triples for your graph**.

``` 
@Component
@Repository
public interface TripleRepository extends CosmosRepository<Triple, String>, TripleRepositoryExtensions {

    Iterable<Triple> findBySubjectType(String subjectType);
    
    Iterable<Triple> findBySubjectLabel(String subjectLabel);
    
    Iterable<Triple> findByTenantAndSubjectLabel(String tenant, String subjectLabel);
    
    @Query("select value count(1) from c")
    long countAllTriples();
    
    @Query("select value count(1) from c where c.subjectLabel = @subjectLabel")
    long getNumberOfDocsWithSubjectLabel(@Param("subjectLabel") String subjectLabel);
    
    @Query("select * from c where c.pk = @pk and c.lob = @lob and c.subjectType = @subjectType and c.objectType = @objectType")
    List<Triple> getByPkLobAndSubjects(
            @Param("pk") String pk,      // "pk": "triple|123"
            @Param("lob") String lob,
            @Param("subjectType") String subjectType,
            @Param("objectType") String objectType);
...
```

#### The SQL

```
select * from c where c.pk = @pk and c.lob = @lob and c.subjectType = @subjectType and c.objectType = @objectType
```

### CosmosDB Composite Indexing

**The corresponding CosmosDB Composite Index for the above query**.

```
...

    [
      {
        "path": "/pk",
        "order": "ascending"
      },
      {
        "path": "/lob",
        "order": "ascending"
      },
      {
        "path": "/subjectType",
        "order": "ascending"
      },
      {
        "path": "/objectType",
        "order": "ascending"
      }
    ]
...
```

<p align="center">
    <img src="img/spacer-500.png">
</p>

## Demonstration

### UI Notes

- Use the Search Form to search by NPM Library Name 
- Depth can be specified to see a shallow or deep heirarchy 
- Cache Options L (Libraries) or T (Triples) can be used to specify reads from cache
- Click a Library to display its summary info at top of page
- Double-click a Library to see its graph
- The view is implemented with JavaScript and the open-source D3.js library

### UI Search Form 

<p align="center">
    <img src="img/UI-Search-Form.png">
</p>

<p align="center">
    <img src="img/spacer-300.png">
</p>

### Graph of the MSSQL library with Adjacent Nodes (dependencies)

<p align="center">
    <img src="img/UI-mssql-1-no-cache.png">
</p>

<p align="center">
    <img src="img/spacer-300.png">
</p>

### Graph of the MSSQL library with Adjacent Nodes, and their Adjacent Nodes

<p align="center">
    <img src="img/UI-mssql-2-no-cache.png">
</p>

<p align="center">
    <img src="img/spacer-300.png">
</p>

### Same as above, but with Caching

**Note the faster response time.**  The cache could be implemented with local disk,
Azure Redis Cache, or the **CosmosDB Integrated Cache**.

<p align="center">
    <img src="img/UI-mssql-2-with-cache.png">
</p>

<p align="center">
    <img src="img/spacer-500.png">
</p>

---

## Additional Links

- [Interesting Links](links.md)
- [How to Setup This App in your Environment](setup.md)

<p align="center">
    <img src="img/spacer-100.png">
</p>

## Wrapping Up

### Thank you!

### Questions or Comments?

