package org.isobit.erp.repository;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.isobit.erp.model.Person;

import java.util.List;
import java.util.stream.Collectors;

public class PersonRepository {

    private static final String COLLECTION = "persons";
    
    private final MongoClient mongoClient;

    public PersonRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public Future<Person> save(Person person) {
        JsonObject document = new JsonObject()
                .put("name", person.getName())
                .put("address", person.getAddress())
                .put("weight", person.getWeight())
                .put("dateOfDate", person.getDateOfDate())
                .put("code", person.getCode());

        return mongoClient.save(COLLECTION, document)
                .map(id -> {
                    person.setId(id);
                    return person;
                });
    }

    public Future<List<Person>> findAll() {
        return mongoClient.find(COLLECTION, new JsonObject())
                .map(documents -> documents.stream().map(doc -> {
                    Person person = new Person();
                    person.setId(doc.getString("_id"));
                    person.setName(doc.getString("name"));
                    person.setAddress(doc.getString("address"));
                    person.setWeight(doc.getDouble("weight"));
                    person.setDateOfDate(doc.getString("dateOfDate"));
                    person.setCode(doc.getString("code"));
                    return person;
                }).collect(Collectors.toList()));
    }

    public Future<Person> findById(String id) {
        return mongoClient.findOne(COLLECTION, new JsonObject().put("_id", id), new JsonObject())
                .map(doc -> {
                    if (doc == null) {
                        return null;
                    }

                    Person person = new Person();
                    person.setId(doc.getString("_id"));
                    person.setName(doc.getString("name"));
                    person.setAddress(doc.getString("address"));
                    person.setWeight(doc.getDouble("weight"));
                    person.setDateOfDate(doc.getString("dateOfDate"));
                    person.setCode(doc.getString("code"));
                    return person;
                });
    }

    public Future<Boolean> update(String id, Person person) {
        JsonObject query = new JsonObject().put("_id", id);

        JsonObject update = new JsonObject()
                .put("$set", new JsonObject()
                        .put("name", person.getName())
                        .put("address", person.getAddress())
                        .put("weight", person.getWeight())
                        .put("dateOfDate", person.getDateOfDate())
                        .put("code", person.getCode()));

        return mongoClient.updateCollection(COLLECTION, query, update)
                .map(result -> result.getDocModified() > 0);
    }

    public Future<Boolean> delete(String id) {
        return mongoClient.removeDocument(COLLECTION, new JsonObject().put("_id", id))
                .map(result -> result.getRemovedCount() > 0);
    }
}
