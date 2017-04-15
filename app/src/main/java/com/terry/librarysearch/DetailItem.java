package com.terry.librarysearch;

public class DetailItem {
    private String bookCode;
    private String location;
    private String chunggu;
    private String status;

    public DetailItem(String bookCode, String location, String chunggu, String status) {
        this.bookCode = bookCode;
        this.location = location;
        this.chunggu = chunggu;
        this.status = status;
    }

    public String getBookCode() {
        return bookCode;
    }

    public String getLocation() {
        return location;
    }

    public String getChunggu() {
        return chunggu;
    }

    public String getStatus() {
        return status;
    }
}
