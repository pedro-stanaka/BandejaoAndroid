package br.uel.easymenu;

import java.util.ArrayList;
import java.util.List;

import br.uel.easymenu.model.Meal;
import br.uel.easymenu.model.University;

public class UniversityBuilder {

    String name = "Name";
    String fullName = "Full Name";
    List<Meal> meals = new ArrayList<>();

    public UniversityBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UniversityBuilder withFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public UniversityBuilder withMeals(List<Meal> meals) {
        this.meals = meals;
        return this;
    }

    public University build() {
        University university = new University();
        university.setName(name);
        university.setFullName(fullName);
        university.setMeals(meals);
        return university;
    }
}
