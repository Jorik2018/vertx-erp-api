package org.isobit.erp;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.isobit.erp.AppModule;
import org.isobit.erp.repository.PersonRepository;
import org.isobit.erp.controller.PersonController;
import org.isobit.erp.service.PersonService;

public class MainVerticle extends VerticleBase {

  @Override
  public Future<?> start() {

    int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

    Injector injector = Guice.createInjector(new AppModule(vertx));

    Router mainRouter = Router.router(vertx);

    mainRouter.get("/").handler(ctx -> ctx.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x 2.0!"));

    mainRouter.route("/api/person/*").subRouter(injector.getInstance(PersonController.class).mount(Router.router(vertx)));

    return vertx.createHttpServer()
        .requestHandler(mainRouter)
        .listen(port, "0.0.0.0")
        .onSuccess(server -> System.out.println("HTTP server started on port " + port))
        .mapEmpty();

  }
}
