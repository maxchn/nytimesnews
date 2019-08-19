package com.maxchn.nytimesnewsapp.repository;

import java.util.List;

public interface Repository<T> {
    List<T> getAll();

    boolean create(T item);

    boolean delete(Integer id);

    List<T> find(String selection, String[] selectionArgs);
}