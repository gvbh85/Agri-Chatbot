package com.example.civicsdg.model;

import jakarta.persistence.*;

@Entity
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String issueType;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, length = 200)
    private String location;

    @Column(length = 100)
    private String sdg;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false, length = 150)
    private String email;

    public Issue() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIssueType() { return issueType; }
    public void setIssueType(String issueType) { this.issueType = issueType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSdg() { return sdg; }
    public void setSdg(String sdg) { this.sdg = sdg; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
