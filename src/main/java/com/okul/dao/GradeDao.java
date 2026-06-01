package com.okul.dao;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.okul.model.Grade;
import com.okul.util.FirebaseConfig;

import java.util.ArrayList;
import java.util.List;

public class GradeDao {

    private static final String COLLECTION = "grades";

    private Firestore db() {
        return FirebaseConfig.getDb();
    }

    public void save(Grade grade) {
        try {
            DocumentReference ref = db().collection(COLLECTION).document();
            grade.setId(ref.getId());
            ref.set(grade).get();
        } catch (Exception e) {
            throw new RuntimeException("Not kaydedilemedi.", e);
        }
    }

    public void update(Grade grade) {
        try {
            db().collection(COLLECTION).document(grade.getId()).set(grade).get();
        } catch (Exception e) {
            throw new RuntimeException("Not guncellenemedi.", e);
        }
    }

    public List<Grade> findByStudentId(String studentId) {
        try {
            List<QueryDocumentSnapshot> docs = db().collection(COLLECTION)
                    .whereEqualTo("studentId", studentId).get().get().getDocuments();
            List<Grade> list = new ArrayList<>();
            for (QueryDocumentSnapshot d : docs) {
                list.add(d.toObject(Grade.class));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Notlar listelenemedi.", e);
        }
    }

    public List<Grade> findByCourseId(String courseId) {
        try {
            List<QueryDocumentSnapshot> docs = db().collection(COLLECTION)
                    .whereEqualTo("courseId", courseId).get().get().getDocuments();
            List<Grade> list = new ArrayList<>();
            for (QueryDocumentSnapshot d : docs) {
                list.add(d.toObject(Grade.class));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Ders notlari listelenemedi.", e);
        }
    }

    public Grade findByCourseAndStudent(String courseId, String studentId) {
        for (Grade g : findByStudentId(studentId)) {
            if (g.getCourseId().equals(courseId)) {
                return g;
            }
        }
        return null;
    }
}
