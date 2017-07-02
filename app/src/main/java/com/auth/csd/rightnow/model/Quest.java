package com.auth.csd.rightnow.model;

import java.util.Date;
import java.util.Random;

/**
 * Created by nikos on 1/7/2017.
 */

public class Quest {
    private long id;
    private long creator_id;
    private String name;
    private String question;
    private String answer;
    private double[] location;
    private Date date_created;
    private Date date_expires;
    private Date date_taken;
    private boolean completed;

    private static Random rand = new Random(System.currentTimeMillis());

    public static Quest make(String name) {
        Quest quest = new Quest();
        quest.setName(name);
        quest.setDate_taken(new Date());

        quest.setCompleted(rand.nextBoolean());

        return quest;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(long creator_id) {
        this.creator_id = creator_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getDate_expires() {
        return date_expires;
    }

    public void setDate_expires(Date date_expires) {
        this.date_expires = date_expires;
    }

    public Date getDate_taken() {
        return date_taken;
    }

    public void setDate_taken(Date date_taken) {
        this.date_taken = date_taken;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
