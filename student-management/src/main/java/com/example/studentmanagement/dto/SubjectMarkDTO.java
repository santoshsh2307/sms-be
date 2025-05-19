package com.example.studentmanagement.dto;

//SubjectMarkDTO.java
public class SubjectMarkDTO {
 private String name;
 private Integer marks;

 public SubjectMarkDTO(String name, Integer marks) {
     this.name = name;
     this.marks = marks;
 }

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public Integer getMarks() {
	return marks;
}

public void setMarks(Integer marks) {
	this.marks = marks;
}
 
}

