package com.okul.dao;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.okul.model.Enrollment;
import com.okul.util.FirebaseConfig;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentDao {

    private static final String COLLECTION = "enrollments";

    private Firestore db() {
        return FirebaseConfig.getDb();
    }

    public void save(Enrollment enrollment) {
        try {
            DocumentReference ref = db().collection(COLLECTION).document();
            enrollment.setId(ref.getId());
            ref.set(enrollment).get();
        } catch (Exception e) {
            throw new RuntimeException("Kayit olusturulamadi.", e);
        }
    }

    public List<Enrollment> findByCourseId(String courseId) {
        try {
            List<QueryDocumentSnapshot> docs = db().collection(COLLECTION)
                    .whereEqualTo("courseId", courseId).get().get().getDocuments();
            List<Enrollment> list = new ArrayList<>();
            for (QueryDocumentSnapshot d : docs) {
                list.add(d.toObject(Enrollment.class));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Ders kayitlari listelenemedi.", e);
        }
    }

    public List<Enrollment> findByStudentId(String studentId) {
        try {
            List<QueryDocumentSnapshot> docs = db().collection(COLLECTION)
                    .whereEqualTo("studentId", studentId).get().get().getDocuments();
            List<Enrollment> list = new ArrayList<>();
            for (QueryDocumentSnapshot d : docs) {
                list.add(d.toObject(Enrollment.class));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Ogrenci kayitlari listelenemedi.", e);
        }
    }

    public boolean exists(String courseId, String studentId) {
        for (Enrollment e : findByStudentId(studentId)) {
            if (e.getCourseId().equals(courseId)) {
                return true;
            }
        }
        return false;
    }
}
