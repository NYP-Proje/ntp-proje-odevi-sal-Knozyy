package com.okul.dao;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.okul.model.Course;
import com.okul.util.FirebaseConfig;

import java.util.ArrayList;
import java.util.List;

public class CourseDao {

    private static final String COLLECTION = "courses";

    private Firestore db() {
        return FirebaseConfig.getDb();
    }

    public void save(Course course) {
        try {
            DocumentReference ref = db().collection(COLLECTION).document();
            course.setId(ref.getId());
            ref.set(course).get();
        } catch (Exception e) {
            throw new RuntimeException("Ders kaydedilemedi.", e);
        }
    }

    public Course findById(String id) {
        try {
            DocumentSnapshot snap = db().collection(COLLECTION).document(id).get().get();
            return snap.exists() ? snap.toObject(Course.class) : null;
        } catch (Exception e) {
            throw new RuntimeException("Ders bulunamadi.", e);
        }
    }

    public List<Course> findAll() {
        try {
            List<QueryDocumentSnapshot> docs = db().collection(COLLECTION).get().get().getDocuments();
            List<Course> list = new ArrayList<>();
            for (QueryDocumentSnapshot d : docs) {
                list.add(d.toObject(Course.class));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Dersler listelenemedi.", e);
        }
    }

    public List<Course> findByTeacherId(String teacherId) {
        try {
            List<QueryDocumentSnapshot> docs = db().collection(COLLECTION)
                    .whereEqualTo("teacherId", teacherId).get().get().getDocuments();
            List<Course> list = new ArrayList<>();
            for (QueryDocumentSnapshot d : docs) {
                list.add(d.toObject(Course.class));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Ogretmenin dersleri listelenemedi.", e);
        }
    }

    public void deleteById(String id) {
        try {
            db().collection(COLLECTION).document(id).delete().get();
        } catch (Exception e) {
            throw new RuntimeException("Ders silinemedi.", e);
        }
    }
}
