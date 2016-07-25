package br.uel.easymenu.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.uel.easymenu.tables.DbHelper;

public abstract class SqliteDao<T> implements Dao<T> {

    protected SQLiteDatabase database;

    private String tableName;

    public SqliteDao(Context context, String tableName) {
        DbHelper helper = DbHelper.getInstance(context);
        database = helper.getWritableDatabase();
        this.tableName = tableName;
    }

    @Override
    public long insert(T object) {
        ContentValues values = new ContentValues();
        populateValues(values, object);
        return database.insert(tableName, null, values);
    }

    @Override
    public Collection<Long> insert(Collection<T> objects) {
        List<Long> idsList = new ArrayList<Long>();

        for (T object : objects) {
            long id = this.insert(object);
            idsList.add(id);
        }

        return idsList;
    }

    @Override
    public T findById(long id) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName + " WHERE _id = ?", new String[]{id + ""});
        cursor.moveToFirst();
        return (isCursorNotEmpty(cursor)) ? buildObject(cursor) : null;
    }

    @Override
    public void delete(long id) {
        String selection = "_id = ?";
        String[] args = {String.valueOf(id)};
        database.delete(tableName, selection, args);
    }

    @Override
    public List<T> fetchAll() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName, null);

        return fetchObjectsFromCursor(cursor);
    }

    protected List<T> fetchObjectsFromCursor(Cursor cursor) {
        List<T> objects = new ArrayList<T>();
        while (cursor.moveToNext()) {
            objects.add(buildObject(cursor));
        }
        return objects;
    }

    @Override
    public int count() {
        Cursor cursor = database.rawQuery("SELECT COUNT(*) AS COUNT_TABLE FROM " + tableName, null);
        cursor.moveToFirst();
        return getIntFromColumn("COUNT_TABLE", cursor);
    }

    private boolean isCursorNotEmpty(Cursor cursor) {
        return (cursor != null && cursor.getCount() > 0);
    }

    protected abstract void populateValues(ContentValues values, T object);

    protected abstract T buildObject(Cursor cursor);

    protected long getLongFromColumn(String column, Cursor cursor) {
        int index = cursor.getColumnIndex(column);
        return cursor.getLong(index);
    }

    protected int getIntFromColumn(String column, Cursor cursor) {
        int index = cursor.getColumnIndex(column);
        return cursor.getInt(index);
    }

    protected String getStringFromColumn(String column, Cursor cursor) {
        int index = cursor.getColumnIndex(column);
        return cursor.getString(index);
    }

    @Override
    public void beginTransaction() {
        database.beginTransaction();
    }

    @Override
    public void setTransactionSuccess() {
        database.setTransactionSuccessful();
    }

    @Override
    public void endTransaction() {
        database.endTransaction();
    }
}
