import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class uas2 {
    public static void main(String[] args) {
        String fileName = "output.txt";

        int[] biayaPerHari = {0, 100000, 250000, 500000};

        System.out.println("-------------------------------------------------------------");
        System.out.printf("%-10s %-15s %-10s %-10s %-15s%n", 
                          "ID Pas", "Nama Pasien", "Kode Kamar", "Lama Inap", "Total Biaya");
        System.out.println("-------------------------------------------------------------");

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split("_");

                if (data.length != 4) {
                    System.out.println("Format data salah: " + line);
                    continue;
                }

                try {
                    String idPasien = data[0].trim();
                    int kodeKamar = Integer.parseInt(data[1].trim());
                    int lamaInap = Integer.parseInt(data[2].trim());
                    String namaPasien = data[3].trim();

                    if (kodeKamar < 1 || kodeKamar > 3) {
                        System.out.println("Kode kamar tidak valid: " + kodeKamar);
                        continue;
                    }

                    int totalBiaya = lamaInap * biayaPerHari[kodeKamar];

                    System.out.printf("%-10s %-15s %-10d %-10d Rp%,10d%n", 
                                      idPasien, namaPasien, kodeKamar, lamaInap, totalBiaya);
                } catch (NumberFormatException e) {
                    System.out.println("Kesalahan format angka di baris: " + line);
                }
            }

            System.out.println("-------------------------------------------------------------");
            System.out.println("Ketentuan:");
            System.out.println("Kode Kamar   Biaya/hari");
            System.out.println("1           Rp 100.000,-");
            System.out.println("2           Rp 250.000,-");
            System.out.println("3           Rp 500.000,-");

        } catch (IOException e) {
            System.out.println("File tidak ditemukan atau terjadi kesalahan: " + e.getMessage());
        }
    }
}
