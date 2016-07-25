package br.uel.dao;

import java.util.Collection;
import java.util.List;

public interface Dao<T> {

    public void insert(T object);
    public void insert(Collection<T> objects);

    public T findById(long id);

    public void delete(long id);

    public List<T> fetchAll();

}
