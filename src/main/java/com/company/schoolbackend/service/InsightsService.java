package com.company.schoolbackend.service;

import com.company.schoolbackend.dto.InsightsResponse;
import com.company.schoolbackend.dto.StudentInsight;
import com.company.schoolbackend.dto.TeacherInsight;
import com.company.schoolbackend.entity.Student;
import com.company.schoolbackend.entity.StudentScore;
import com.company.schoolbackend.entity.TeacherRating;
import com.company.schoolbackend.entity.TeacherRatingClass;
import com.company.schoolbackend.repository.StudentRepository;
import com.company.schoolbackend.repository.StudentScoreRepository;
import com.company.schoolbackend.repository.TeacherRatingClassRepository;
import com.company.schoolbackend.repository.TeacherRatingRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class InsightsService {
    private final StudentRepository studentRepository;
    private final StudentScoreRepository studentScoreRepository;
    private final TeacherRatingRepository teacherRatingRepository;
    private final TeacherRatingClassRepository teacherRatingClassRepository;

    public InsightsService(
            StudentRepository studentRepository,
            StudentScoreRepository studentScoreRepository,
            TeacherRatingRepository teacherRatingRepository,
            TeacherRatingClassRepository teacherRatingClassRepository
    ) {
        this.studentRepository = studentRepository;
        this.studentScoreRepository = studentScoreRepository;
        this.teacherRatingRepository = teacherRatingRepository;
        this.teacherRatingClassRepository = teacherRatingClassRepository;
    }

    public InsightsResponse getInsights(String classCode, String scoreOp, Double scoreValue) {
        List<Student> students = studentRepository.findAll();
        if (classCode != null && !classCode.isBlank() && !"all".equalsIgnoreCase(classCode)) {
            students = students.stream().filter(s -> classCode.equals(s.getClassCode())).collect(Collectors.toList());
        }
        Map<String, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getId, s -> s));
        List<StudentScore> scores = studentScoreRepository.findByStudentIdIn(new ArrayList<>(studentMap.keySet()));

        List<StudentInsight> insights = new ArrayList<>();
        for (StudentScore score : scores) {
            Student student = studentMap.get(score.getStudentId());
            if (student == null) {
                continue;
            }
            insights.add(new StudentInsight(student.getName(), student.getClassCode(), score.getScore().doubleValue()));
        }

        Comparator<StudentInsight> byScoreDesc = Comparator.comparingDouble(StudentInsight::getScore).reversed();
        List<StudentInsight> sorted = insights.stream().sorted(byScoreDesc).collect(Collectors.toList());

        List<StudentInsight> filtered = new ArrayList<>(sorted);
        if (scoreValue != null && scoreOp != null) {
            filtered = sorted.stream().filter(item -> {
                if (">".equals(scoreOp)) {
                    return item.getScore() > scoreValue;
                }
                if ("<".equals(scoreOp)) {
                    return item.getScore() < scoreValue;
                }
                return item.getScore() == scoreValue;
            }).collect(Collectors.toList());
        }

        List<StudentInsight> atRisk = sorted.stream()
                .filter(item -> item.getScore() < 8.0)
                .collect(Collectors.toList());

        List<TeacherRating> ratings = teacherRatingRepository.findAllByOrderByRatingDesc();
        Map<Long, List<String>> teacherClasses = new HashMap<>();
        if (classCode != null && !classCode.isBlank() && !"all".equalsIgnoreCase(classCode)) {
            List<TeacherRatingClass> filteredClasses = teacherRatingClassRepository.findByClassCode(classCode);
            Set<Long> ratingIds = filteredClasses.stream().map(TeacherRatingClass::getTeacherRatingId).collect(Collectors.toSet());
            ratings = ratings.stream().filter(r -> ratingIds.contains(r.getId())).collect(Collectors.toList());
            for (TeacherRatingClass link : filteredClasses) {
                teacherClasses.computeIfAbsent(link.getTeacherRatingId(), key -> new ArrayList<>()).add(link.getClassCode());
            }
        } else {
            List<TeacherRatingClass> allLinks = teacherRatingClassRepository.findByTeacherRatingIdIn(
                    ratings.stream().map(TeacherRating::getId).collect(Collectors.toList())
            );
            for (TeacherRatingClass link : allLinks) {
                teacherClasses.computeIfAbsent(link.getTeacherRatingId(), key -> new ArrayList<>()).add(link.getClassCode());
            }
        }

        List<TeacherInsight> teachers = new ArrayList<>();
        for (TeacherRating rating : ratings) {
            List<String> classes = teacherClasses.getOrDefault(rating.getId(), new ArrayList<>());
            teachers.add(new TeacherInsight(rating.getName(), rating.getSubject(), rating.getRating().doubleValue(), classes));
        }

        InsightsResponse response = new InsightsResponse();
        response.setTopStudents(filtered);
        response.setAtRiskStudents(atRisk);
        response.setTopTeachers(teachers);
        return response;
    }
}
