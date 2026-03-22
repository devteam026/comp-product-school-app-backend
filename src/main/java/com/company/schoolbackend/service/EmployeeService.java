package com.company.schoolbackend.service;

import com.company.schoolbackend.dto.EmployeeResponse;
import com.company.schoolbackend.dto.EmployeeTeacherDetailsDto;
import com.company.schoolbackend.dto.EmployeeUpsertRequest;
import com.company.schoolbackend.entity.Employee;
import com.company.schoolbackend.entity.EmployeeTeacherDetails;
import com.company.schoolbackend.entity.UserRole;
import com.company.schoolbackend.repository.EmployeeRepository;
import com.company.schoolbackend.repository.EmployeeTeacherDetailsRepository;
import com.company.schoolbackend.repository.AppUserRepository;
import com.company.schoolbackend.repository.TeacherClassRepository;
import com.company.schoolbackend.entity.AppUser;
import com.company.schoolbackend.entity.TeacherClass;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeTeacherDetailsRepository teacherDetailsRepository;
    private final AppUserRepository appUserRepository;
    private final TeacherClassRepository teacherClassRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeTeacherDetailsRepository teacherDetailsRepository,
                           AppUserRepository appUserRepository, TeacherClassRepository teacherClassRepository,
                           PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.teacherDetailsRepository = teacherDetailsRepository;
        this.appUserRepository = appUserRepository;
        this.teacherClassRepository = teacherClassRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<EmployeeResponse> list() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeResponse> responses = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeTeacherDetails details = teacherDetailsRepository.findByEmployeeId(employee.getId()).orElse(null);
            responses.add(toResponse(employee, details));
        }
        return responses;
    }

    public EmployeeResponse get(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow();
        EmployeeTeacherDetails details = teacherDetailsRepository.findByEmployeeId(id).orElse(null);
        return toResponse(employee, details);
    }

    public EmployeeResponse upsert(EmployeeUpsertRequest request) {
        if (request == null || request.getFirstName() == null || request.getLastName() == null
                || request.getFirstName().isBlank() || request.getLastName().isBlank()) {
            throw new IllegalArgumentException("First and last name are required");
        }

        Employee employee = request.getId() == null
                ? new Employee()
                : employeeRepository.findById(request.getId()).orElse(new Employee());

        if (employee.getId() == null) {
            employee.setCreatedAt(OffsetDateTime.now());
        }

        employee.setFirstName(request.getFirstName().trim());
        employee.setMiddleName(trim(request.getMiddleName()));
        employee.setLastName(request.getLastName().trim());
        employee.setGender(trim(request.getGender()));
        employee.setDateOfBirth(parseDate(request.getDateOfBirth()));
        employee.setProfilePhotoKey(trim(request.getProfilePhotoKey()));
        employee.setMaritalStatus(trim(request.getMaritalStatus()));
        employee.setBloodGroup(trim(request.getBloodGroup()));
        employee.setNationality(trim(request.getNationality()));
        employee.setMobileNumber(trim(request.getMobileNumber()));
        employee.setWhatsappNumber(trim(request.getWhatsappNumber()));
        employee.setEmail(trim(request.getEmail()));
        employee.setAddressLine1(trim(request.getAddressLine1()));
        employee.setAddressLine2(trim(request.getAddressLine2()));
        employee.setCity(trim(request.getCity()));
        employee.setState(trim(request.getState()));
        employee.setPinCode(trim(request.getPinCode()));
        employee.setEmergencyContactName(trim(request.getEmergencyContactName()));
        employee.setEmergencyContactNumber(trim(request.getEmergencyContactNumber()));
        employee.setHighestQualification(trim(request.getHighestQualification()));
        employee.setSpecialization(trim(request.getSpecialization()));
        employee.setUniversityName(trim(request.getUniversityName()));
        employee.setYearOfPassing(request.getYearOfPassing());
        employee.setExperienceYears(request.getExperienceYears());
        employee.setPreviousEmployer(trim(request.getPreviousEmployer()));
        employee.setCertifications(trim(request.getCertifications()));
        employee.setDepartment(trim(request.getDepartment()));
        employee.setDesignation(trim(request.getDesignation()));
        employee.setRole(normalizeRole(request.getRole()));
        employee.setEmploymentType(trim(request.getEmploymentType()));
        employee.setDateOfJoining(parseDate(request.getDateOfJoining()));
        employee.setProbationPeriod(trim(request.getProbationPeriod()));
        employee.setReportingManagerId(request.getReportingManagerId());
        employee.setWorkLocation(trim(request.getWorkLocation()));
        employee.setShiftTiming(trim(request.getShiftTiming()));
        employee.setAadhaarNumber(trim(request.getAadhaarNumber()));
        employee.setPanNumber(trim(request.getPanNumber()));
        employee.setPassportNumber(trim(request.getPassportNumber()));
        employee.setDrivingLicense(trim(request.getDrivingLicense()));
        employee.setContractDocKey(trim(request.getContractDocKey()));
        employee.setIdProofKey(trim(request.getIdProofKey()));
        employee.setResumeKey(trim(request.getResumeKey()));
        employee.setUpdatedAt(OffsetDateTime.now());

        Employee saved = employeeRepository.save(employee);
        EmployeeTeacherDetails teacherDetails = upsertTeacherDetails(saved.getId(), request.getTeacherDetails());
        return toResponse(saved, teacherDetails);
    }

    private EmployeeTeacherDetails upsertTeacherDetails(Long employeeId, EmployeeTeacherDetailsDto details) {
        if (details == null) {
            return teacherDetailsRepository.findByEmployeeId(employeeId).orElse(null);
        }
        EmployeeTeacherDetails entity = teacherDetailsRepository.findByEmployeeId(employeeId)
                .orElse(new EmployeeTeacherDetails());
        entity.setEmployeeId(employeeId);
        entity.setSubjectsAssigned(trim(details.getSubjectsAssigned()));
        entity.setClassesAssigned(trim(details.getClassesAssigned()));
        entity.setPeriodAllocation(trim(details.getPeriodAllocation()));
        entity.setClassTeacher(details.getClassTeacher());
        return teacherDetailsRepository.save(entity);
    }

    public void assignRole(Long employeeId, String roleValue, String password, Boolean updatePassword, Boolean active) {
        if (employeeId == null) {
            throw new IllegalArgumentException("Employee required");
        }
        if (roleValue == null || roleValue.isBlank()) {
            throw new IllegalArgumentException("Role required");
        }
        boolean shouldUpdate = updatePassword == null || updatePassword;
        if (shouldUpdate && (password == null || password.isBlank())) {
            throw new IllegalArgumentException("Password required");
        }
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        UserRole role = UserRole.valueOf(roleValue.trim().toLowerCase());
        String username = String.valueOf(employee.getId());
        AppUser user = appUserRepository.findByUsername(username).orElse(new AppUser());
        if (user.getId() == null) {
            user.setUsername(username);
            user.setDisplayName(employee.getFirstName() + " " + employee.getLastName());
            user.setCreatedAt(OffsetDateTime.now());
        }
        user.setActive(active == null ? true : active);
        user.setRole(role);
        if (shouldUpdate) {
            user.setPasswordHash(passwordEncoder.encode(password));
        }
        appUserRepository.save(user);
    }

    public void assignTeacherClasses(Long employeeId, List<String> classCodes) {
        if (employeeId == null) {
            throw new IllegalArgumentException("Employee required");
        }
        AppUser user = appUserRepository.findByUsername(String.valueOf(employeeId))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Assign login role first for this employee."
                ));
        if (user.getRole() != UserRole.teacher) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee is not a teacher");
        }
        teacherClassRepository.deleteByTeacherUserId(user.getId());
        if (classCodes == null) {
            return;
        }
        for (String code : classCodes) {
            if (code == null || code.isBlank()) {
                continue;
            }
            TeacherClass tc = new TeacherClass();
            tc.setTeacherUserId(user.getId());
            tc.setClassCode(code.trim());
            teacherClassRepository.save(tc);
        }
    }

    private EmployeeResponse toResponse(Employee employee, EmployeeTeacherDetails teacherDetails) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setEmployeeId(employee.getId());
        response.setFirstName(employee.getFirstName());
        response.setMiddleName(employee.getMiddleName());
        response.setLastName(employee.getLastName());
        response.setGender(employee.getGender());
        response.setDateOfBirth(employee.getDateOfBirth() == null ? "" : employee.getDateOfBirth().toString());
        response.setProfilePhotoKey(employee.getProfilePhotoKey());
        response.setMaritalStatus(employee.getMaritalStatus());
        response.setBloodGroup(employee.getBloodGroup());
        response.setNationality(employee.getNationality());
        response.setMobileNumber(employee.getMobileNumber());
        response.setWhatsappNumber(employee.getWhatsappNumber());
        response.setEmail(employee.getEmail());
        response.setAddressLine1(employee.getAddressLine1());
        response.setAddressLine2(employee.getAddressLine2());
        response.setCity(employee.getCity());
        response.setState(employee.getState());
        response.setPinCode(employee.getPinCode());
        response.setEmergencyContactName(employee.getEmergencyContactName());
        response.setEmergencyContactNumber(employee.getEmergencyContactNumber());
        response.setHighestQualification(employee.getHighestQualification());
        response.setSpecialization(employee.getSpecialization());
        response.setUniversityName(employee.getUniversityName());
        response.setYearOfPassing(employee.getYearOfPassing());
        response.setExperienceYears(employee.getExperienceYears());
        response.setPreviousEmployer(employee.getPreviousEmployer());
        response.setCertifications(employee.getCertifications());
        response.setDepartment(employee.getDepartment());
        response.setDesignation(employee.getDesignation());
        response.setRole(employee.getRole());
        response.setEmploymentType(employee.getEmploymentType());
        response.setDateOfJoining(employee.getDateOfJoining() == null ? "" : employee.getDateOfJoining().toString());
        response.setProbationPeriod(employee.getProbationPeriod());
        response.setReportingManagerId(employee.getReportingManagerId());
        response.setWorkLocation(employee.getWorkLocation());
        response.setShiftTiming(employee.getShiftTiming());
        response.setAadhaarNumber(employee.getAadhaarNumber());
        response.setPanNumber(employee.getPanNumber());
        response.setPassportNumber(employee.getPassportNumber());
        response.setDrivingLicense(employee.getDrivingLicense());
        response.setContractDocKey(employee.getContractDocKey());
        response.setIdProofKey(employee.getIdProofKey());
        response.setResumeKey(employee.getResumeKey());
        if (teacherDetails != null) {
            EmployeeTeacherDetailsDto dto = new EmployeeTeacherDetailsDto();
            dto.setSubjectsAssigned(teacherDetails.getSubjectsAssigned());
            dto.setClassesAssigned(teacherDetails.getClassesAssigned());
            dto.setPeriodAllocation(teacherDetails.getPeriodAllocation());
            dto.setClassTeacher(teacherDetails.getClassTeacher());
            response.setTeacherDetails(dto);
        }
        return response;
    }

    private static String normalizeRole(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(value.trim())) {
                return role.name();
            }
        }
        return value.trim();
    }

    private static String trim(String value) {
        return value == null ? null : value.trim();
    }

    private static LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value);
    }
}
