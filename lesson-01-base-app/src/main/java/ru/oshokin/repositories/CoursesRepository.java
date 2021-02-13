package ru.oshokin.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.oshokin.entities.Course;

@Repository
public interface CoursesRepository extends CrudRepository<Course, Long> {
}
