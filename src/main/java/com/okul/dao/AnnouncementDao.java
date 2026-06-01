package com.okul.dao;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.okul.model.Announcement;
import com.okul.util.FirebaseConfig;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementDao {

    private static final String COLLECTION = "announcements";

    private Firestore db() {
        return FirebaseConfig.getDb();
    }

    public void save(Announcement announcement) {
        try {
            DocumentReference ref = db().collection(COLLECTION).document();
            announcement.setId(ref.getId());
            ref.set(announcement).get();
        } catch (Exception e) {
            throw new RuntimeException("Duyuru kaydedilemedi.", e);
        }
    }

    public List<Announcement> findByCourseId(String courseId) {
        try {
            List<QueryDocumentSnapshot> docs = db().collection(COLLECTION)
                    .whereEqualTo("courseId", courseId).get().get().getDocuments();
            List<Announcement> list = new ArrayList<>();
            for (QueryDocumentSnapshot d : docs) {
                list.add(d.toObject(Announcement.class));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Duyurular listelenemedi.", e);
        }
    }
}
