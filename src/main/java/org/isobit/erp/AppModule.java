package org.isobit.erp;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.isobit.erp.repository.PersonRepository;
import org.isobit.erp.service.PersonService;

public class AppModule extends AbstractModule {

    private final Vertx vertx;

    public AppModule(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    protected void configure() {
        bind(Vertx.class).toInstance(vertx);
    }

    @Provides
    @Singleton
    MongoClient provideMongoClient(Vertx vertx) {
        JsonObject mongoConfig = new JsonObject()
                .put("connection_string", System.getenv().getOrDefault(
                        "MONGO_URL",
                        "mongodb://localhost:27017"
                ))
                .put("db_name", System.getenv().getOrDefault(
                        "MONGO_DB",
                        "personadb"
                ));

        return MongoClient.createShared(vertx, mongoConfig);
    }

    @Provides
    @Singleton
    PersonRepository providePersonRepository(MongoClient mongoClient) {
        return new PersonRepository(mongoClient);
    }

    @Provides
    @Singleton
    PersonService providePersonService(PersonRepository repository) {
        return new PersonService(repository);
    }
}