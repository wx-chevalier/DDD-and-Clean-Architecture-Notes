package com.educational.platform.course.reviews.create;

import com.educational.platform.common.exception.RelatedResourceIsNotResolvedException;
import com.educational.platform.course.reviews.CourseReview;
import com.educational.platform.course.reviews.CourseReviewFactory;
import com.educational.platform.course.reviews.CourseReviewRepository;
import lombok.RequiredArgsConstructor;

import org.axonframework.commandhandling.CommandHandler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolationException;
import java.util.UUID;

/**
 * Command handler for {@link ReviewCourseCommand} creates a course review.
 */
@RequiredArgsConstructor
@Component
@Transactional
public class ReviewCourseCommandHandler {

    private final CourseReviewRepository courseReviewRepository;
    private final CourseReviewFactory courseReviewFactory;

    /**
     * Creates course review from command.
     *
     * @param command command
     * @return uuid
     * @throws ConstraintViolationException          in the case of validation issues
     * @throws RelatedResourceIsNotResolvedException if course or reviewer is not found by relation
     */
    @CommandHandler
    @NonNull
    public UUID handle(ReviewCourseCommand command) {
        final CourseReview courseReview = courseReviewFactory.createFrom(command);
        courseReviewRepository.save(courseReview);

        return courseReview.toIdentifier();
    }

}
