package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.PublishTestRequest;
import com.example.studentmanagement.dto.SubjectDTO;
import com.example.studentmanagement.dto.TestMarksDTO;
import com.example.studentmanagement.dto.TestResponseDTO;
import com.example.studentmanagement.model.Mark;
import com.example.studentmanagement.model.Subject;
import com.example.studentmanagement.model.Test;
import com.example.studentmanagement.model.User;
import com.example.studentmanagement.service.MarkService;
import com.example.studentmanagement.service.SubjectService;
import com.example.studentmanagement.service.TestService;
import com.example.studentmanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teacher")
@PreAuthorize("hasRole('TEACHER')")
@CrossOrigin(origins = "*")
public class TeacherController {

    private final UserService userService;
    private final TestService testService;
    private final MarkService markService;
    private final SubjectService subjectService;

    public TeacherController(UserService userService, TestService testService,
                             MarkService markService, SubjectService subjectService) {
        this.userService = userService;
        this.testService = testService;
        this.markService = markService;
        this.subjectService = subjectService;
    }

    // ✅ Get all students
    @GetMapping("/students")
    public ResponseEntity<List<User>> getAllStudents() {
        List<User> students = userService.findAllUsers().stream()
            .filter(user -> user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("STUDENT")))
            .collect(Collectors.toList());
        return ResponseEntity.ok(students);
    }

    // ✅ Get specific student by ID
    @GetMapping("/students/{id}")
    public ResponseEntity<User> getStudent(@PathVariable Long id) {
        return userService.findById(id)
            .filter(user -> user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("STUDENT")))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Update student info
    @PutMapping("/students/{id}")
    public ResponseEntity<User> updateStudent(@PathVariable Long id, @RequestBody User updatedStudent) {
        return userService.findById(id)
            .filter(user -> user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("STUDENT")))
            .map(existingStudent -> {
                existingStudent.setUsername(updatedStudent.getUsername());
                existingStudent.setPassword(updatedStudent.getPassword()); // ⚠️ Consider encoding
                userService.saveUser(existingStudent);
                return ResponseEntity.ok(existingStudent);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Publish a new test
    @PostMapping("/publish-test")
    public ResponseEntity<Test> publishTest(@RequestBody PublishTestRequest request) {
        Test test = testService.publishTest(request);
        return ResponseEntity.ok(test);
    }

    // ✅ Get all tests
    @GetMapping("/tests")
    public ResponseEntity<List<TestResponseDTO>> getAllTests() {
        List<Test> tests = testService.getAllTests();
        List<TestResponseDTO> response = tests.stream()
            .map(test -> new TestResponseDTO(
                test.getId(),
                test.getTestName(),
                test.getTeacher() != null ? test.getTeacher().getUsername() : "Unknown",
                test.getSubjects().stream()
                    .map(Subject::getName)
                    .collect(Collectors.toList())
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // ✅ Get student's score for a specific test and subject
//    @GetMapping("/students/{studentId}/subjects/{subjectId}/tests/{testId}/marks")
//    public ResponseEntity<Integer> getMarksForStudentAndSubject(
//            @PathVariable Long studentId,
//            @PathVariable Long subjectId,
//            @PathVariable Long testId) {
//
//        Integer score = markService.getMarksForStudentAndSubject(studentId, subjectId, testId);
//        return ResponseEntity.ok(score);
//    }

    // ✅ Save marks for a student
    @PostMapping("/students/{studentId}/subjects/{subjectId}/tests/{testId}/marks")
    public ResponseEntity<?> saveMarks(
            @PathVariable Long studentId,
            @PathVariable Long subjectId,
            @PathVariable Long testId,
            @RequestBody Integer score) {

        var studentOpt = userService.findById(studentId);
        var testOpt = testService.getTestById(testId);
        var subjectOpt = subjectService.getSubjectById(subjectId);

        if (studentOpt.isEmpty() || testOpt.isEmpty() || subjectOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Mark mark = new Mark(studentOpt.get(), testOpt.get(), subjectOpt.get(), score);
        Mark savedMark = markService.saveMark(mark);

        Map<String, String> map = new HashMap();
        map.put("message", "success");
        return ResponseEntity.ok(map);
    }

    // ✅ Get all marks for a student
    @GetMapping("/students/{id}/marks")
    public ResponseEntity<List<TestMarksDTO>> getStudentMarks(@PathVariable Long id) {
        return userService.findById(id)
            .filter(user -> user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("STUDENT")))
            .map(student -> {
                List<TestMarksDTO> marks = testService.getStudentMarks(id);
                return ResponseEntity.ok(marks);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/tests/{testId}/subjects")
    public ResponseEntity<List<SubjectDTO>> getSubjectsByTestId(@PathVariable Long testId) {
        return testService.getTestById(testId)
            .map(test -> {
                List<SubjectDTO> subjectDTOs = test.getSubjects().stream()
                    .map(subject -> {
                        SubjectDTO dto = new SubjectDTO();
                        dto.setId(subject.getId());
                        dto.setName(subject.getName());
                        dto.setMarks(subject.getMarks());
                        return dto;
                    })
                    .collect(Collectors.toList());
                return ResponseEntity.ok(subjectDTOs);
            })
            .orElse(ResponseEntity.notFound().build());
    }

}
