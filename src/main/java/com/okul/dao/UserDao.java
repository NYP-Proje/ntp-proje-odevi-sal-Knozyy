package com.okul.dao;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.okul.model.Role;
import com.okul.model.User;
import com.okul.util.FirebaseConfig;

import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static final String COLLECTION = "users";

    private Firestore db() {
        return FirebaseConfig.getDb();
    }

    public void save(User user) {
        try {
            DocumentReference ref = db().collection(COLLECTION).document();
            user.setId(ref.getId());
            ref.set(user).get();
        } catch (Exception e) {
            throw new RuntimeException("Kullanici kaydedilemedi.", e);
        }
    }

    public User findById(String id) {
        try {
            DocumentSnapshot snap = db().collection(COLLECTION).document(id).get().get();
            return snap.exists() ? snap.toObject(User.class) : null;
        } catch (Exception e) {
            throw new RuntimeException("Kullanici bulunamadi.", e);
        }
    }

    public User findByEmail(String email) {
        try {
            List<QueryDocumentSnapshot> docs = db().collection(COLLECTION)
                    .whereEqualTo("email", email).get().get().getDocuments();
            return docs.isEmpty() ? null : docs.get(0).toObject(User.class);
        } catch (Exception e) {
            throw new RuntimeException("Kullanici aranamadi.", e);
        }
    }

    public List<User> findByRole(Role role) {
        try {
            List<QueryDocumentSnapshot> docs = db().collection(COLLECTION)
                    .whereEqualTo("role", role.name()).get().get().getDocuments();
            List<User> list = new ArrayList<>();
            for (QueryDocumentSnapshot d : docs) {
                list.add(d.toObject(User.class));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Kullanicilar listelenemedi.", e);
        }
    }

    public void deleteById(String id) {
        try {
            db().collection(COLLECTION).document(id).delete().get();
        } catch (Exception e) {
            throw new RuntimeException("Kullanici silinemedi.", e);
        }
    }
}
