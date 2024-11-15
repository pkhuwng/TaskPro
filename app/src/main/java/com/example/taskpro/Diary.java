package com.example.taskpro;

import java.util.Calendar;

public class Diary {
    private int day; // Ngày
    private int month; // Tháng
    private int year; // Năm
    private String hour; // Giờ
    private String title; // Tiêu đề
    private String content; // Nội dung
    private String key; // Khóa từ Firebase

    // Constructor mặc định
    public Diary() {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0
        this.year = calendar.get(Calendar.YEAR);
        this.hour = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)); // Định dạng giờ
        this.title = ""; // Tiêu đề mặc định
        this.content = ""; // Nội dung mặc định
    }

    public Diary(int day, int month, int year, String hour, String title, String content) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.title = title;
        this.content = content;
    }

    // Getter cho từng thuộc tính
    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getHour() {
        return hour;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}