package com.backend.studentgrades.repo;

import com.backend.studentgrades.model.*;
import com.backend.studentgrades.util.AWSService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

import static com.backend.studentgrades.util.Dates.atUtc;
import static com.backend.studentgrades.util.FPS.FPSBuilder.aFPS;
import static com.backend.studentgrades.util.FPSCondition.FPSConditionBuilder.aFPSCondition;
import static com.backend.studentgrades.util.FPSField.FPSFieldBuilder.aFPSField;
import static com.backend.studentgrades.util.Strings.likeLowerOrNull;

@Service
public class StudentService {

    @Autowired
    StudentRepository repository;

    @Autowired
    EntityManager em;

    @Autowired
    ObjectMapper om;

    @Autowired
    AWSService awsService;


    public PaginationAndList search(String fullName,
                                    LocalDate fromBirthDate,
                                    LocalDate toBirthDate,
                                    Integer fromSatScore,
                                    Integer toSatScore,
                                    Integer fromAvgScore,
                                    Integer page,
                                    Integer count,
                                    StudentSortField sort,
                                    SortDirection sortDirection) throws JsonProcessingException {

        var res =aFPS().select(List.of(
                        aFPSField().field("id").alias("id").build(),
                        aFPSField().field("created_at").alias("createdat").build(),
                        aFPSField().field("fullname").alias("fullname").build(),
                        aFPSField().field("birth_date").alias("birthdate").build(),
                        aFPSField().field("sat_score").alias("satscore").build(),
                        aFPSField().field("graduation_score").alias("graduationscore").build(),
                        aFPSField().field("phone").alias("phone").build(),
                        aFPSField().field("profile_picture").alias("profilepicture").build(),
                        aFPSField().field("(select avg(sg.course_score) from  student_grade sg where sg.student_id = s.id ) ").alias("avgscore").build()

                ))
                .from(List.of(" student s"))
                .conditions(List.of(
                        aFPSCondition().condition("( lower(fullname) like :fullName )").parameterName("fullName").value(likeLowerOrNull(fullName)).build(),
                        aFPSCondition().condition("( s.birth_Date >= :fromBirthDate )").parameterName("fromBirthDate").value(atUtc(fromBirthDate)).build(),
                        aFPSCondition().condition("( s.birth_Date <= :toBirthDate )").parameterName("toBirthDate").value(atUtc(toBirthDate)).build(),
                        aFPSCondition().condition("( sat_score >= :fromSatScore )").parameterName("fromSatScore").value(fromSatScore).build(),
                        aFPSCondition().condition("( sat_score <= :toSatScore )").parameterName("toSatScore").value(toSatScore).build(),
                        aFPSCondition().condition("( (select avg(sg.course_score) from  student_grade sg where sg.student_id = s.id ) >= :fromAvgScore )").parameterName("fromAvgScore").value(fromAvgScore).build()
                )).sortField(sort.fieldName).sortDirection(sortDirection).page(page).count(count)
                .itemClass(StudentOut.class)
                .build().exec(em, om);

        return res;
    }

    public Optional<Student> findById(Long id) {
        return repository.findById(id);
    }


    public Student save(Student student) {
        return repository.save(student);
    }

    public void delete(Student student) {
        repository.delete(student);
    }

    public AWSService uploadStudentPicture(MultipartFile image, String bucketPath) {
        awsService.putInBucket(image, bucketPath);

        return awsService;
    }

}