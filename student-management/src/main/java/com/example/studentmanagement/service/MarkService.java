package com.example.studentmanagement.service;

import com.example.studentmanagement.model.Mark;
import com.example.studentmanagement.repository.MarkRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MarkService {

    private final MarkRepository markRepository;

    public MarkService(MarkRepository markRepository) {
        this.markRepository = markRepository;
    }

    public Mark saveMark(Mark mark) {
        return markRepository.save(mark);
    }

    public Optional<Mark> getMarksForStudentAndSubject(Long studentId, Long subjectId, Long testId) {
        return markRepository.findByStudentIdAndSubjectIdAndTestId(studentId, subjectId, testId);
    }
}
