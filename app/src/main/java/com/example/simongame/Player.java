package com.example.simongame;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Player implements Comparable<Player>{
    String score;
    String name;
    String date;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player(String score, String name, String date){ ;
        this.name = name;
        this.score = score;
        this.date = date;
    }

    public Player() {
        // TODO Auto-generated constructor stub
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Player o) {
        if (getScore() == null || o.getScore() == null) {
            return 0;
        }
        return getScore().compareTo(o.getScore());

    }
}
