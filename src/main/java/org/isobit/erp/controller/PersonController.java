package org.isobit.erp.controller;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.isobit.erp.model.Person;
import org.isobit.erp.service.PersonService;

public class PersonController {

    private final PersonService service;
    
    public PersonController(PersonService service) {
        this.service = service;
    }

    public Router mount(Router router) {
        router.route().handler(BodyHandler.create());
        router.post().handler(this::create);
        router.get().handler(this::findAll);
        router.get("/:id").handler(this::findById);
        router.put("/:id").handler(this::update);
        router.delete("/:id").handler(this::delete);
        return router;
    }

    private void create(RoutingContext ctx) {
        try {
            JsonObject body = ctx.body().asJsonObject();
            Person person = Person.fromJson(body);

            service.create(person)
                    .onSuccess(saved -> ctx.response()
                            .setStatusCode(201)
                            .putHeader("content-type", "application/json")
                            .end(saved.toJson().encode()))
                    .onFailure(error -> handleError(ctx, error));
        } catch (Exception e) {
            handleError(ctx, e);
        }
    }

    private void findAll(RoutingContext ctx) {
        service.getAll()
                .onSuccess(personas -> {
                    JsonArray array = new JsonArray();
                    personas.forEach(person -> array.add(person.toJson()));

                    ctx.response()
                            .putHeader("content-type", "application/json")
                            .end(array.encode());
                })
                .onFailure(error -> handleError(ctx, error));
    }

    private void findById(RoutingContext ctx) {
        String id = ctx.pathParam("id");

        service.getById(id)
                .onSuccess(person -> {
                    if (person == null) {
                        ctx.response()
                                .setStatusCode(404)
                                .putHeader("content-type", "application/json")
                                .end(new JsonObject().put("message", "Person no encontrada").encode());
                        return;
                    }

                    ctx.response()
                            .putHeader("content-type", "application/json")
                            .end(person.toJson().encode());
                })
                .onFailure(error -> handleError(ctx, error));
    }

    private void update(RoutingContext ctx) {
        try {
            String id = ctx.pathParam("id");
            JsonObject body = ctx.body().asJsonObject();
            Person person = Person.fromJson(body);

            service.update(id, person)
                    .onSuccess(updated -> {
                        if (!updated) {
                            ctx.response()
                                    .setStatusCode(404)
                                    .putHeader("content-type", "application/json")
                                    .end(new JsonObject().put("message", "Person no encontrada").encode());
                            return;
                        }

                        ctx.response()
                                .putHeader("content-type", "application/json")
                                .end(new JsonObject().put("message", "Person actualizada").encode());
                    })
                    .onFailure(error -> handleError(ctx, error));
        } catch (Exception e) {
            handleError(ctx, e);
        }
    }

    private void delete(RoutingContext ctx) {
        String id = ctx.pathParam("id");

        service.delete(id)
                .onSuccess(deleted -> {
                    if (!deleted) {
                        ctx.response()
                                .setStatusCode(404)
                                .putHeader("content-type", "application/json")
                                .end(new JsonObject().put("message", "Person no encontrada").encode());
                        return;
                    }

                    ctx.response()
                            .putHeader("content-type", "application/json")
                            .end(new JsonObject().put("message", "Person eliminada").encode());
                })
                .onFailure(error -> handleError(ctx, error));
    }

    private void handleError(RoutingContext ctx, Throwable error) {
        int status = error instanceof IllegalArgumentException ? 400 : 500;

        ctx.response()
                .setStatusCode(status)
                .putHeader("content-type", "application/json")
                .end(new JsonObject()
                        .put("error", error.getMessage() == null ? "Error interno" : error.getMessage())
                        .encode());
    }
}
