package com.example.openshelves;

public class BookCategoryLV{
    int image;
    Integer year;
    String title, author, quote;

    public BookCategoryLV(int image, String title, String author,Integer year, String quote) {
        this.image = image;
        this.title = title;
        this.author = author;
        this.year=year;
        this.quote = quote;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getQuote() {
        return quote;
    }

    public Integer getYear() {
        return year;
    }

}
