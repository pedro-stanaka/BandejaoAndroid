package br.uel.easymenu.dao;

import java.util.Collection;
import java.util.List;

public interface Dao<T> {

    public long insert(T object);

    public Collection<Long> insert(Collection<T> objects);

    public T findById(long id);

    public void delete(long id);

    public List<T> fetchAll();

    public int count();

    public void beginTransaction();

    public void setTransactionSuccess();

    public void endTransaction();
}
