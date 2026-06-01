package com.okul.service;

import com.okul.dao.CourseDao;
import com.okul.dao.EnrollmentDao;
import com.okul.model.Course;

import java.util.List;

public class CourseService {

    private final CourseDao courseDao;
    private final EnrollmentDao enrollmentDao;

    public CourseService() {
        this(new CourseDao(), new EnrollmentDao());
    }

    public CourseService(CourseDao courseDao, EnrollmentDao enrollmentDao) {
        this.courseDao = courseDao;
        this.enrollmentDao = enrollmentDao;
    }

    public void createCourse(String code, String name, String teacherId,
                             int quota, int credit, String term) {
        if (code == null || code.isBlank()) {
            throw new RuntimeException("Ders kodu bos olamaz.");
        }
        if (quota <= 0) {
            throw new RuntimeException("Kontenjan 0'dan buyuk olmali.");
        }
        if (credit <= 0) {
            throw new RuntimeException("Kredi 0'dan buyuk olmali.");
        }
        courseDao.save(new Course(code, name, teacherId, quota, credit, term));
    }

    public List<Course> getAllCourses() {
        return courseDao.findAll();
    }

    public List<Course> getCoursesByTeacher(String teacherId) {
        return courseDao.findByTeacherId(teacherId);
    }

    public Course getById(String id) {
        return courseDao.findById(id);
    }

    public void deleteCourse(String id) {
        courseDao.deleteById(id);
    }

    public int remainingQuota(Course course) {
        int enrolled = enrollmentDao.findByCourseId(course.getId()).size();
        return course.getQuota() - enrolled;
    }
}
