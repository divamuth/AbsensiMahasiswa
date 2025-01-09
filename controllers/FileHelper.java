package controllers;

import models.*;
import java.io.*;
import java.util.*;

public class FileHelper {
    private static final String FILE_PATH = "data/mahasiswa.txt";
    private static final String ABSENSI_FILE = "data/absensi.txt";

    // Membaca data mahasiswa dari file
    public static List<MahasiswaModel> getMahasiswa() {
        List<MahasiswaModel> mahasiswaList = new ArrayList<>();
        ensureDirectoryExists();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 3) {
                    MahasiswaModel mahasiswa = new MahasiswaModel();
                    mahasiswa.setNim(parts[0]);
                    mahasiswa.setNama(parts[1]);
                    mahasiswa.setKelas(parts[2]);
                    mahasiswaList.add(mahasiswa);
                } else {
                    System.out.println("Data mahasiswa tidak valid: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File mahasiswa.txt tidak ditemukan. Membuat list kosong...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mahasiswaList;
    }

    // Menyimpan data mahasiswa ke file
    public static void saveMahasiswa(List<MahasiswaModel> mahasiswaList) {
        ensureDirectoryExists();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (MahasiswaModel m : mahasiswaList) {
                bw.write(m.getNim() + ", " + m.getNama() + ", " + m.getKelas());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<AbsensiModel> getAbsensi() {
        List<AbsensiModel> list = new ArrayList<>();
        ensureDirectoryExists();
        try (BufferedReader reader = new BufferedReader(new FileReader(ABSENSI_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    AbsensiModel a = new AbsensiModel();
                    a.setNim(data[0]);
                    a.setTanggal(data[1]);
                    a.setStatus(data[2]);
                    list.add(a);
                } else {
                    System.out.println("Data absensi tidak valid: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File absensi.txt tidak ditemukan. Membuat list kosong...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void saveAbsensi(List<AbsensiModel> list) {
        ensureDirectoryExists();
        try (PrintWriter writer = new PrintWriter(new FileWriter(ABSENSI_FILE))) {
            for (AbsensiModel a : list) {
                writer.println(String.format("%s,%s,%s",
                    a.getNim(),
                    a.getTanggal(),
                    a.getStatus()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void ensureDirectoryExists() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    // Implementasi method getMahasiswaByKeyword
    public static List<MahasiswaModel> getMahasiswaByKeyword(String keyword) {
        List<MahasiswaModel> filteredList = new ArrayList<>();
        List<MahasiswaModel> allMahasiswa = getMahasiswa();
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return allMahasiswa;
        }

        String searchKeyword = keyword.toLowerCase().trim();
        
        for (MahasiswaModel mahasiswa : allMahasiswa) {
            if (mahasiswa.getNim().toLowerCase().contains(searchKeyword) ||
                mahasiswa.getNama().toLowerCase().contains(searchKeyword) ||
                mahasiswa.getKelas().toLowerCase().contains(searchKeyword)) {
                filteredList.add(mahasiswa);
            }
        }
        
        return filteredList;
    }
}
