package MovieRental;

/**
 * @author Tshepo Sepadile
 * @author Banele Mlamleli
 */
import static MovieRental.ReadFile.*;
import java.awt.HeadlessException;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Database {

    public static Connection connect;
    public static Statement stmnt;
    public static PreparedStatement prepStmnt;
    public static ResultSet resultSet;
    
    //The arraylist below will store all the data read from the database from each table created
    public static ArrayList<Customer> arrayListSelectAllCustomers = new ArrayList<>();
    public static ArrayList<DVD> arrayListSelectAllMovies = new ArrayList<>();
    public static ArrayList<Rental> arrayListSelectAllRentals = new ArrayList<>();
    

    //Establish connection to the databse using UCAnAccess
    public static void Connect() {
        try {
            String dbURL = "jdbc:ucanaccess:///home/shaun/Documents/PROGRAMMING/Java/Projects/MovieRental1/src/Assets/MovieDatabase.accdb";
//            String username = "";
//            String password = "";
            String driverName = "net.ucanaccess.jdbc.UcanaccessDriver";
            System.out.println("About to Load the JDBC Driver....");
            Class.forName(driverName);
            connect = DriverManager.getConnection(dbURL);
            System.out.println("Successfully connected to database.");            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage()+"\nUnable to connect to database.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage()+"\nUnable to connect to database.");
        } catch(ClassNotFoundException cnfe){
            JOptionPane.showMessageDialog(null, cnfe.getMessage()+"\nUnable to connect to database.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(cnfe.getMessage()+"\nUnable to connect to database.");
        }
    }
    
    //Dropping the table it the are created already
    public static void dropTables() {
        try {
            String dropCustomerTable = "DROP TABLE CUSTOMER";
            String dropDvdTable = "DROP TABLE DVD";
            String dropRentalTable = "DROP TABLE RENTAL";
            stmnt = connect.createStatement();
            stmnt.execute(dropCustomerTable);
            stmnt.execute(dropDvdTable);
            stmnt.execute(dropRentalTable);

            System.out.println("Tables dropped");
        } catch (SQLException sqle) {
            System.out.println(sqle.fillInStackTrace());
        } catch (NullPointerException npe) {
            System.out.println(npe.fillInStackTrace());
        }
    }
        
    public static void createTables(Connection connect) {
        try {
            //Creating tables
            System.out.println("Creating CUSTOMER table.");
            String create_table_cust = "create table CUSTOMER (custNumber INTEGER CONSTRAINT customer_custnum_pk PRIMARY KEY, "
                    + "firstName VARCHAR(50) CONSTRAINT customer_fName_nn NOT NULL, "
                    + "surname VARCHAR(50) CONSTRAINT customer_surname_nn NOT NULL, "
                    + "phoneNum VARCHAR(20) CONSTRAINT customer_phoneNum_uk UNIQUE, "
                    + "credit DOUBLE, "
                    + "Can_Rent VARCHAR(5) CONSTRAINT customer_can_rent_nn NOT NULL)";
            
            System.out.println("Creating DVD table.");
            String create_table_dvd = "create table DVD(dvdNumber INTEGER CONSTRAINT dvd_dvdNum_pk PRIMARY KEY, "
                    + "title VARCHAR(50) CONSTRAINT dvd_title_uk UNIQUE, "
                    + "category VARCHAR(50) CONSTRAINT dvd_category_nn NOT NULL, "
                    + "price DOUBLE, "
                    + "New_Release VARCHAR(5) CONSTRAINT dvd_new_release_nn NOT NULL, "
                    + "Available_For_Rental VARCHAR(5) CONSTRAINT dvd_available_for_rental_nn NOT NULL)";
            
            System.out.println("Creating RENTAL table.");
            String create_table_rental = "create table RENTAL(rentalNumber INTEGER CONSTRAINT rental_rentalNum_pk PRIMARY KEY, "
                    + "dateRented VARCHAR(12) CONSTRAINT rental_drented_nn NOT NULL, "
                    + "dateReturned VARCHAR(12) CONSTRAINT rental_dreturned_nn NOT NULL, "
                    + "custNumber INTEGER CONSTRAINT rental_custNum_fk REFERENCES CUSTOMER(custNumber), "
                    + "dvdNumber INTEGER CONSTRAINT rental_dvdNum_fk REFERENCES DVD(dvdNumber), "
                    + "totalPenaltyCost DOUBLE)";
            stmnt = connect.createStatement();
//            stmnt.executeUpdate("DROP TABLE RENTAL"); //deleting rental table if it exist already
//            stmnt.executeUpdate("DROP TABLE CUSTOMER"); //deleting customer table if it exist already
//            stmnt.executeUpdate("DROP TABLE DVD"); //deleting dvd table if it exist already            
            stmnt.executeUpdate(create_table_cust); //creating customer table
            stmnt.executeUpdate(create_table_dvd); //creating dvd table
            stmnt.executeUpdate(create_table_rental); //creating rental table
            JOptionPane.showMessageDialog(null,"Tables were created successfully.","Success",JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Tables were created successfully.");

            //stmnt.close();
            //connect.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e.getMessage()+"\nError!! Database was not created","Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage()+"\nError!! Database was not created");
        }
    }
        
    //populate CUSTOMER table with data from serialized file
    public void insertCustomerData() {
        try {
            String insertDetails = "INSERT INTO CUSTOMER(custNumber, firstName, surname, phoneNum, credit, Can_Rent) VALUES(?, ?, ?, ?, ?, ?)";
            for(int a = 0; a < customerArraylist.size(); a++){
                prepStmnt = connect.prepareStatement(insertDetails);
                prepStmnt.setInt(1, customerArraylist.get(a).getCustNumber());
                prepStmnt.setString(2, customerArraylist.get(a).getName());
                prepStmnt.setString(3, customerArraylist.get(a).getSurname());
                prepStmnt.setString(4, customerArraylist.get(a).getPhoneNum());
                prepStmnt.setDouble(5, customerArraylist.get(a).getCredit());
                prepStmnt.setString(6, Boolean.toString(customerArraylist.get(a).canRent()));
                prepStmnt.executeUpdate();
            }
            System.out.println("Customer data has been recorded successfully");
        } catch (HeadlessException | SQLException error) {
            JOptionPane.showMessageDialog(null, "Unable to insert Customer data from serialized file\n" + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //populate DVD table with data from serialized file
    public void insertDvdData() {
        try {
            String insertDetails = "INSERT INTO DVD(dvdNumber, title, category, price, New_Release, Available_For_Rental) VALUES(?, ?, ?, ?, ?, ?)";
            for(int a = 0; a < dvdArraylist.size(); a++){
                prepStmnt = connect.prepareStatement(insertDetails);
                prepStmnt.setInt(1, dvdArraylist.get(a).getDvdNumber());
                prepStmnt.setString(2, dvdArraylist.get(a).getTitle());
                prepStmnt.setString(3, dvdArraylist.get(a).getCategory());
                prepStmnt.setDouble(4, dvdArraylist.get(a).getPrice());
                prepStmnt.setString(5, Boolean.toString(dvdArraylist.get(a).isNewRelease()));
                prepStmnt.setString(6, Boolean.toString(dvdArraylist.get(a).isAvailable()));
                prepStmnt.executeUpdate();
            }
            System.out.println("Dvd data has been recorded successfully");
        } catch (HeadlessException | SQLException error) {
            JOptionPane.showMessageDialog(null, "Unable to insert Movie data from serialized file\n" + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //populate RENTAL table with data from serialized file
    public void insertRentalData() {
        try {
            String insertDetails = "INSERT INTO RENTAL(rentalNumber, dateRented, dateReturned, custNumber, dvdNumber, totalPenaltyCost) VALUES(?, ?, ?, ?, ?, ?)";
            for(int a = 0; a < rentalArraylist.size(); a++){
                prepStmnt = connect.prepareStatement(insertDetails);
                prepStmnt.setInt(1, rentalArraylist.get(a).getRentalNumber());
                prepStmnt.setString(2, rentalArraylist.get(a).getDateRented());
                prepStmnt.setString(3, rentalArraylist.get(a).getDateReturned());
                prepStmnt.setInt(4, rentalArraylist.get(a).getCustNumber());
                prepStmnt.setInt(5, rentalArraylist.get(a).getDvdNumber());
                prepStmnt.setDouble(6, rentalArraylist.get(a).getTotalPenaltyCost());
                prepStmnt.executeUpdate();
            }
            System.out.println("Rental data has been recorded successfully");
        } catch (HeadlessException | SQLException error) {
            JOptionPane.showMessageDialog(null, "Unable to insert Rental data from serialized file\n" + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Selecting all the data about customer from the CUSTOMER table
    public void selectAllCustomers(){
        System.out.println("Reading all data from Customers table");
        try {
            stmnt = connect.createStatement();
            ResultSet allCustomers = stmnt.executeQuery("SELECT * FROM CUSTOMER");
            if (allCustomers != null) {
                while (allCustomers.next()){
                    arrayListSelectAllCustomers.add(new Customer(allCustomers.getInt(1), allCustomers.getString(2), allCustomers.getString(3), allCustomers.getString(4), allCustomers.getDouble(5), Boolean.parseBoolean(allCustomers.getString(6))));
                }
            }
        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Unable to read all registered customer users\n" + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Selecting all the data about the movies from the DVD table
    public void selectAllMovies() {
        System.out.println("Reading all data from Movies/DVD table");
        try {
            stmnt = connect.createStatement();
            ResultSet allMovies = stmnt.executeQuery("SELECT * FROM DVD");
            int categoryValue = 0;
            if (allMovies != null) {
                while (allMovies.next()) {
                    switch(allMovies.getString(3)) {
                        case "horror":categoryValue = 1;break;
                        case "Sci-fi":categoryValue = 2;break;
                        case "Drama":categoryValue = 3;break;
                        case "Romance":categoryValue = 4;break;
                        case "Comedy":categoryValue = 5;break;
                        case "Action":categoryValue = 6;break;
                        case "Cartoon":categoryValue = 7;break;
                    }
                    arrayListSelectAllMovies.add(new DVD(allMovies.getInt(1), allMovies.getString(2), categoryValue, Boolean.parseBoolean(allMovies.getString(4)), Boolean.parseBoolean(allMovies.getString(5))));
                }
            }
        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Unable to read all registered movies\n" + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Selecting all the data about rentals from the RENTAL table
    public void selectAllRental() {
        System.out.println("Reading all data from Rental table");
        try {
            stmnt = connect.createStatement();
            ResultSet allRentals = stmnt.executeQuery("SELECT * FROM RENTAL");
            if (allRentals != null) {
                while (allRentals.next()){
                    arrayListSelectAllRentals.add(new Rental(allRentals.getInt(1), allRentals.getString(2), allRentals.getString(3), allRentals.getInt(4), allRentals.getInt(5)));
                }
            }
        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Unable to read all registered rentals\n" + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Adding new customer into the CUSTOMER table
    public void addCustomer(int custNumber, String firstName, String surname, String phoneNum, double credit, String Can_Rent) {
        try {
            String addCustomerDetails = "INSERT INTO CUSTOMER(custNumber, firstName, surname, phoneNum, credit, Can_Rent) VALUES(?, ?, ?, ?, ?, ?)";
                prepStmnt = connect.prepareStatement(addCustomerDetails);
                prepStmnt.setInt(1, custNumber);
                prepStmnt.setString(2, firstName);
                prepStmnt.setString(3, surname);
                prepStmnt.setString(4, phoneNum);
                prepStmnt.setDouble(5, credit);
                prepStmnt.setString(6, Can_Rent);
                prepStmnt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Customer data has been recorded successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Customer data has been recorded successfully");
        } catch (HeadlessException | SQLException error) {
            JOptionPane.showMessageDialog(null, "Unable to insert new customer data\n" + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Adding new movie into the DVD table
    public void addDvd(int dvdNumber, String title, String category, double price, String newRelease, String available) {
        try {
            String insertDvd = "INSERT INTO DVD(dvdNumber, title, category, price, New_Release, Available_For_Rental) VALUES(?, ?, ?, ?, ?, ?)";
                prepStmnt = connect.prepareStatement(insertDvd);
                prepStmnt.setInt(1, dvdNumber);
                prepStmnt.setString(2, title);
                prepStmnt.setString(3, category);
                prepStmnt.setDouble(4, price);
                prepStmnt.setString(5, newRelease);
                prepStmnt.setString(6, available);
                prepStmnt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Dvd data has been recorded successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Dvd data has been recorded successfully");
        } catch (HeadlessException | SQLException error) {
            JOptionPane.showMessageDialog(null, "Unable to insert new Movie data\n" + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void rentMovieAndUpdateCustomerTable(int customerNm){
        try {
            String updateDetails = "UPDATE CUSTOMER SET Can_Rent = ? WHERE custNumber = "+customerNm;
                prepStmnt = connect.prepareStatement(updateDetails);
                prepStmnt.setString(1, "false");
                prepStmnt.executeUpdate();
            System.out.println("customer rental status has updated successfully");
        } catch (HeadlessException | SQLException error) {
            JOptionPane.showMessageDialog(null, "Unable to update customer data\n" + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void rentMovieAndUpdateDvdTable(int rentedDvdNm){
        try {
            String updateDetails = "UPDATE DVD SET Available_For_Rental = ? WHERE dvdNumber = "+rentedDvdNm;
            prepStmnt = connect.prepareStatement(updateDetails);
            prepStmnt.setString(1, "false");
            prepStmnt.executeUpdate();
            System.out.println("Dvd table has updated successfully");
        } catch (HeadlessException | SQLException error) {
            JOptionPane.showMessageDialog(null, "Unable to update DVD data\n" + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void insertRentedMovieIntoRentalTable(int rentalNumber, String dateRented, String dateReturned, int custN, int rentedDvdNum, double totPenaltyFee){
        try {
            String insertDetails = "INSERT INTO RENTAL(rentalNumber, dateRented, dateReturned, custNumber, dvdNumber, totalPenaltyCost) VALUES(?, ?, ?, ?, ?, ?)";
                prepStmnt = connect.prepareStatement(insertDetails);
                prepStmnt.setInt(1, rentalNumber);
                prepStmnt.setString(2, dateRented);
                prepStmnt.setString(3, dateReturned);
                prepStmnt.setInt(4, custN);
                prepStmnt.setInt(5, rentedDvdNum);
                prepStmnt.setDouble(6, totPenaltyFee);
                prepStmnt.executeUpdate();
            System.out.println("Rental data has been recorded successfully");
        } catch (HeadlessException | SQLException error) {
            JOptionPane.showMessageDialog(null, "Unable to insert rental details in the Rental table\n" + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void returnMovieAndUpdateCustomerTable(int customerNm){
        try {
            String updateDetails = "UPDATE CUSTOMER SET Can_Rent = ? WHERE custNumber = "+customerNm;
                prepStmnt = connect.prepareStatement(updateDetails);
                prepStmnt.setString(1, "true"); //6
                prepStmnt.executeUpdate();
            System.out.println("customer rental status has updated successfully");
        } catch (HeadlessException | SQLException error) {
            JOptionPane.showMessageDialog(null, "Unable to update customer data\n" + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void returnMovieAndUpdateDvdTable(int rentedDvdNm){
        try {
            String updateDetails = "UPDATE DVD SET Available_For_Rental = ? WHERE dvdNumber = "+rentedDvdNm;
            prepStmnt = connect.prepareStatement(updateDetails);
            prepStmnt.setString(1, "true"); //6
            prepStmnt.executeUpdate();
            System.out.println("Dvd rental stutus has been updated successfully");
        } catch (HeadlessException | SQLException error) {
            JOptionPane.showMessageDialog(null, "Unable to update DVD data\n" + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void updateCustomerCredit(int custNum, double credt){
        try {
            String updateDetails = "UPDATE CUSTOMER SET credit = ? WHERE custNumber = "+custNum;
            prepStmnt = connect.prepareStatement(updateDetails);
            prepStmnt.setDouble(1, credt); //5
            prepStmnt.executeUpdate();
            System.out.println("Credit has been updated successfully");
        } catch (HeadlessException | SQLException error) {
            JOptionPane.showMessageDialog(null, "Unable to update customer credit\n" + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}