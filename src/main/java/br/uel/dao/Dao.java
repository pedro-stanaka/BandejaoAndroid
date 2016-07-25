package br.uel.dao;

import java.util.List;

public interface Dao<T> {

    public void create(T object);

    public T read(long id);

    public void delete(long id);

    public List<T> list();
}
