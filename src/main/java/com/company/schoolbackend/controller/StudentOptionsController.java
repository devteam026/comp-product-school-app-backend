package com.company.schoolbackend.controller;

import com.company.schoolbackend.entity.Hostel;
import com.company.schoolbackend.entity.HostelRoom;
import com.company.schoolbackend.entity.TransportRoute;
import com.company.schoolbackend.entity.TransportStoppage;
import com.company.schoolbackend.entity.TransportVehicle;
import com.company.schoolbackend.repository.HostelRepository;
import com.company.schoolbackend.repository.HostelRoomRepository;
import com.company.schoolbackend.repository.TransportRouteRepository;
import com.company.schoolbackend.repository.TransportStoppageRepository;
import com.company.schoolbackend.repository.TransportVehicleRepository;
import java.util.List;
import java.util.Collections;
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
    private final TransportStoppageRepository transportStoppageRepository;
    private final HostelRepository hostelRepository;
    private final HostelRoomRepository hostelRoomRepository;

    public StudentOptionsController(
            TransportRouteRepository transportRouteRepository,
            TransportVehicleRepository transportVehicleRepository,
            TransportStoppageRepository transportStoppageRepository,
            HostelRepository hostelRepository,
            HostelRoomRepository hostelRoomRepository
    ) {
        this.transportRouteRepository = transportRouteRepository;
        this.transportVehicleRepository = transportVehicleRepository;
        this.transportStoppageRepository = transportStoppageRepository;
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
        List<TransportVehicle> vehicles = transportVehicleRepository.findByActiveTrueOrderByVehicleNoAsc();
        return vehicles.stream()
                .map(TransportVehicle::getVehicleNo)
                .collect(Collectors.toList());
    }

    @GetMapping("/transport/stoppages")
    public List<String> transportStoppages(@RequestParam(value = "route", required = false) String route) {
        List<TransportStoppage> stoppages;
        if (route == null || route.isBlank()) {
            stoppages = transportStoppageRepository.findByActiveTrueOrderByStopNameAsc();
        } else {
            stoppages = transportStoppageRepository.findByRouteNameAndActiveTrueOrderByStopNameAsc(route);
        }
        return stoppages.stream()
                .map(TransportStoppage::getStopName)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    @GetMapping("/hostels")
    public List<String> hostels() {
        List<String> names = hostelRepository.findAllByOrderByNameAsc().stream()
                .map(Hostel::getName)
                .collect(Collectors.toList());
        if (!names.isEmpty()) {
            return names;
        }
        return hostelRoomRepository.findAllByOrderByRoomNumberAsc().stream()
                .map(HostelRoom::getHostelId)
                .distinct()
                .map(id -> hostelRepository.findById(id).map(Hostel::getName).orElse(""))
                .filter(name -> name != null && !name.isBlank())
                .collect(Collectors.toList());
    }

    @GetMapping("/hostels/rooms")
    public List<String> hostelRooms(
            @RequestParam(value = "hostel", required = false) String hostel,
            @RequestParam(value = "onlyAvailable", required = false, defaultValue = "false") boolean onlyAvailable
    ) {
        List<HostelRoom> rooms;
        if (hostel == null || hostel.isBlank()) {
            rooms = hostelRoomRepository.findAllByOrderByRoomNumberAsc();
        } else {
            Hostel found = hostelRepository.findFirstByNameIgnoreCase(hostel.trim());
            rooms = found == null
                    ? java.util.Collections.emptyList()
                    : hostelRoomRepository.findByHostelIdOrderByRoomNumberAsc(found.getId());
        }
        return rooms.stream()
                .filter(room -> {
                    if (!onlyAvailable) return true;
                    String status = room.getStatus();
                    if (status != null && status.equalsIgnoreCase("FULL")) return false;
                    Integer capacity = room.getCapacity();
                    Integer occupied = room.getCurrentOccupancy();
                    if (capacity != null && capacity > 0 && occupied != null && occupied >= capacity) {
                        return false;
                    }
                    return true;
                })
                .map(HostelRoom::getRoomNumber)
                .collect(Collectors.toList());
    }
}
