package com.list_project.lsit.Models;

public class WarehouseManager {
    private Long id;
    private String name;
    private String contact; // Define contact as a String

    public WarehouseManager() {}

    public WarehouseManager(Long id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
