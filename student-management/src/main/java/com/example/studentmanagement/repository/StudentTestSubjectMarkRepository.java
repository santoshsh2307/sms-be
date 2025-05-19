package com.example.studentmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.studentmanagement.model.StudentTestSubjectMark;

@Repository
public interface StudentTestSubjectMarkRepository extends JpaRepository<StudentTestSubjectMark, Long> {

    Optional<StudentTestSubjectMark> findByStudentIdAndTestIdAndSubjectId(Long studentId, Long testId, Long subjectId);

}
