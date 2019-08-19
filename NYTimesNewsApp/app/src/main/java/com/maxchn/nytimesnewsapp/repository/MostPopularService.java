package com.maxchn.nytimesnewsapp.repository;

import com.maxchn.nytimesnewsapp.model.MostPopularType;

public interface MostPopularService {
    void getAll(MostPopularType type);
}