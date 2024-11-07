package tn.esprit.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.services.CourseServicesImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CourseServicesImplTest {

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseServices;

    private Course course;

    @BeforeEach
    public void setUp() {
        // Initialize a sample course object
        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course.setSupport(Support.SKI);
        course.setPrice(100.0f);
        course.setTimeSlot(2);
    }

    @Test
    public void testRetrieveAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course));

        List<Course> courses = courseServices.retrieveAllCourses();

        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getNumCourse()).isEqualTo(course.getNumCourse());
    }

    @Test
    public void testAddCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course savedCourse = courseServices.addCourse(course);

        assertThat(savedCourse.getNumCourse()).isEqualTo(course.getNumCourse());
    }

    @Test
    public void testUpdateCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course updatedCourse = courseServices.updateCourse(course);

        assertThat(updatedCourse.getNumCourse()).isEqualTo(course.getNumCourse());
    }

    @Test
    public void testRetrieveCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course foundCourse = courseServices.retrieveCourse(1L);

        assertThat(foundCourse).isNotNull();
        assertThat(foundCourse.getNumCourse()).isEqualTo(course.getNumCourse());
    }

    @Test
    public void testRetrieveCourse_NotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Course foundCourse = courseServices.retrieveCourse(1L);

        assertThat(foundCourse).isNull();
    }
}
