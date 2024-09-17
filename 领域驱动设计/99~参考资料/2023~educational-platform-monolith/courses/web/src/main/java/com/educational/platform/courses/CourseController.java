package com.educational.platform.courses;

import com.educational.platform.courses.course.CourseCannotBePublishedException;
import com.educational.platform.courses.course.create.CreateCourseCommand;
import com.educational.platform.courses.course.publish.PublishCourseCommand;
import com.educational.platform.web.handler.ErrorResponse;
import lombok.RequiredArgsConstructor;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Represents Course API adapter.
 */
@Validated
@RequestMapping(value = "/courses")
@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CommandGateway commandGateway;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    CreatedCourseResponse create(@Valid @RequestBody CreateCourseRequest courseCreateRequest) {
        final CreateCourseCommand command = CreateCourseCommand.builder()
                .name(courseCreateRequest.getName())
                .description(courseCreateRequest.getDescription())
                .build();

        return new CreatedCourseResponse(commandGateway.sendAndWait(command));
    }

    @PutMapping(value = "/{uuid}/publish-status", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void publish(@PathVariable("uuid") UUID uuid) {
        commandGateway.sendAndWait(new PublishCourseCommand(uuid));
    }

    @ExceptionHandler(CourseCannotBePublishedException.class)
    public ResponseEntity<ErrorResponse> onConflictException(Exception e) {
        final ErrorResponse response = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
