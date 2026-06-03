package org.isobit.erp;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;

public class MainVerticle extends VerticleBase {

  @Override
  public Future<?> start() {
    int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

    return vertx.createHttpServer()
      .requestHandler(req -> req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!"))
      .listen(port, "0.0.0.0")
      .onSuccess(server -> System.out.println("HTTP server started on port " + port))
      .mapEmpty();
  }
}
