/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mywords.sql.dao;

import com.example.mywords.sql.entity.WordTableEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface WordTableDao {
    @Query("SELECT * FROM wordtable")
    List<WordTableEntity> loadAllWords ();

    @Query("select * from wordtable where id = :id")
    WordTableEntity loadWordById (int id);

    @Query("select * from wordtable where wordid = :wordId")
    WordTableEntity loadWordByWordId (String wordId);

    @Query("select * from wordtable where word = :word")
    WordTableEntity loadWordByWord (String word);

    //按照词根找
    @Query("select * from wordtable where rootvariant = :rootvariant")
    List<WordTableEntity> loadWordByRoot (String rootvariant);

    //按照词根找
    @Query("select * from wordtable where type3 = :type3")
    List<WordTableEntity> loadWordByType3 (String type3);

    //模糊匹配(匹配前缀)
    @Query("select * from wordtable where word like :word || '%'")
    List<WordTableEntity> loadWordsByPre (String word);
}