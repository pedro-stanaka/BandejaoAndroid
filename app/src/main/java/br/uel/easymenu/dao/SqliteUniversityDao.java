package br.uel.easymenu.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import br.uel.easymenu.model.Meal;
import br.uel.easymenu.model.University;
import br.uel.easymenu.tables.UniversityTable;

public class SqliteUniversityDao extends SqliteDao<University> implements UniversityDao {

    private Context context;

    public SqliteUniversityDao(Context context) {
        super(context, UniversityTable.NAME);
        this.context = context;
    }

    @Override
    public long insert(University university) {
        long id = super.insert(university);
        university.setId(id);

        SqliteMealDao mealDao = new SqliteMealDao(context);
        for(Meal meal : university.getMeals()) {
            meal.setUniversity(university);
            long mealId = mealDao.insert(meal);
            meal.setId(mealId);
        }

        return id;
    }

    @Override
    protected void populateValues(ContentValues values, University object) {
        if(object.getId() != 0)
            values.put(UniversityTable.ID_UNIVERSITY, object.getId());

        values.put(UniversityTable.FULL_NAME, object.getFullName());
        values.put(UniversityTable.UNIVERSITY_NAME, object.getName());
    }

    @Override
    protected University buildObject(Cursor cursor) {
        University university = new University();
        university.setId(getLongFromColumn(UniversityTable.ID_UNIVERSITY, cursor));
        university.setFullName(getStringFromColumn(UniversityTable.FULL_NAME, cursor));
        university.setName(getStringFromColumn(UniversityTable.UNIVERSITY_NAME, cursor));
        return university;
    }
}
