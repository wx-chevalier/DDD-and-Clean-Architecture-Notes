package com.educational.platform.course.reviews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.UUID;

/**
 * Represents review course request.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCourseRequest {

    @Max(5)
    @PositiveOrZero
    @NotNull
    private Double rating;
    private String comment;

}
