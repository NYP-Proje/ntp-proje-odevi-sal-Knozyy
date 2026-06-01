package com.okul.service;

import com.okul.dao.CourseDao;
import com.okul.dao.EnrollmentDao;
import com.okul.model.Course;
import com.okul.model.Enrollment;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EnrollmentServiceTest {

    @Test
    void enroll_uygunsa_kayit_eder() {
        EnrollmentDao enrollmentDao = mock(EnrollmentDao.class);
        CourseDao courseDao = mock(CourseDao.class);

        Course course = new Course("BIL101", "Programlama", "t1", 2, 3, "2025-Guz");
        course.setId("c1");
        when(courseDao.findById("c1")).thenReturn(course);
        when(enrollmentDao.exists("c1", "s1")).thenReturn(false);
        when(enrollmentDao.findByCourseId("c1")).thenReturn(List.of());

        EnrollmentService service = new EnrollmentService(enrollmentDao, courseDao);
        service.enroll("c1", "s1");

        verify(enrollmentDao, times(1)).save(any(Enrollment.class));
    }

    @Test
    void enroll_kontenjan_doluysa_hata_verir() {
        EnrollmentDao enrollmentDao = mock(EnrollmentDao.class);
        CourseDao courseDao = mock(CourseDao.class);

        Course course = new Course("BIL101", "Programlama", "t1", 1, 3, "2025-Guz");
        course.setId("c1");
        when(courseDao.findById("c1")).thenReturn(course);
        when(enrollmentDao.exists("c1", "s2")).thenReturn(false);

        when(enrollmentDao.findByCourseId("c1"))
                .thenReturn(List.of(new Enrollment("c1", "s1", "2026-01-01")));

        EnrollmentService service = new EnrollmentService(enrollmentDao, courseDao);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.enroll("c1", "s2"));
        assertTrue(ex.getMessage().contains("Kontenjan"));
        verify(enrollmentDao, never()).save(any());
    }

    @Test
    void enroll_zaten_kayitliysa_hata_verir() {
        EnrollmentDao enrollmentDao = mock(EnrollmentDao.class);
        CourseDao courseDao = mock(CourseDao.class);

        Course course = new Course("BIL101", "Programlama", "t1", 5, 3, "2025-Guz");
        course.setId("c1");
        when(courseDao.findById("c1")).thenReturn(course);
        when(enrollmentDao.exists("c1", "s1")).thenReturn(true);

        EnrollmentService service = new EnrollmentService(enrollmentDao, courseDao);

        assertThrows(RuntimeException.class, () -> service.enroll("c1", "s1"));
        verify(enrollmentDao, never()).save(any());
    }
}
