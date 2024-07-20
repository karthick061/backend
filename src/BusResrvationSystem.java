import java.sql.*;

public class BusResrvationSystem {
    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/bus_reservation_system";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "1234567890";

    public static void main(String[] args) throws ClassNotFoundException {
        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            System.out.println("Connected to the database.");

            // Example: Insert data into Company table
            insertIntoCompany(conn, "Dreams transport", "Parthiban", "8013529854");

            // Example: Insert data into Bus table
            insertIntoBus(conn, 50, "AC", "velacherry", "chennai", "lakshmangudi", "tn-50", "maiyalduthurai-chengalpattu-vilupuram", "10:00", "mailaduthurai,vilupuram", 1);

            // Example: Insert data into Driver table
            insertIntoDriver(conn, "babu", "1234567890", 1);

            // Example: Insert data into Booking table
            insertIntoBooking(conn, 1, "2023-07-18", "karthick@gmail.com", "Confirmed");

            // Close the connection
            conn.close();
            System.out.println("Database operations completed and connection closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to insert data into Company table
    private static void insertIntoCompany(Connection conn, String companyName, String ownerName, String contactNo) throws SQLException {
        String query = "INSERT INTO Company (Company_Name, Owner_Name, Contact_No) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, companyName);
            stmt.setString(2, ownerName);
            stmt.setString(3, contactNo);
            stmt.executeUpdate();
            System.out.println("Inserted data into Company table.");
        }
    }

    // Method to insert data into Bus table
    private static void insertIntoBus(Connection conn, int capacity, String acOrNonAC, String destination, String fromPlace, String toPlace, String busNoPlate, String routes, String timing, String busStops, int companyId) throws SQLException {
        // Check if the Company_ID exists in the Company table before inserting into Bus
        String checkCompanyIdQuery = "SELECT COUNT(*) FROM Company WHERE Company_ID = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkCompanyIdQuery)) {
            checkStmt.setInt(1, companyId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    String query = "INSERT INTO Bus (Capacity, AC_or_non_AC, Destination, From_Place, To_Place, Bus_No_Plate, Routes, Timing, Bus_Stops, Company_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setInt(1, capacity);
                        stmt.setString(2, acOrNonAC);
                        stmt.setString(3, destination);
                        stmt.setString(4, fromPlace);
                        stmt.setString(5, toPlace);
                        stmt.setString(6, busNoPlate);
                        stmt.setString(7, routes);

                        // Ensure timing is in "hh:mm:ss" format
                        if (timing.length() == 5) { // format is "hh:mm"
                            timing += ":00";
                        }
                        stmt.setTime(8, Time.valueOf(timing));

                        stmt.setString(9, busStops);
                        stmt.setInt(10, companyId);
                        stmt.executeUpdate();
                        System.out.println("Inserted data into Bus table.");
                    }
                } else {
                    System.out.println("Company ID does not exist in the Company table.");
                }
            }
        }
    }

    // Method to insert data into Driver table
    private static void insertIntoDriver(Connection conn, String driverName, String driverNo, int busNo) throws SQLException {
        // Check if the Bus_No exists in the Bus table before inserting into Driver
        String checkBusNoQuery = "SELECT COUNT(*) FROM Bus WHERE Bus_No = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkBusNoQuery)) {
            checkStmt.setInt(1, busNo);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    String query = "INSERT INTO Driver (Driver_Name, Driver_No, Bus_No) VALUES (?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, driverName);
                        stmt.setString(2, driverNo);
                        stmt.setInt(3, busNo);
                        stmt.executeUpdate();
                        System.out.println("Inserted data into Driver table.");
                    }
                } else {
                    System.out.println("Bus number does not exist in the Bus table.");
                }
            }
        }
    }

    // Method to insert data into Booking table
    private static void insertIntoBooking(Connection conn, int busNo, String bookingDate, String mail, String confirmationMail) throws SQLException {
        // Check if the Bus_No exists in the Bus table before inserting into Booking
        String checkBusNoQuery = "SELECT COUNT(*) FROM Bus WHERE Bus_No = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkBusNoQuery)) {
            checkStmt.setInt(1, busNo);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    String query = "INSERT INTO Booking (Bus_No, Booking_Date, Mail, Confirmation_Mail) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setInt(1, busNo);
                        stmt.setDate(2, Date.valueOf(bookingDate));
                        stmt.setString(3, mail);
                        stmt.setString(4, confirmationMail);
                        stmt.executeUpdate();
                        System.out.println("Inserted data into Booking table.");
                    }
                } else {
                    System.out.println("Bus number does not exist in the Bus table.");
                }
            }
        }
    }
}
