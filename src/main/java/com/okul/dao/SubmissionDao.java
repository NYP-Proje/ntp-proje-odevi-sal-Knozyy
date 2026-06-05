package com.okul.dao;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.okul.model.Submission;
import com.okul.util.FirebaseConfig;

import java.util.ArrayList;
import java.util.List;

public class SubmissionDao {

    private static final String COLLECTION = "submissions";

    private Firestore db() {
        return FirebaseConfig.getDb();
    }

    public void save(Submission submission) {
        try {
            DocumentReference ref = db().collection(COLLECTION).document();
            submission.setId(ref.getId());
            ref.set(submission).get();
        } catch (Exception e) {
            throw new RuntimeException("Teslim kaydedilemedi.", e);
        }
    }

    public List<Submission> findByAssignmentId(String assignmentId) {
        try {
            List<QueryDocumentSnapshot> docs = db().collection(COLLECTION)
                    .whereEqualTo("assignmentId", assignmentId).get().get().getDocuments();
            List<Submission> list = new ArrayList<>();
            for (QueryDocumentSnapshot d : docs) {
                list.add(d.toObject(Submission.class));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Teslimler listelenemedi.", e);
        }
    }
}
