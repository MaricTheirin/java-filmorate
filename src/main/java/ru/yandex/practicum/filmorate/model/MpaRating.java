package ru.yandex.practicum.filmorate.model;

import lombok.Builder;

@Builder
public class MpaRating {
    private int id;
    private String name;

    public MpaRating(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
