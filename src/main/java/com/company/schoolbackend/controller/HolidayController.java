package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.HolidayDto;
import com.company.schoolbackend.dto.HolidayRequest;
import com.company.schoolbackend.service.HolidayService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public List<HolidayDto> list() {
        return holidayService.list();
    }

    @PostMapping
    public HolidayDto add(@RequestBody HolidayRequest request) {
        return holidayService.add(request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        holidayService.delete(id);
    }
}
