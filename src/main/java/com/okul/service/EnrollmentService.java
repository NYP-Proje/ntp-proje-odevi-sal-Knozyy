package com.okul.service;

import com.okul.dao.CourseDao;
import com.okul.dao.EnrollmentDao;
import com.okul.model.Course;
import com.okul.model.Enrollment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class EnrollmentService {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentService.class);

    private final EnrollmentDao enrollmentDao;
    private final CourseDao courseDao;

    public EnrollmentService() {
        this(new EnrollmentDao(), new CourseDao());
    }

    public EnrollmentService(EnrollmentDao enrollmentDao, CourseDao courseDao) {
        this.enrollmentDao = enrollmentDao;
        this.courseDao = courseDao;
    }

    public void enroll(String courseId, String studentId) {
        Course course = courseDao.findById(courseId);
        if (course == null) {
            throw new RuntimeException("Ders bulunamadi.");
        }
        if (enrollmentDao.exists(courseId, studentId)) {
            throw new RuntimeException("Bu derse zaten kayitlisiniz.");
        }
        int enrolled = enrollmentDao.findByCourseId(courseId).size();
        if (enrolled >= course.getQuota()) {
            throw new RuntimeException("Kontenjan dolu! Bu derse kayit yapilamaz.");
        }
        enrollmentDao.save(new Enrollment(courseId, studentId, LocalDate.now().toString()));
        log.info("Kayit yapildi -> ogrenci={} ders={}", studentId, courseId);
    }

    public List<Enrollment> getCourseEnrollments(String courseId) {
        return enrollmentDao.findByCourseId(courseId);
    }

    public List<Enrollment> getStudentEnrollments(String studentId) {
        return enrollmentDao.findByStudentId(studentId);
    }
}
