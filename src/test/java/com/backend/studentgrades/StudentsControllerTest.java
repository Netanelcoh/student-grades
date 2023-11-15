package com.backend.studentgrades;

import com.backend.studentgrades.controller.StudentController;
import com.backend.studentgrades.model.StudentOut;
import com.backend.studentgrades.util.AWSService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.studentgrades.utils.TestContext;
import com.backend.studentgrades.utils.TestMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


import static com.backend.studentgrades.utils.StudentSearch.StudentSearchBuilder.aStudentSearch;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;



@SpringBootTest(classes = {TestMocks.class})
@ActiveProfiles({"test"})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitConfig
class StudentsControllerTest {
    @Autowired
    private ObjectMapper om;


    @Autowired
    private StudentController sc;

    @Autowired
    private AWSService awsService;

    private TestContext c;

    @BeforeEach
    void initContext() {
        c = new TestContext(om);
        c.setStudentController(sc);
    }

    @Test
    void get10Students() throws Exception {
        c.givenStudents(10, sc);
        assertThat(aStudentSearch(sc, c.testUuid()).execute().getData().size(), is(10));
    }

    @Test
    void checkSatFromFilter() throws Exception {
        c.givenStudents(10, sc);
        assertThat(aStudentSearch(sc, c.testUuid()).fromSatScore(600).execute().getData().size(), is(5));
    }

    @Test
    void checkSatToFilter() throws Exception {
        c.givenStudents(10, sc);
        assertThat(aStudentSearch(sc, c.testUuid()).toSatScore(600).execute().getData().size(), is(6));
    }

/*    @Test
    void checkSmsSent() throws Exception {
        c.givenStudents(10, sc);
        sc.smsAll("hi");
        Thread.sleep(1000);
        verify(smsService, atLeastOnce()).send(any(),any());
    }*/

    @Test
    void checkPictureUpload() throws Exception {
        c.givenStudents(1, sc);
        StudentOut student = c.getFirstStudent();
        sc.uploadStudentImage(student.getId(),  new MockMultipartFile("test", "originalFileName", "png", new byte[0]));
        verify(awsService, times(  1)).putInBucket(any(),any());
    }

}
