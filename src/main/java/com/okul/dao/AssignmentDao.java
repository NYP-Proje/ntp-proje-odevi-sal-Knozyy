package com.okul.dao;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.okul.model.Assignment;
import com.okul.util.FirebaseConfig;

import java.util.ArrayList;
import java.util.List;

public class AssignmentDao {

    private static final String COLLECTION = "assignments";

    private Firestore db() {
        return FirebaseConfig.getDb();
    }

    public void save(Assignment assignment) {
        try {
            DocumentReference ref = db().collection(COLLECTION).document();
            assignment.setId(ref.getId());
            ref.set(assignment).get();
        } catch (Exception e) {
            throw new RuntimeException("Odev kaydedilemedi.", e);
        }
    }

    public Assignment findById(String id) {
        try {
            DocumentSnapshot snap = db().collection(COLLECTION).document(id).get().get();
            return snap.exists() ? snap.toObject(Assignment.class) : null;
        } catch (Exception e) {
            throw new RuntimeException("Odev bulunamadi.", e);
        }
    }

    public List<Assignment> findByCourseId(String courseId) {
        try {
            List<QueryDocumentSnapshot> docs = db().collection(COLLECTION)
                    .whereEqualTo("courseId", courseId).get().get().getDocuments();
            List<Assignment> list = new ArrayList<>();
            for (QueryDocumentSnapshot d : docs) {
                list.add(d.toObject(Assignment.class));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Odevler listelenemedi.", e);
        }
    }
}
