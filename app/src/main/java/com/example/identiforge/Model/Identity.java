package com.example.identiforge.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Identity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int levelUp;
    private int level;
    private int points;

    public Identity(String title, String description, int levelUp) {
        this.title = title;
        this.description = description;
        this.levelUp = levelUp;
        this.level = 0;
        this.points = 0;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLevelUp() {
        return levelUp;
    }

    public void setLevelUp(int levelUp) {
        this.levelUp = levelUp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean canLevelUp() {
        return points >= levelUp;
    }
}
