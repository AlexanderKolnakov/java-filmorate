package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data

public class Mpa {
    private int id;
    private final String name;

    public Mpa(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Map<String,Object> toMap() {

        Map<String, Object> values = new HashMap<>();
        values.put("mpa_id", id);
        values.put("rating", name);

        return values;
    }
}
