package fr.but3.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private int id;

    @Column(nullable = false)
    private String name;

    public User() {}

    public User(String name) { this.name = name; }

    public int getId() { return id; }
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}