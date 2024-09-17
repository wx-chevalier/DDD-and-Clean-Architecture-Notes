package com.educational.platform.courses.course.create;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateQuizCommand extends CreateCurriculumItemCommand {

	private final String text;
	private final List<CreateQuestionCommand> questions;

	@Builder
	public CreateQuizCommand(List<CreateQuestionCommand> questions, String title, String description, Integer serialNumber, String text) {
		super(title, description, serialNumber);
		this.text = text;
		this.questions = questions;
	}

}
