package com.backend.studentgrades.repo;


import com.backend.studentgrades.model.StudentGrade;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentGradeRepository extends CrudRepository<StudentGrade,Long> {

    List<StudentGrade> findByStudentId(Long id);

}
