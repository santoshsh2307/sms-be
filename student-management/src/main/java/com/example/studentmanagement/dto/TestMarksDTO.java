package com.example.studentmanagement.dto;

import java.util.List;

public class TestMarksDTO {
 private String testName;
 private List<SubjectMarkDTO> subjects;

 public TestMarksDTO(String testName, List<SubjectMarkDTO> subjects) {
     this.testName = testName;
     this.subjects = subjects;
 }

public String getTestName() {
	return testName;
}

public void setTestName(String testName) {
	this.testName = testName;
}

public List<SubjectMarkDTO> getSubjects() {
	return subjects;
}

public void setSubjects(List<SubjectMarkDTO> subjects) {
	this.subjects = subjects;
}
 
}
