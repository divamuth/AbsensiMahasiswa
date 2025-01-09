package models;

public class MahasiswaModel {
    private String nim;
    private String nama;
    private String kelas;

    public MahasiswaModel() {
        // Default constructor
    }

    public MahasiswaModel(String nim, String nama, String kelas) {
        this.nim = nim;
        this.nama = nama;
        this.kelas = kelas;
    }

    // Getters
    public String getNim() { return nim; }
    public String getNama() { return nama; }
    public String getKelas() { return kelas; }

    // Setters
    public void setNim(String nim) { this.nim = nim; }
    public void setNama(String nama) { this.nama = nama; }
    public void setKelas(String kelas) { this.kelas = kelas; }
}
