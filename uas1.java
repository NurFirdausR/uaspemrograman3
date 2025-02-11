import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class uas1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String fileName = "output.txt";

        System.out.print("Input digit terakhir NPM Anda: ");
        int digitNPM = scanner.nextInt();

        int jumlahData = (digitNPM % 2 == 0) ? 4 : 3;  
        scanner.nextLine(); 
        try (FileWriter writer = new FileWriter(fileName)) {
            for (int i = 1; i <= jumlahData; i++) {
                System.out.println("\nData Pasien " + i + ":");

                
                System.out.print("ID Pasien: ");
                String idPasien = scanner.nextLine().trim();

               
                System.out.print("Nama Pasien: ");
                String namaPasien = scanner.nextLine().trim();

               
                System.out.print("Kode Kamar (1/2/3): ");
                int kodeKamar = Integer.parseInt(scanner.nextLine().trim());

                
                System.out.print("Lama Inap (dalam hari): ");
                int lamaInap = Integer.parseInt(scanner.nextLine().trim());

               
                String data = String.format("%s_%d_%02d_%s%n", idPasien, kodeKamar, lamaInap, namaPasien);

               
                writer.write(data);
            }

            System.out.println("\nSelesai menulis ke file: " + fileName);

        } catch (IOException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        }

        scanner.close();
    }
}
