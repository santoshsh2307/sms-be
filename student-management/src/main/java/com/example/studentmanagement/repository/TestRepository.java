package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Test;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TestRepository extends JpaRepository<Test, Long> {

	// TestRepository.java
	@Query("SELECT DISTINCT m.test FROM Mark m WHERE m.student.id = :studentId")
	List<Test> findTestsByStudentId(@Param("studentId") Long studentId);

}
