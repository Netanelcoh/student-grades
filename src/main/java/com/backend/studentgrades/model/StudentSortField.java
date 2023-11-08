package com.backend.studentgrades.model;

public enum StudentSortField {
    id("id") ,
    createdAt ("created_at"),
    fullName ("fullname"),
    birthDate ("birth_date"),
    satScore ("sat_score"),
    graduationScore ("graduation_score"),
    phone ("phone"),
    profilepicture ("profile_picture"),
    avgScore (" (select avg(sg.course_score) from  student_grade sg where sg.student_id = s.id ) ");

    public final String fieldName;
    private StudentSortField(String fieldName) {
        this.fieldName = fieldName;
    }
}