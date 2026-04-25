package com.company.schoolbackend.dto;

public class HolidayDto {
    private Long id;
    private String date;
    private String name;

    public HolidayDto() {}

    public HolidayDto(Long id, String date, String name) {
        this.id = id;
        this.date = date;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
