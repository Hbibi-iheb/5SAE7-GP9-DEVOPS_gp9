package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.ICourseRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@AllArgsConstructor
@Service
public class CourseServicesImpl implements ICourseServices {

    private static final Logger logger = LogManager.getLogger(CourseServicesImpl.class);
    private ICourseRepository courseRepository;

    @Override
    public List<Course> retrieveAllCourses() {
        logger.info("Retrieving all courses from the database.");
        List<Course> courses = courseRepository.findAll();
        logger.debug("Number of courses retrieved: {}", courses.size());
        return courses;
    }

    @Override
    public Course addCourse(Course course) {
        logger.info("Adding a new course with type: {} and level: {}", course.getTypeCourse(), course.getLevel());
        try {
            Course savedCourse = courseRepository.save(course);
            logger.debug("Course added successfully with ID: {}", savedCourse.getNumCourse());
            return savedCourse;
        } catch (Exception e) {
            logger.error("Error while adding course of type {}: {}", course.getTypeCourse(), e.getMessage());
            throw e;
        }
    }

    @Override
    public Course updateCourse(Course course) {
        logger.info("Updating course with ID: {}", course.getNumCourse());
        try {
            Course updatedCourse = courseRepository.save(course);
            logger.debug("Course updated successfully with ID: {}", updatedCourse.getNumCourse());
            return updatedCourse;
        } catch (Exception e) {
            logger.error("Error while updating course with ID {}: {}", course.getNumCourse(), e.getMessage());
            throw e;
        }
    }

    @Override
    public Course retrieveCourse(Long numCourse) {
        logger.info("Retrieving course with ID: {}", numCourse);
        return courseRepository.findById(numCourse).orElseGet(() -> {
            logger.warn("Course with ID {} not found.", numCourse);
            return null;
        });
    }
}
