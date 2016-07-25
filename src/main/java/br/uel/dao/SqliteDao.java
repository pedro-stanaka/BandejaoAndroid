package br.uel.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.uel.DbHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class SqliteDao<T> implements Dao<T> {

    private SQLiteDatabase database;

    public SqliteDao(Context context) {
        DbHelper helper = new DbHelper(context);
        database = helper.getWritableDatabase();
    }

    @Override
    public void create(T object) {
        ContentValues values = new ContentValues();
        populateValues(values, object);
        database.insert(getTableName(), null, values);
    }

    @Override
    public T read(long id) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + getTableName() + " WHERE _id = ?", new String[]{id + ""});
        return (isCursorNotEmpty(cursor)) ? buildObject(cursor) : null;
    }

    @Override
    public void delete(long id) {
        String selection = "_id = ?";
        String[] args = {String.valueOf(id)};
        database.delete(getTableName(), selection, args);
    }

    @Override
    public List<T> list() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + getTableName(), null);
        List<T> objects = new ArrayList<T>();
        while(cursor.moveToNext()) {
           objects.add(buildObject(cursor));
        }
        return objects;
    }

    private boolean isCursorNotEmpty(Cursor cursor) {
        return (cursor!=null && cursor.getCount()>0);
    }

    protected abstract void populateValues(ContentValues values, T object);

    protected abstract String getTableName();

    protected abstract T buildObject(Cursor cursor);
}
