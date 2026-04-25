package com.company.schoolbackend.service;

import com.company.schoolbackend.dto.HolidayDto;
import com.company.schoolbackend.dto.HolidayRequest;
import com.company.schoolbackend.entity.Holiday;
import com.company.schoolbackend.repository.HolidayRepository;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    public List<HolidayDto> list() {
        return holidayRepository.findAllByOrderByHolidayDateAsc()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public HolidayDto add(HolidayRequest request) {
        if (request.getDate() == null || request.getDate().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date is required");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Holiday name is required");
        }
        LocalDate date = LocalDate.parse(request.getDate());
        if (holidayRepository.findByHolidayDate(date).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A holiday is already declared for this date");
        }
        Holiday holiday = new Holiday();
        holiday.setHolidayDate(date);
        holiday.setName(request.getName().trim());
        holiday.setCreatedAt(OffsetDateTime.now());
        return toDto(holidayRepository.save(holiday));
    }

    public void delete(Long id) {
        if (!holidayRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Holiday not found");
        }
        holidayRepository.deleteById(id);
    }

    public Optional<Holiday> findByDate(LocalDate date) {
        return holidayRepository.findByHolidayDate(date);
    }

    private HolidayDto toDto(Holiday h) {
        return new HolidayDto(h.getId(), h.getHolidayDate().toString(), h.getName());
    }
}
