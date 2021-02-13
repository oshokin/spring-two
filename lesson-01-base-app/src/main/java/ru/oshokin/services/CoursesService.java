package ru.oshokin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.oshokin.entities.Course;
import ru.oshokin.repositories.CoursesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CoursesService {

    private CoursesRepository coursesRepository;

    @Autowired
    public void setCoursesRepository(CoursesRepository coursesRepository) {
        this.coursesRepository = coursesRepository;
    }

    public List<Course> getAllCoursesList() {
        return (List<Course>) coursesRepository.findAll();
    }

    public Course getCourseById(Long id) {
        Optional<Course> course = coursesRepository.findById(id);
        if (course.isPresent()) {
            return course.get();
        }
        return null;
    }

}
