package com.example.demo;

public class Greeting {

    private final long id;
    private final String content;
    private final int annee;
    private final String lastname;


    public Greeting(long id, String content, int annee, String lastname) {
        this.id = id;
        this.content = content;
        this.annee = annee * 2;
        this.lastname = lastname;
    }

    public int getAnnee() {
        return annee;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getLastname() { return lastname; }
}
