package com.backend.studentgrades.repo;

import com.backend.studentgrades.model.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {
}
