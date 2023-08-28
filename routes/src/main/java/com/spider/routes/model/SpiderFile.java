package com.spider.routes.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "spider_files")
public class SpiderFile {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "problem_filename", nullable = false)
    private String problemFilename;

    @Column(name = "result_filename")
    private String solutionFilename;

    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private User author;


    public SpiderFile() {
    }

    public SpiderFile(String originalFilename, String problemFilename, String solutionFilename, User author) {
        this.originalFilename = originalFilename;
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

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
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
}
