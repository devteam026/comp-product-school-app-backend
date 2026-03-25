package com.company.schoolbackend.controller;

import com.company.schoolbackend.entity.Hostel;
import com.company.schoolbackend.entity.HostelRoom;
import com.company.schoolbackend.entity.TransportRoute;
import com.company.schoolbackend.entity.TransportVehicle;
import com.company.schoolbackend.repository.HostelRepository;
import com.company.schoolbackend.repository.HostelRoomRepository;
import com.company.schoolbackend.repository.TransportRouteRepository;
import com.company.schoolbackend.repository.TransportVehicleRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student-options")
public class StudentOptionsController {
    private final TransportRouteRepository transportRouteRepository;
    private final TransportVehicleRepository transportVehicleRepository;
    private final HostelRepository hostelRepository;
    private final HostelRoomRepository hostelRoomRepository;

    public StudentOptionsController(
            TransportRouteRepository transportRouteRepository,
            TransportVehicleRepository transportVehicleRepository,
            HostelRepository hostelRepository,
            HostelRoomRepository hostelRoomRepository
    ) {
        this.transportRouteRepository = transportRouteRepository;
        this.transportVehicleRepository = transportVehicleRepository;
        this.hostelRepository = hostelRepository;
        this.hostelRoomRepository = hostelRoomRepository;
    }

    @GetMapping("/transport/routes")
    public List<String> transportRoutes() {
        return transportRouteRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(TransportRoute::getName)
                .collect(Collectors.toList());
    }

    @GetMapping("/transport/vehicles")
    public List<String> transportVehicles(@RequestParam(value = "route", required = false) String route) {
        List<TransportVehicle> vehicles = route == null || route.isBlank()
                ? transportVehicleRepository.findByActiveTrueOrderByVehicleNoAsc()
                : transportVehicleRepository.findByActiveTrueAndRouteNameOrderByVehicleNoAsc(route.trim());
        return vehicles.stream()
                .map(TransportVehicle::getVehicleNo)
                .collect(Collectors.toList());
    }

    @GetMapping("/hostels")
    public List<String> hostels() {
        return hostelRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(Hostel::getName)
                .collect(Collectors.toList());
    }

    @GetMapping("/hostels/rooms")
    public List<String> hostelRooms(@RequestParam(value = "hostel", required = false) String hostel) {
        List<HostelRoom> rooms = hostel == null || hostel.isBlank()
                ? hostelRoomRepository.findByActiveTrueOrderByRoomNoAsc()
                : hostelRoomRepository.findByActiveTrueAndHostelNameOrderByRoomNoAsc(hostel.trim());
        return rooms.stream()
                .map(HostelRoom::getRoomNo)
                .collect(Collectors.toList());
    }
}
