package com.example.studentmanagement.dto;

import java.util.List;

public class TestResponseDTO {
    private Long id;
    private String testName;
    private String teacherName;
    private List<String> subjects;

    public TestResponseDTO() {}

    public TestResponseDTO(Long id, String testName, String teacherName, List<String> subjects) {
        this.id = id;
        this.testName = testName;
        this.teacherName = teacherName;
        this.subjects = subjects;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public List<String> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}

    
}
