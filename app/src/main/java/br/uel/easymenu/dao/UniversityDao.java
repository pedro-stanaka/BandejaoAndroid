package br.uel.easymenu.dao;

import br.uel.easymenu.model.University;

public interface UniversityDao extends Dao<University> {

    public University findByName(String name);
}
