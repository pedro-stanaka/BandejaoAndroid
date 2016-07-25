package br.uel.easymenu.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

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

    @Override
    public University findByName(String name) {
        String sql = "SELECT * FROM " + UniversityTable.NAME +
                " WHERE " + UniversityTable.UNIVERSITY_NAME + " = ?";

        String[] params = new String[]{name};

        Cursor cursor = database.rawQuery(sql, params);
        List<University> universities = fetchObjectsFromCursor(cursor);
        return (universities.size() > 0) ? universities.get(0) : null;
    }
}
