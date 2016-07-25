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

    public University getUniversity() {


        return null;
    }
}
