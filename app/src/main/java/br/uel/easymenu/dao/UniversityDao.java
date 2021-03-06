package br.uel.easymenu.dao;

import java.util.List;

import br.uel.easymenu.model.University;

public interface UniversityDao extends Dao<University> {

    public University findByName(String name);

    public List<University> orderByName();

    public long insertWithMeals(University university);

    public List<University> notInNamesList(List<String> universityNames);

    public List<University> inNamesList(List<String> universityNames);

}
