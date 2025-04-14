package com.student.model;

public class Resource {
    private int id;
    private String name;
    private String type;
    private String owner;

    public Resource(String name, String type, String owner) {
        this.name = name;
        this.type = type;
        this.owner = owner;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}