package com.backend.studentgrades.controller;

import com.backend.studentgrades.model.*;
import com.backend.studentgrades.repo.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.util.Optional;


@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    StudentService studentService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<PaginationAndList> search(@RequestParam(required = false) String fullName,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromBirthDate,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toBirthDate,
                                                    @RequestParam(required = false) Integer fromSatScore,
                                                    @RequestParam(required = false) Integer toSatScore,
                                                    @RequestParam(required = false) Integer fromAvgScore,
                                                    @RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "50") @Min(1) Integer count,
                                                    @RequestParam(defaultValue = "id") StudentSortField sort, @RequestParam(defaultValue = "asc") SortDirection sortDirection) throws JsonProcessingException {

        var res = studentService.search(fullName, fromBirthDate, toBirthDate, fromSatScore, toSatScore, fromAvgScore, page, count, sort, sortDirection);

        return ResponseEntity.ok(res);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOneStudent(@PathVariable Long id)
    {
        return new ResponseEntity<>(studentService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> insertStudent(@RequestBody StudentIn studentIn)
    {
        Student student = studentIn.toStudent();
        student = studentService.save(student);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody StudentIn student)
    {
        Optional<Student> dbStudent = studentService.findById(id);
        if (dbStudent.isEmpty()) throw new RuntimeException("Student with id: " + id + " not found");
        student.updateStudent(dbStudent.get());
        Student updatedStudent = studentService.save(dbStudent.get());
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteStudent(@PathVariable Long id)
    {
        Optional<Student> dbStudent = studentService.findById(id);
        if (dbStudent.isEmpty()) throw new RuntimeException("Student with id: " + id + " not found");
        studentService.delete(dbStudent.get());
        return new ResponseEntity<>("DELETED", HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/image", method = RequestMethod.PUT)
    public ResponseEntity<?> uploadStudentImage(@PathVariable Long id,  @RequestParam("image") MultipartFile image)
    {
        Optional<Student> dbStudent = studentService.findById(id);
        if (dbStudent.isEmpty()) throw new RuntimeException("Student with id: " + id + " not found");
        String bucketPath = "apps/netanel/student-" +  id + ".png" ;
        var awsService = studentService.uploadStudentPicture(image, bucketPath);
        dbStudent.get().setProfilePicture(bucketPath);
        Student updatedStudent = studentService.save(dbStudent.get());
        return new ResponseEntity<>(StudentOut.of(updatedStudent, awsService) , HttpStatus.OK);
    }

}