package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Mark;
import com.example.studentmanagement.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark, Long> {

    Optional<Mark> findByStudentIdAndSubjectIdAndTestId(Long studentId, Long subjectId, Long testId);



    

}
