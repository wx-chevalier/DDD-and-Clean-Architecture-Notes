package com.educational.platform.courses.course;

import com.educational.platform.common.domain.ValueObject;
import lombok.*;

import jakarta.persistence.Embeddable;

/**
 * Represents Course Rating model.
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class CourseRating implements ValueObject {

    private double rating;

}
