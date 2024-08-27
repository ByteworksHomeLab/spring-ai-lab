# Vector Embedding
[Previous](4-Spring-AI.md) | [Next](..%2F..%2F..%2F..%2F..%2FREADME.md)

There is a CSV file with 15,000 Airbnb listings from Austin, TX. These need to run through the embedding model and 
written to the `vector_table`. A `/run-ingestion` endpoint was added as a convenient way to do the embedding.
It takes about an hour to run, depending on your hardware. With the Spring Boot application running, paste this URL in your browser:

```shell
http://localhost:8080/run-ingestion
```

Messages like these will start appearing in the Spring Boot console output:

```shell
2024-08-18T19:54:53.398-05:00  INFO 13846 --- [spring-ai-airbnb] [oundedElastic-2] o.s.a.transformer.splitter.TextSplitter  : Splitting up document into 2 chunks.
2024-08-18T19:54:53.762-05:00  INFO 13846 --- [spring-ai-airbnb] [oundedElastic-2] o.s.a.transformer.splitter.TextSplitter  : Splitting up document into 2 chunks.
2024-08-18T19:54:55.383-05:00  INFO 13846 --- [spring-ai-airbnb] [oundedElastic-2] o.s.a.transformer.splitter.TextSplitter  : Splitting up document into 2 chunks.
```

Use SQL to verify the results:

```sql
select count(*) from vector_store;
```

There should be 16,249 rows in the vector table.

To see the embedding value, run this query:

```sql
select content, embedding from vector_store limit 10;
```

[Previous](4-Spring-AI.md) | [Next](..%2F..%2F..%2F..%2F..%2FREADME.md)


