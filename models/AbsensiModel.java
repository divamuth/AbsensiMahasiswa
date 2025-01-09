package models;

public class AbsensiModel {
    private String nim;
    private String tanggal;
    private String status;

    public AbsensiModel() {
        // Default constructor
    }

    // Getters
    public String getNim() { return nim; }
    public String getTanggal() { return tanggal; }
    public String getStatus() { return status; }

    // Setters
    public void setNim(String nim) { this.nim = nim; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public void setStatus(String status) { this.status = status; }
}
