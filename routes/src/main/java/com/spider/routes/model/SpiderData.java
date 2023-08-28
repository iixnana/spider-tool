package com.spider.routes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "spider_data")
public class SpiderData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_id", nullable = false)
    private UUID fileId;

    @Column(name = "problem_filename", nullable = false)
    private String problemFilename;

    @Column(name = "solution_filename")
    private String solutionFilename;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "session", referencedColumnName = "id")
    private SpiderSession session;

    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;


    public SpiderData() {
    }

    public SpiderData(UUID fileId, String problemFilename, String solutionFilename, User author, Instant createdOn, Instant lastUpdatedOn) {
        this.fileId = fileId;
        this.problemFilename = problemFilename;
        this.solutionFilename = solutionFilename;
        this.author = author;
        this.createdOn = createdOn;
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
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

    public String getCreatedOn() {
        return createdOn.toString();
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn.toString();
    }

    public void setLastUpdatedOn(Instant lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

}
