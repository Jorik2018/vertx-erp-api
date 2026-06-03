package org.isobit.erp.service;

import io.vertx.core.Future;
import org.isobit.erp.model.Person;
import org.isobit.erp.repository.PersonRepository;

import java.util.List;

public class PersonService {

    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public Future<Person> create(Person person) {
        try {
            validate(person);
            return repository.save(person);
        } catch (IllegalArgumentException e) {
            return Future.failedFuture(e);
        }
    }

    public Future<List<Person>> getAll() {
        return repository.findAll();
    }

    public Future<Person> getById(String id) {
        if (id == null || id.isBlank()) {
            return Future.failedFuture("El id es obligatorio");
        }
        return repository.findById(id);
    }

    public Future<Boolean> update(String id, Person person) {
        if (id == null || id.isBlank()) {
            return Future.failedFuture("El id es obligatorio");
        }

        try {
            validate(person);
            return repository.update(id, person);
        } catch (IllegalArgumentException e) {
            return Future.failedFuture(e);
        }
    }

    public Future<Boolean> delete(String id) {
        if (id == null || id.isBlank()) {
            return Future.failedFuture("El id es obligatorio");
        }
        return repository.delete(id);
    }

    private void validate(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("La person es obligatoria");
        }
        if (person.getName() == null || person.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (person.getAddress() == null || person.getAddress().isBlank()) {
            throw new IllegalArgumentException("La dirección es obligatoria");
        }
        if (person.getWeight() == null || person.getWeight() <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor a 0");
        }
        if (person.getDateOfDate() == null || person.getDateOfDate().isBlank()) {
            throw new IllegalArgumentException("dateOfDate es obligatorio");
        }
        if (person.getCode() == null || person.getCode().isBlank()) {
            throw new IllegalArgumentException("El código es obligatorio");
        }
    }
}
