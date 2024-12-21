package com.list_project.lsit.Models;

public class DeliveryDriver {
    private Long id;
    private String name;

    public DeliveryDriver() {}

    public DeliveryDriver(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(long currentId) {
        this.id = currentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
