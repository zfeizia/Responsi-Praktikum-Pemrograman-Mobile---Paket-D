# Digimon Explorer

Aplikasi Android untuk menjelajahi daftar Digimon beserta level, attribute, dan type-nya, dengan data dari [Digi-API](https://digi-api.com/). Dibuat sebagai tugas Responsi Praktikum Pemrograman Mobile (Paket D) menggunakan Kotlin, Jetpack Compose, Material Design 3, dan arsitektur MVVM.

| | |
|---|---|
| **Nama** | [Zainab Feizia] |
| **NIM** | [H1D024097] |
| **Video penjelasan kode** | [https://youtu.be/8cSpqPEfDAo] |

---

## Screenshot

---

## Fitur

- Menampilkan daftar Digimon dalam grid dua kolom (`LazyVerticalGrid`) berisi gambar, nama, nomor, level, attribute, dan type.
- Halaman detail Digimon: gambar besar, chip Level/Attribute/Type, tanggal rilis, deskripsi, dan daftar skill.
- Warna kartu dan halaman detail menyesuaikan attribute Digimon (Vaccine, Data, Virus, Free, Variable).
- Penanganan state UI: **Loading**, **Success** (data), dan **Error** dengan tombol **Coba Lagi**.
- Custom theme (light dan dark) serta custom typography.
- Navigasi dua halaman: Home dan Detail.

---

## Teknologi yang Digunakan

| Komponen | Versi / Keterangan |
|---|---|
| Bahasa | Kotlin 2.2.10 |
| UI | Jetpack Compose (BOM 2026.02.01) dan Material Design 3 |
| Navigasi | Navigation Compose 2.9.0 |
| Arsitektur | MVVM (Model, View, ViewModel, Repository) |
| State | `StateFlow` + `collectAsState()` |
| Asynchronous | Kotlin Coroutines (`viewModelScope`, `async`, `awaitAll`) |
| Networking | Retrofit 2.11.0 + Gson Converter |
| Pemuatan gambar | Coil 2.6.0 (`AsyncImage`) |
| Android Gradle Plugin | 9.2.1 |
| SDK | minSdk 29, targetSdk 36, compileSdk 37 |

---

## Arsitektur MVVM

Alur data pada aplikasi:

| Layer | Class | Tanggung jawab |
|---|---|---|
| **Model** | `DigimonDto`, `Digimon`, `DigimonSkill` | `DigimonDto` merepresentasikan JSON dari API. `Digimon` adalah model bersih untuk UI. Fungsi `toDigimon()` memetakan DTO ke model UI. |
| **View** | `HomeScreen`, `DetailScreen` (Compose) | Menggambar UI berdasarkan state dan meneruskan event pengguna ke atas lewat lambda. Tidak ada logika bisnis. |
| **ViewModel** | `DigimonListViewModel`, `DigimonDetailViewModel` | Menyimpan UI state dalam `StateFlow`, memanggil Repository di `viewModelScope`, dan menangani error. |
| **Repository** | `DigimonRepository` | Satu-satunya pintu ke sumber data. ViewModel tidak mengetahui detail Retrofit. |

---

## Struktur Project

```text
com.pemmob.digimonexplorer
├── MainActivity.kt                    # Entry point, NavHost (2 rute)
├── data
│   ├── model
│   │   ├── DigimonDto.kt              # Model response API
│   │   └── Digimon.kt                 # Model UI + mapper toDigimon()
│   └── repository
│       └── DigimonRepository.kt       # Sumber data tunggal
├── network
│   └── ApiInterface.kt                # Endpoint Retrofit + ApiClient
├── util
│   └── DigimonConstants.kt            # BASE_URL dan PAGE_SIZE
└── ui
    ├── screen
    │   ├── HomeScreen.kt              # Daftar Digimon (LazyVerticalGrid)
    │   └── DetailScreen.kt            # Detail Digimon
    ├── theme
    │   ├── Color.kt                   # Palet warna + attributeColor()
    │   ├── Theme.kt                   # DigimonTheme (light dan dark)
    │   └── Type.kt                    # DigimonTypography
    └── viewmodel
        ├── DigimonListViewModel.kt    # State + logika daftar
        └── DigimonDetailViewModel.kt  # State + logika detail
```

---

## Penjelasan Teknis

1. **Networking.** `ApiInterface` memiliki dua endpoint `suspend`: daftar Digimon (`api/v1/digimon`) dan detail (`api/v1/digimon/{id}`). `ApiClient` membuat Retrofit satu kali (`by lazy`). `BASE_URL` dan `PAGE_SIZE` disimpan di `DigimonConstants`.
2. **Model.** `DigimonDto` mengikuti bentuk JSON API dengan field *nullable*. `Digimon` adalah model bersih untuk UI, dan fungsi `toDigimon()` memetakan DTO ke model tersebut.
3. **Repository.** Endpoint daftar hanya berisi id, nama, dan gambar, sedangkan level, attribute, dan type ada di endpoint detail. Karena itu `DigimonRepository` mengambil daftar, lalu detail tiap Digimon secara paralel dengan `async` dan `awaitAll`.
4. **ViewModel dan State.** State dimodelkan dengan `sealed interface` (`Loading`, `Success`, `Error`) dan disimpan di `MutableStateFlow` private yang diekspos sebagai `StateFlow`. Data dimuat di `viewModelScope`.
5. **Theme.** `DigimonTheme` memuat skema warna terang dan gelap serta `DigimonTypography`. Fungsi `attributeColor()` memberi warna kartu sesuai attribute.
6. **UI dan Navigasi.** Screen mengamati state dengan `collectAsState()` dan menampilkan satu dari tiga kondisi lewat `when`. Daftar memakai `LazyVerticalGrid`. `NavHost` memiliki dua rute: `home` dan `detail/{digimonId}`.

## Cara Menjalankan

**Prasyarat:** Android Studio versi terbaru, JDK 11 atau lebih baru, dan koneksi internet.

1. Clone repository ini:
   ```bash
   git clone [url repository]
   ```
2. Buka folder project dengan Android Studio dan tunggu proses **Gradle Sync** selesai.
3. Siapkan emulator atau perangkat fisik dengan Android 10 (API 29) ke atas.
4. Jalankan aplikasi dengan tombol **Run**. Pastikan perangkat terhubung ke internet.

---

