package com.educational.platform.courses.course;

import java.util.UUID;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Represents Curriculum Item domain model.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("null")
public abstract class CurriculumItem {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;

	private UUID uuid;

	private String title;
	private String description;
	private Integer serialNumber;

	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;

	// for JPA
	protected CurriculumItem() {

	}

	protected CurriculumItem(String title, String description, Course course, Integer serialNumber) {
		this.uuid = UUID.randomUUID();
		this.title = title;
		this.description = description;
		this.course = course;
		this.serialNumber = serialNumber;
	}
}
