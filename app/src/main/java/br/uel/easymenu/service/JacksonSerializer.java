package br.uel.easymenu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.List;

public class JacksonSerializer implements Serializer{

    private ObjectMapper mapper;

    public JacksonSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public <T> List<T> deserialize(String json, Class<T> clazz) {
        List<T> objects = null;

        try {
            CollectionType type = mapper.getTypeFactory().
                    constructCollectionType(List.class, clazz);
            objects = mapper.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objects;
    }
}
