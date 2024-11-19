
package lsit.Models;

public class Supplier {
    private Long id;
    private String name;
    private String contact;

    public Supplier() {}

    public Supplier(Long id, String name, String contact) {
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
