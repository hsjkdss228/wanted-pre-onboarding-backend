package com.inu.wanted.preassignment.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class TechStack {
    @Column(name = "name")
    private String name;

    private TechStack() {

    }

    public TechStack(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "TechStack: { name = '" + name + "' }";
    }
}
