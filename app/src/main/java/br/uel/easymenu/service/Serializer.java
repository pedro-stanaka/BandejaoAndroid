package br.uel.easymenu.service;

import java.util.List;

public interface Serializer {
    <T> List<T> deserialize(String json, Class<T> clazz);
}
