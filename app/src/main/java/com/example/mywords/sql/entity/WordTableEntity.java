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

package com.example.mywords.sql.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "wordtable")
public class WordTableEntity {
    /**
     * CREATE TABLE wordtable (
     *     id                 INTEGER PRIMARY KEY AUTOINCREMENT,
     *     etymology          TEXT,
     *     example            TEXT,
     *     exampletranslation TEXT,
     *     [explain]          TEXT,
     *     iscollect          INTEGER,
     *     islearned          INTEGER,
     *     issimple           INTEGER,
     *     level              TEXT,
     *     meaning            TEXT,
     *     rootvariant        TEXT,
     *     soundmark          TEXT,
     *     structure          TEXT,
     *     translation        TEXT,
     *     type               TEXT,
     *     type1              TEXT,
     *     type2              TEXT,
     *     type3              TEXT,
     *     type4              TEXT,
     *     word               TEXT,
     *     wordid             TEXT
     * );
     */
    @PrimaryKey
    private int id;
    private String etymology;
    private String example;
    private String exampletranslation;
    private String explain;
    private int iscollect;
    private int islearned;
    private int issimple;
    private String level;
    private String meaning;
    private String rootvariant;
    private String soundmark;
    private String structure;
    private String translation;
    private String type;
    private String type1;
    private String type2;
    private String type3;
    private String type4;
    private String word;
    private String wordid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEtymology () {
        return etymology;
    }

    public void setEtymology (String etymology) {
        this.etymology = etymology;
    }

    public String getExample () {
        return example;
    }

    public void setExample (String example) {
        this.example = example;
    }

    public String getExampletranslation () {
        return exampletranslation;
    }

    public void setExampletranslation (String exampletranslation) {
        this.exampletranslation = exampletranslation;
    }

    public String getExplain () {
        return explain;
    }

    public void setExplain (String explain) {
        this.explain = explain;
    }

    public int getIscollect () {
        return iscollect;
    }

    public void setIscollect (int iscollect) {
        this.iscollect = iscollect;
    }

    public int getIslearned () {
        return islearned;
    }

    public void setIslearned (int islearned) {
        this.islearned = islearned;
    }

    public int getIssimple () {
        return issimple;
    }

    public void setIssimple (int issimple) {
        this.issimple = issimple;
    }

    public String getLevel () {
        return level;
    }

    public void setLevel (String level) {
        this.level = level;
    }

    public String getMeaning () {
        return meaning;
    }

    public void setMeaning (String meaning) {
        this.meaning = meaning;
    }

    public String getRootvariant () {
        return rootvariant;
    }

    public void setRootvariant (String rootvariant) {
        this.rootvariant = rootvariant;
    }

    public String getSoundmark () {
        return soundmark;
    }

    public void setSoundmark (String soundmark) {
        this.soundmark = soundmark;
    }

    public String getStructure () {
        return structure;
    }

    public void setStructure (String structure) {
        this.structure = structure;
    }

    public String getTranslation () {
        return translation;
    }

    public void setTranslation (String translation) {
        this.translation = translation;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getType1 () {
        return type1;
    }

    public void setType1 (String type1) {
        this.type1 = type1;
    }

    public String getType2 () {
        return type2;
    }

    public void setType2 (String type2) {
        this.type2 = type2;
    }

    public String getType3 () {
        return type3;
    }

    public void setType3 (String type3) {
        this.type3 = type3;
    }

    public String getType4 () {
        return type4;
    }

    public void setType4 (String type4) {
        this.type4 = type4;
    }

    public String getWord () {
        return word;
    }

    public void setWord (String word) {
        this.word = word;
    }

    public String getWordid () {
        return wordid;
    }

    public void setWordid (String wordid) {
        this.wordid = wordid;
    }

    public WordTableEntity () {
    }

    @Ignore
    public WordTableEntity (int id, String etymology, String example, String exampletranslation, String explain, int iscollect,
                            int islearned, int issimple, String level, String meaning, String rootvariant, String soundmark,
                            String structure, String translation, String type, String type1, String type2, String type3,
                            String type4, String word, String wordid) {
        this.id = id;
        this.etymology = etymology;
        this.example = example;
        this.exampletranslation = exampletranslation;
        this.explain = explain;
        this.iscollect = iscollect;
        this.islearned = islearned;
        this.issimple = issimple;
        this.level = level;
        this.meaning = meaning;
        this.rootvariant = rootvariant;
        this.soundmark = soundmark;
        this.structure = structure;
        this.translation = translation;
        this.type = type;
        this.type1 = type1;
        this.type2 = type2;
        this.type3 = type3;
        this.type4 = type4;
        this.word = word;
        this.wordid = wordid;
    }

    @Override
    public String toString () {
        return "WordTableEntity{" +
                "id=" + id +
                ", etymology='" + etymology + '\'' +
                ", example='" + example + '\'' +
                ", exampletranslation='" + exampletranslation + '\'' +
                ", explain='" + explain + '\'' +
                ", iscollect=" + iscollect +
                ", islearned=" + islearned +
                ", issimple=" + issimple +
                ", level='" + level + '\'' +
                ", meaning='" + meaning + '\'' +
                ", rootvariant='" + rootvariant + '\'' +
                ", soundmark='" + soundmark + '\'' +
                ", structure='" + structure + '\'' +
                ", translation='" + translation + '\'' +
                ", type='" + type + '\'' +
                ", type1='" + type1 + '\'' +
                ", type2='" + type2 + '\'' +
                ", type3='" + type3 + '\'' +
                ", type4='" + type4 + '\'' +
                ", word='" + word + '\'' +
                ", wordid='" + wordid + '\'' +
                '}';
    }
}