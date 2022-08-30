package com.example.openshelves;

import java.time.LocalDateTime;
import java.util.Date;

public class BookFB {
    private String title;
    private String author;
    private String publisher;
    private String time;
    private String genre;
    private String status;
    private String coverLink;
    private String description;
    private Integer totalNoPages;
    private Integer currentNoPages;
    private String dateFinished;
    private String dateStarted;
    private Integer noStars;
    private String id;

    public BookFB(String title, String author, String publisher, String time, String genre, String status, String coverLink, String description, Integer totalNoPages, Integer currentNoPages, String dateFinished, String dateStarted, Integer noStars, String id) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.time = time;
        this.genre = genre;
        this.status = status;
        this.coverLink = coverLink;
        this.description = description;
        this.totalNoPages = totalNoPages;
        this.currentNoPages = currentNoPages;
        this.dateFinished = dateFinished;
        this.dateStarted = dateStarted;
        this.noStars = noStars;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoverLink() {
        return coverLink;
    }

    public void setCoverLink(String coverLink) {
        this.coverLink = coverLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalNoPages() {
        return totalNoPages;
    }

    public void setTotalNoPages(Integer totalNoPages) {
        this.totalNoPages = totalNoPages;
    }

    public Integer getCurrentNoPages() {
        return currentNoPages;
    }

    public void setCurrentNoPages(Integer currentNoPages) {
        this.currentNoPages = currentNoPages;
    }

    public String getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(String dateFinished) {
        this.dateFinished = dateFinished;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public Integer getNoStars() {
        return noStars;
    }

    public void setNoStars(Integer noStars) {
        this.noStars = noStars;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BookFB{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", time='" + time + '\'' +
                ", genre='" + genre + '\'' +
                ", status='" + status + '\'' +
                ", coverLink='" + coverLink + '\'' +
                ", description='" + description + '\'' +
                ", totalNoPages=" + totalNoPages +
                ", currentNoPages=" + currentNoPages +
                ", dateFinished='" + dateFinished + '\'' +
                ", dateStarted='" + dateStarted + '\'' +
                ", noStars=" + noStars +
                ", id='" + id + '\'' +
                '}';
    }
}
