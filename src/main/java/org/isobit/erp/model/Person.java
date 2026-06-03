package org.isobit.erp.model;

import io.vertx.core.json.JsonObject;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    private String id;
    private String name;
    private String address;
    private Double weight;
    private String dateOfDate;
    private String code;

    public static Person fromJson(JsonObject json) {
        Person person = new Person();
        person.setId(json.getString("id"));
        person.setName(json.getString("name"));
        person.setAddress(json.getString("address"));
        person.setWeight(json.getDouble("weight"));
        person.setDateOfDate(json.getString("dateOfDate"));
        person.setCode(json.getString("code"));
        return person;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        if (id != null) {
            json.put("id", id);
        }
        json.put("name", name);
        json.put("address", address);
        json.put("weight", weight);
        json.put("dateOfDate", dateOfDate);
        json.put("code", code);
        return json;
    }

}
