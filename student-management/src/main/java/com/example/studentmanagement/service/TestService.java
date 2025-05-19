package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.PublishTestRequest;
import com.example.studentmanagement.dto.SubjectDTO;
import com.example.studentmanagement.dto.SubjectMarkDTO;
import com.example.studentmanagement.dto.TestMarksDTO;
import com.example.studentmanagement.model.Mark;
import com.example.studentmanagement.model.Subject;
import com.example.studentmanagement.model.Test;
import com.example.studentmanagement.model.User;
import com.example.studentmanagement.repository.MarkRepository;
import com.example.studentmanagement.repository.TestRepository;
import com.example.studentmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestService {

    private final TestRepository testRepository;
    private final UserRepository userRepository;
    private final MarkRepository markRepository;

    public TestService(TestRepository testRepository, UserRepository userRepository, MarkRepository markRepository) {
        this.testRepository = testRepository;
        this.userRepository = userRepository;
        this.markRepository=markRepository;
    }

    public Test publishTest(PublishTestRequest request) {
        if (request.getSubjects().size() != 3) {
            throw new IllegalArgumentException("Exactly 3 subjects are required");
        }

        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Test test = new Test();
        test.setTestName(request.getTestName());
        test.setTeacher(teacher);

        List<Subject> subjects = request.getSubjects().stream().map(dto -> {
            Subject subject = new Subject();
            subject.setName(dto.getName());
            subject.setMarks(dto.getMarks());
            subject.setTest(test);
            return subject;
        }).collect(Collectors.toList());

        test.setSubjects(subjects);
        return testRepository.save(test);
    }
    
    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    
    public List<TestMarksDTO> getStudentMarks(Long studentId) {
        // Fetch all tests taken by the student (you may have a repository query for this)
        List<Test> tests = testRepository.findTestsByStudentId(studentId);

        return tests.stream().map(test -> {
            List<SubjectMarkDTO> subjectMarks = test.getSubjects().stream()
                .map(subject -> {
                    // Assuming subject entity has a way to get student's marks for that subject, e.g.
                    Integer marks = getMarksForStudentAndSubject(studentId, test.getId(), subject.getId());
                    return new SubjectMarkDTO(subject.getName(), marks);
                })
                .collect(Collectors.toList());

            return new TestMarksDTO(test.getTestName(), subjectMarks);
        }).collect(Collectors.toList());
    }

	public Optional<Test> getTestById(Long testId) {
		return testRepository.findById(testId);
	}
	
	public Integer getMarksForStudentAndSubject(Long studentId, Long testId, Long subjectId) {
	    return markRepository.findByStudentIdAndSubjectIdAndTestId(studentId, subjectId, testId)
	            .map(Mark::getScore) 
	            .orElse(0); 
	}




}
