# Proje Dokümantasyonu — Okul Yönetim Sistemi

Bu dosya teknik rapor için temel diyagramları ve açıklamaları içerir.
Diyagramlar **Mermaid** ile yazılmıştır; [mermaid.live](https://mermaid.live) sitesine yapıştırıp
PNG olarak indirip rapora ekleyebilirsin.

---

## 1. Sistem Mimarisi (Katmanlı Mimari)

```mermaid
flowchart TD
    UI["UI Katmani (FXML ekranlari)"] --> C["Controller Katmani"]
    C --> S["Service Katmani (is mantigi)"]
    S --> D["DAO Katmani (Firestore erisimi)"]
    D --> FB[("Firebase Firestore - online")]
    S --> M["Model (veri siniflari)"]
    C --> U["Util (hata, parola, dosya, oturum, not hesabi)"]
```

Her katmanın tek bir sorumluluğu vardır ve yalnızca bir alttaki katmanla konuşur.
Bu sayede kod test edilebilir ve bakımı kolaydır (DAO ayrı olduğu için Service testlerinde Firestore mock'lanabilir).

---

## 2. Veritabanı / Veri Modeli (ER Diyagramı)

Firestore NoSQL bir veritabanıdır; ancak entiteler ve ilişkiler ilişkisel mantıkla,
**tekrarsız** ve **ID ile bağlı** (normalizasyon prensibi) tasarlanmıştır.

```mermaid
erDiagram
    USERS ||--o{ COURSES : "verir (ogretmen)"
    USERS ||--o{ ENROLLMENTS : "kaydolur (ogrenci)"
    COURSES ||--o{ ENROLLMENTS : "icerir"
    COURSES ||--o{ ASSIGNMENTS : "icerir"
    ASSIGNMENTS ||--o{ SUBMISSIONS : "alir"
    USERS ||--o{ SUBMISSIONS : "teslim eder (ogrenci)"
    COURSES ||--o{ GRADES : "icerir"
    USERS ||--o{ GRADES : "alir (ogrenci)"
    COURSES ||--o{ ANNOUNCEMENTS : "icerir"
    USERS ||--o{ ANNOUNCEMENTS : "yayinlar (ogretmen)"

    USERS {
        string id
        string firstName
        string lastName
        string email
        string passwordHash
        string role
    }
    COURSES {
        string id
        string code
        string name
        string teacherId
        int quota
        int credit
        string term
    }
    ENROLLMENTS {
        string id
        string courseId
        string studentId
        string enrollDate
    }
    ASSIGNMENTS {
        string id
        string courseId
        string title
        string description
        string dueDate
    }
    SUBMISSIONS {
        string id
        string assignmentId
        string studentId
        string filePath
        string submitDate
    }
    GRADES {
        string id
        string courseId
        string studentId
        double score
        string letterGrade
    }
    ANNOUNCEMENTS {
        string id
        string courseId
        string teacherId
        string title
        string content
        string date
    }
```

**Koleksiyonlar (7):** users, courses, enrollments, assignments, submissions, grades, announcements.

---

## 3. UML Sınıf Diyagramı (sadeleştirilmiş)

```mermaid
classDiagram
    class User {
        +String id
        +String firstName
        +String lastName
        +String email
        +String passwordHash
        +Role role
        +fullName() String
    }
    class Course {
        +String id
        +String code
        +String name
        +String teacherId
        +int quota
        +int credit
        +String term
    }
    class Enrollment {
        +String courseId
        +String studentId
        +String enrollDate
    }
    class Grade {
        +String courseId
        +String studentId
        +double score
        +String letterGrade
    }

    class EnrollmentService {
        +enroll(courseId, studentId)
    }
    class GradeService {
        +assignGrade(courseId, studentId, score)
        +getTranscript(studentId)
        +calculateGpa(studentId)
        +getCourseRanking(courseId)
    }
    class GradeCalculator {
        +scoreToLetter(score) String
        +letterToGpa(letter) double
        +weightedGpa(rows) double
    }

    EnrollmentService ..> Course
    EnrollmentService ..> Enrollment
    GradeService ..> Grade
    GradeService ..> GradeCalculator
    User --> Role
```

---

## 4. Kullanım Senaryosu (Use Case)

```mermaid
flowchart LR
    Admin([Admin])
    Teacher([Ogretmen])
    Student([Ogrenci])

    Admin --- UC1[Hesap olustur/sil]
    Admin --- UC2[Ders ac/sil + kontenjan]
    Admin --- UC3[Ogrenciyi derse ata]

    Teacher --- UC4[Duyuru yayinla]
    Teacher --- UC5[Odev olustur]
    Teacher --- UC6[Teslimleri gor/indir]
    Teacher --- UC7[Not gir]
    Teacher --- UC8[Basari siralamasi]

    Student --- UC9[Derse kaydol]
    Student --- UC10[Odev dosyasi yukle]
    Student --- UC11[Duyurulari gor]
    Student --- UC12[Transkript / GANO gor]
```

---

## 5. Algoritma — GANO (Genel Not Ortalaması) Hesabı

Projenin "Algoritma / Yenilikçi Yaklaşım" kısmı `GradeCalculator` sınıfındadır.

**Adımlar:**
1. Sayısal not (0–100) harf notuna çevrilir: 90+ → AA, 80+ → BA, 70+ → BB, 65+ → CB, 60+ → CC, 55+ → DC, 50+ → DD, altı → FF.
2. Harf notu katsayıya çevrilir: AA=4.0, BA=3.5, BB=3.0, CB=2.5, CC=2.0, DC=1.5, DD=1.0, FF=0.0.
3. Kredi ağırlıklı ortalama:

```
GANO = toplam(katsayi * kredi) / toplam(kredi)
```

**Örnek:** BIL101 (3 kredi, AA=4.0) ve MAT101 (4 kredi, BB=3.0):
`(4.0*3 + 3.0*4) / (3+4) = 24 / 7 = 3.43`

Ayrıca **kontenjan kontrolü** (dolu derse kayıt engelleme) ve **başarı sıralaması**
(ders öğrencilerini nota göre büyükten küçüğe sıralama) algoritmaları da uygulanmıştır.

---

## 6. Güvenlik
- Parolalar düz metin tutulmaz; **BCrypt** ile hash'lenir (`PasswordUtil`).
- **Rol bazlı erişim:** her kullanıcı yalnızca kendi rolünün panelini görür (`Session` + giriş yönlendirmesi).
- Firebase'e bağlantı HTTPS üzerinden şifreli yapılır.
- Firebase servis anahtarı (`serviceAccountKey.json`) repoya konmaz (`.gitignore`).
