package com.okul.service;

import com.okul.dao.AnnouncementDao;
import com.okul.model.Announcement;

import java.time.LocalDate;
import java.util.List;

public class AnnouncementService {

    private final AnnouncementDao announcementDao;

    public AnnouncementService() {
        this(new AnnouncementDao());
    }

    public AnnouncementService(AnnouncementDao announcementDao) {
        this.announcementDao = announcementDao;
    }

    public void postAnnouncement(String courseId, String teacherId, String title, String content) {
        if (title == null || title.isBlank()) {
            throw new RuntimeException("Duyuru basligi bos olamaz.");
        }
        announcementDao.save(new Announcement(courseId, teacherId, title, content,
                LocalDate.now().toString()));
    }

    public List<Announcement> getByCourse(String courseId) {
        return announcementDao.findByCourseId(courseId);
    }
}
