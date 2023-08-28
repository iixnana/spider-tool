package com.spider.routes.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "spider_files")
public class SpiderFile {
    @Id
    private UUID id;

    @Column(name = "problem_filename", nullable = false)
    private String problemFilename;

    @Column(name = "result_filename")
    private String solutionFilename;

    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @CreationTimestamp
    private Instant createdOn;


    public SpiderFile() {
    }

    public SpiderFile(String originalFilename, String problemFilename, String solutionFilename, User author) {
        this.problemFilename = problemFilename;
        this.solutionFilename = solutionFilename;
        this.author = author;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProblemFilename() {
        return problemFilename;
    }

    public void setProblemFilename(String problemFilename) {
        this.problemFilename = problemFilename;
    }

    public String getSolutionFilename() {
        return solutionFilename;
    }

    public void setSolutionFilename(String resultFilename) {
        this.solutionFilename = resultFilename;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }
}
