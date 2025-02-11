import java.sql.*;
import java.util.Scanner;
import java.util.Locale;
import java.text.NumberFormat;

public class RentalTransaction {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url1 = "jdbc:mysql://localhost:3306/ReihanSyabani?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "Bismillah@123";
            Connection conn = DriverManager.getConnection(url1, user, password);
            System.out.println("-- Transaksi --");
            String countSewa = "SELECT COUNT(*) FROM sewa";
            int rowCount = 0;

            NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            try (PreparedStatement countStmt = conn.prepareStatement(countSewa);
                    ResultSet resultSet = countStmt.executeQuery()) {

                if (resultSet.next()) {
                    rowCount = resultSet.getInt(1);
                }
            }

            String transactionId = String.format("%03d", rowCount + 1);
            System.out.println("-- Nomor Sewa   "+ transactionId +" : --");
            System.out.println("------------------------------");

            System.out.print("Input ID Pelanggan: ");
            String customerId = scanner.nextLine();

            String customerQuery = "SELECT nama, no_Telp FROM pelanggan WHERE id = ?";
            try (PreparedStatement customerStmt = conn.prepareStatement(customerQuery)) {
                customerStmt.setString(1, customerId);
                ResultSet customerRs = customerStmt.executeQuery();

                if (customerRs.next()) {
                    System.out.println(">> " + customerRs.getString("nama"));
                    System.out.println(">> " + customerRs.getString("no_Telp"));
                } else {
                    System.out.println("Pelanggan tidak ditemukan.");
                    return;
                }
            }
            System.out.println("------------------------------");

            System.out.print("Input Kode Mobil: ");
            String carCode = scanner.nextLine();

            String carQuery = "SELECT merek, warna, kapasitas, harga_sewa, satuan FROM mobil WHERE kode = ?";
            try (PreparedStatement carStmt = conn.prepareStatement(carQuery)) {
                carStmt.setString(1, carCode);
                ResultSet carRs = carStmt.executeQuery();

                if (carRs.next()) {
                    int hargaSewa = carRs.getInt("harga_sewa");
                   
                    System.out.println(">> " + carRs.getString("merek") + " warna " + carRs.getString("warna"));
                    System.out.println(">> Kapasitas " + carRs.getInt("kapasitas") + " orang");
                    System.out.println(">> Rp " + rupiahFormat.format(hargaSewa) + " / hari");
                    System.out.println(">> satuan: " + carRs.getString("satuan"));

                    if (!carRs.getString("satuan").equalsIgnoreCase("Tersedia")) {
                        System.out.println("Mobil tidak tersedia.");
                        return;
                    }
                } else {
                    System.out.println("Mobil tidak ditemukan.");
                    return;
                }
            }
            System.out.println("------------------------------");

            System.out.print("Input lama sewa (hari): ");
            int rentalDays = scanner.nextInt();
            scanner.nextLine();

            int rentalCost;
            String priceQuery = "SELECT harga_sewa FROM mobil WHERE kode = ?";
            try (PreparedStatement priceStmt = conn.prepareStatement(priceQuery)) {
                priceStmt.setString(1, carCode);
                ResultSet priceRs = priceStmt.executeQuery();
                if (priceRs.next()) {
                    rentalCost = priceRs.getInt("harga_sewa") * rentalDays;
                    System.out.println("Biaya Sewa: Rp " + rupiahFormat.format(rentalCost));
                } else {
                    System.out.println("Gagal menghitung biaya sewa.");
                    return;
                }
            }

            String updateCarStatus = "UPDATE mobil SET satuan = 'Disewa' WHERE kode = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateCarStatus)) {
                updateStmt.setString(1, carCode);
                updateStmt.executeUpdate();
            }

            String insertTransaction = "INSERT INTO sewa (no_sewa, id_pelanggan, kode_mobil, lama_sewa, total_biaya) " +
                    "VALUES (?, ?, ?, ?, ?)";
   


            try (PreparedStatement insertStmt = conn.prepareStatement(insertTransaction)) {
                insertStmt.setString(1, transactionId);
                insertStmt.setString(2, customerId);
                insertStmt.setString(3, carCode);
                insertStmt.setInt(4, rentalDays);
                insertStmt.setInt(5, rentalCost);
                insertStmt.executeUpdate();
            }
            System.out.println("------------------------------");

            System.out.println("Transaksi berhasil disimpan dengan Nomor Sewa: " + transactionId);

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found. Please ensure it's added to the classpath.");
        }
    }
}
