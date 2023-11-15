package com.backend.studentgrades.utils;

import static com.backend.studentgrades.utils.StudentSearch.StudentSearchBuilder.aStudentSearch;
import com.backend.studentgrades.model.StudentIn;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.backend.studentgrades.controller.StudentController;
import com.backend.studentgrades.model.StudentOut;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.mockito.Mockito;


import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;


public class TestContext {

    private final String userCreate = UUID.randomUUID().toString();

    private final Map<String, String> vars = new HashMap<>();

    private final ObjectMapper om;

    private final Principal principal;

    private StudentController sc;

    public TestContext(ObjectMapper om) {
        super();
        this.om = om;
        vars.put("fullName", "");
        vars.put("phone", "");
        vars.put("satScore", "");
        vars.put("graduationScore", "");
        principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(userCreate);
    }

    public void givenStudents(int numStudents, StudentController studentController) throws Exception {

        for (int i = 0; i < numStudents; i++) {
            vars.put("fullName", "Student-" + testUuid() + Strings.padStart(Integer.toString(i), 5, '0'));
            vars.put("phone", i % 2 == 0 ? "" : "0500000000" + String.valueOf(i));
            vars.put("satScore",  String.valueOf(500 + 20*i));
            vars.put("graduationScore", String.valueOf(70 + 2*i));
            studentController.insertStudent(get("json/student.json", StudentIn.class));
        }
    }

    public StudentOut getFirstStudent() throws Exception {
        return (StudentOut)search().execute().getData().get(0);
    }

    public StudentSearch.StudentSearchBuilder search() {
        return aStudentSearch(sc, testUuid());
    }

    private String populate(String source) {
        StrSubstitutor sub = new StrSubstitutor(vars, "{{", "}}");
        return sub.replace(source);
    }

    public <T> T get(String jsonFile, Class<T> clazz) throws Exception {
        String json = readFile(ClassLoader.getSystemResource(jsonFile).toURI());
        String populatedJson = populate(json);
        return om.readValue(populatedJson, clazz);
    }

    public void setStudentController(StudentController sc) {
        this.sc = sc;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public String testUuid() {
        return userCreate;
    }

    public Principal getUser() {
        return principal;
    }

    private static String readFile(URI filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

}
