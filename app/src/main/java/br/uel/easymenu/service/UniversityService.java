package br.uel.easymenu.service;

import javax.inject.Inject;

import br.uel.easymenu.dao.UniversityDao;
import br.uel.easymenu.model.University;

public class UniversityService {

    @Inject
    UniversityDao universityDao;

    public UniversityService(UniversityDao universityDao) {
        this.universityDao = universityDao;
    }

    public University selectUniversity() {
        if(universityDao.count() == 0) {
            return null;
        }

        // Pega dos SharedPreferences
        // Se não tiver, pega o primeiro que encontrar

        return null;
    }
}
