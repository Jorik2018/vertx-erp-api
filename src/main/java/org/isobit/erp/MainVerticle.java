package org.isobit.erp;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import org.isobit.erp.repository.PersonRepository;
import org.isobit.erp.controller.PersonController;
import org.isobit.erp.service.PersonService;

public class MainVerticle extends VerticleBase {

  @Override
  public Future<?> start() {
    int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

    JsonObject mongoConfig = new JsonObject()
        .put("connection_string", System.getenv().getOrDefault("MONGO_URL", "mongodb://localhost:27017"))
        .put("db_name", System.getenv().getOrDefault("MONGO_DB", "personadb"));

    MongoClient mongoClient = MongoClient.createShared(vertx, mongoConfig);

    PersonRepository repository = new PersonRepository(mongoClient);

    PersonService service = new PersonService(repository);

    Router mainRouter = Router.router(vertx);

    mainRouter.route("/api/person/*").subRouter(
        new PersonController(service).mount(Router.router(vertx)));

    return vertx.createHttpServer()
        .requestHandler(mainRouter)
        .requestHandler(req -> req.response()
            .putHeader("content-type", "text/plain")
            .end("Hello from Vert.x!"))
        .listen(port, "0.0.0.0")
        .onSuccess(server -> System.out.println("HTTP server started on port " + port))
        .mapEmpty();
  }
}
