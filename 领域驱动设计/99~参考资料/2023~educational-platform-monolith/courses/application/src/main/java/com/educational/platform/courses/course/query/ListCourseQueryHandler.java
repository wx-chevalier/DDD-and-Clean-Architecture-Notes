package com.educational.platform.courses.course.query;

import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.educational.platform.courses.course.CourseLightDTO;
import com.educational.platform.courses.course.CourseRepository;

import lombok.RequiredArgsConstructor;

/**
 * Query handler for getting the list of courses.
 */
@RequiredArgsConstructor
@Component
public class ListCourseQueryHandler {

	private final CourseRepository repository;

	/**
	 * Retrieves list of course dtos.
	 *
	 * @param query query.
	 * @return corresponding list of course dtos.
	 */
	@QueryHandler
	@NonNull
	public List<CourseLightDTO> handle(ListCourseQuery query) {
		return repository.list();
	}

}
