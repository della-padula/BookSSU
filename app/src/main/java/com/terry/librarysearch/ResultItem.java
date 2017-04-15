package com.terry.librarysearch;

import java.io.Serializable;

public class ResultItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String imageUrl;
    private String bookTitle;
    private String bookAuthor;
    private String bookStatus;
    private String linkUrl;

    public ResultItem(String imageUrl, String bookTitle, String bookAuthor, String bookStatus, String linkUrl) {
        this.imageUrl = imageUrl;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookStatus = bookStatus;
        this.linkUrl = linkUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

}
