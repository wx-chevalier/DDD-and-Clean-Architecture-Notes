package com.educational.platform.courses.course;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String content;

	@ManyToOne
	@JoinColumn(name = "quiz_id")
	private Quiz quiz;

	// for JPA
	protected Question() {
		super();
	}

	public Question(String content, Quiz quiz) {
		this.content = content;
		this.quiz = quiz;
	}

}
