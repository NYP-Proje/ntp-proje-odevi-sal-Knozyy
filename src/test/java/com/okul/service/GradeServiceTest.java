package com.okul.service;

import com.okul.dao.CourseDao;
import com.okul.dao.GradeDao;
import com.okul.model.Course;
import com.okul.model.Grade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GradeServiceTest {

    @Test
    void assignGrade_yeni_not_kaydeder_ve_harf_hesaplar() {
        GradeDao gradeDao = mock(GradeDao.class);
        CourseDao courseDao = mock(CourseDao.class);
        when(gradeDao.findByCourseAndStudent("c1", "s1")).thenReturn(null);

        GradeService service = new GradeService(gradeDao, courseDao);
        service.assignGrade("c1", "s1", 95);

        verify(gradeDao).save(argThat(g -> g.getLetterGrade().equals("AA") && g.getScore() == 95));
    }

    @Test
    void assignGrade_gecersiz_not_hata_verir() {
        GradeService service = new GradeService(mock(GradeDao.class), mock(CourseDao.class));
        assertThrows(RuntimeException.class, () -> service.assignGrade("c1", "s1", 150));
    }

    @Test
    void calculateGpa_transkripti_kullanarak_gano_hesaplar() {
        GradeDao gradeDao = mock(GradeDao.class);
        CourseDao courseDao = mock(CourseDao.class);

        Grade g1 = new Grade("c1", "s1", 95, "AA");
        Grade g2 = new Grade("c2", "s1", 75, "BB");
        when(gradeDao.findByStudentId("s1")).thenReturn(List.of(g1, g2));

        Course c1 = new Course("BIL101", "Programlama", "t1", 30, 3, "2025-Guz");
        Course c2 = new Course("MAT101", "Matematik", "t1", 30, 4, "2025-Guz");
        when(courseDao.findById("c1")).thenReturn(c1);
        when(courseDao.findById("c2")).thenReturn(c2);

        GradeService service = new GradeService(gradeDao, courseDao);
        double gpa = service.calculateGpa("s1");

        assertEquals(3.43, gpa, 0.001);
    }
}
