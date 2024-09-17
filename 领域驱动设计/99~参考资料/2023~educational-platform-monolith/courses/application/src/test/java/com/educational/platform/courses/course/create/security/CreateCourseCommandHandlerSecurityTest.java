package com.educational.platform.courses.course.create.security;

import com.educational.platform.courses.course.Course;
import com.educational.platform.courses.course.CourseFactory;
import com.educational.platform.courses.course.CourseRepository;
import com.educational.platform.courses.course.create.CreateCourseCommand;
import com.educational.platform.courses.course.create.CreateCourseCommandHandler;
import com.educational.platform.courses.teacher.Teacher;
import com.educational.platform.courses.teacher.TeacherRepository;
import com.educational.platform.courses.teacher.create.CreateTeacherCommand;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = "com.educational.platform.security.enabled=true")
public class CreateCourseCommandHandlerSecurityTest {

    @Autowired
    private CourseRepository repository;

    @Autowired
    private CourseFactory courseFactory;

    @SpyBean
    private CreateCourseCommandHandler sut;

    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    void setUp() {
        teacherRepository.save(new Teacher(new CreateTeacherCommand("username")));
    }

    @WithMockUser(username = "username", roles = { "TEACHER" })
    @Test
    void handle_userIsTeacher_courseSaved() {
        // given
        final CreateCourseCommand command = CreateCourseCommand.builder()
                .name("name")
                .description("description")
                .build();

        // when
        sut.handle(command);

        // then
        final Optional<Course> saved = repository.findAll()
                .stream()
                .filter(course -> "name".equals(ReflectionTestUtils.getField(course, "name")))
                .findAny();
        assertThat(saved).isNotEmpty();
    }

    @WithMockUser(roles = "STUDENT")
    @Test
    void handle_userIsStudent_accessDeniedException() {
        // given
        final CreateCourseCommand command = CreateCourseCommand.builder()
                .name("name")
                .description("description")
                .build();

        // when
        final ThrowingCallable createAction = () -> sut.handle(command);

        // then
        assertThatThrownBy(createAction)
                .isInstanceOf(AccessDeniedException.class);
    }
}
