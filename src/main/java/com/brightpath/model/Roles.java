package com.brightpath.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles") // Optional: Specify a custom table name if needed
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    @Column(name = "admin", nullable = false) // Define 'admin' column
    private Long admin;

    @Column(name = "active", nullable = false) // Define 'active' column
    private boolean active;

    @Column(name = "branch", nullable = false) // Define 'branch' column
    private String branch;

    @Column(name = "name", nullable = false) // Define 'name' column
    private String name;  // Add this field

    // Default constructor (required by Jakarta Persistence)
    public Roles() {}

    // Parameterized constructor
    public Roles(Long admin, boolean active, String branch, String gymName, String name) {
        this.admin = admin;
        this.active = active;
        this.branch = branch;
       
        this.name = name;
    }

    // Getters and setters for each field
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdmin() {
        return admin;
    }

    public void setAdmin(Long admin) {
        this.admin = admin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Roles{" +
                "id=" + id +
                ", admin=" + admin +
                ", active=" + active +
                ", branch='" + branch + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
