[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/DP_mhnuQ)

# Okul Yönetim Sistemi

Java + JavaFX ile geliştirilmiş, **Firebase Firestore** (online veritabanı) kullanan masaüstü okul yönetim uygulaması. Bitirme projesi.

3 rol vardır: **Admin (Yönetici)**, **Öğretmen**, **Öğrenci**.

---

##  Kullanılan Teknolojiler
- **Java 25** (Temurin) — kod Java 21 uyumlu hedefle derlenir, JDK 25'te çalışır
- **JavaFX 25** (arayüz, FXML + Controller)
- **Firebase Admin SDK** (Firestore – online NoSQL veritabanı)
- **jBCrypt** (parola hashleme)
- **SLF4J + Logback** (loglama)
- **JUnit 5 + Mockito** (testler)
- **Maven** (bağımlılık + derleme)

##  Mimari (Katmanlı)
```
UI (FXML) → Controller → Service (iş mantığı) → DAO (Firestore erişimi) → Model
```
- `model/` : Veri sınıfları (User, Course, Enrollment, Assignment, Submission, Grade, Announcement)
- `dao/`   : Firestore okuma/yazma
- `service/` : İş kuralları (kontenjan, parola, not, GANO)
- `controller/` : Ekran (FXML) controller'ları
- `util/` : Yardımcılar (Firebase bağlantısı, parola, hata yönetimi, dosya, oturum, not hesabı)

---

##  Kurulum

### 1) Gereksinimler
- **JDK 25** (kurulu: `java -version` ile kontrol et)
- **IntelliJ IDEA (Community – ücretsiz)** öneriyoruz. Maven'ı IntelliJ otomatik halleder.
  (Alternatif: Maven'ı elle kurup komut satırından çalıştırabilirsin.)

### 2) Firebase Projesi Oluştur
Uygulama, online Firestore veritabanına bağlanır. Aşağıdaki adımlar **bir kez** yapılır:

1. https://console.firebase.google.com adresine git, **"Proje ekle"** ile yeni proje oluştur.
2. Sol menüden **Build → Firestore Database → Create database** de. **Test mode** (test modunda başlat) seç, bir bölge seç.
3. Sol üstteki  **Project settings → Service accounts** sekmesine gir.
4. **Generate new private key** butonuna bas → bir `.json` dosyası iner.
5. Bu dosyayı **`serviceAccountKey.json`** adıyla **projenin kök klasörüne** (pom.xml ile aynı yere) koy.


### 3) Çalıştırma

**IntelliJ ile (önerilen):**
1. IntelliJ'i aç → **Open** → bu klasörü seç (pom.xml'i tanıyıp Maven projesi olarak açar).
2. Bağımlılıklar inene kadar bekle (ilk seferde biraz sürer).
3. Sağdaki **Maven** panelini aç → `okul-yonetim → Plugins → javafx → javafx:run` üstüne çift tıkla.

**Komut satırı ile (Maven kuruluysa):**
```bash
mvn javafx:run
```

### 4) İlk Giriş
Uygulama ilk açıldığında otomatik bir yönetici hesabı oluşturur:
```
E-posta: admin@okul.com
Parola : 1234
```
Bu admin ile girip öğretmen/öğrenci hesapları ve ders oluşturabilirsin.

---

##  Demo Akışı (jüri sunumu)
1. **Admin** ile giriş yap.
2. **Hesap Yönetimi:** bir öğretmen + bir öğrenci hesabı oluştur.
3. **Ders Yönetimi:** öğretmeni seçip kontenjanlı bir ders aç.
4. **Öğrenci Atama:** (istersen) öğrenciyi derse ata. Veya öğrenci kendi kaydolur.
5. **Öğretmen** ile giriş → dersi yükle → duyuru yayınla, ödev oluştur.
6. **Öğrenci** ile giriş → derse kaydol → ödev dosyası (PDF/DOCX) yükle.
7. **Öğretmen** → teslimleri gör/aç, not gir, başarı sıralamasını göster.
8. **Öğrenci** → "Notlarım / Transkript" → not ve **GANO** görüntüle.


