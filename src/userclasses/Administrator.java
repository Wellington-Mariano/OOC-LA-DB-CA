/*
INSERT INTO equationssystem.admin_info(id, admin_name, phone_number, admin_password, email_address)
VALUES('1', 'CCT', '0000000', 'Dublin', 'email_address');
*/
package userclasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author welli
 */
public class Administrator implements AdminInterface {

    // Administartor atributes 
    private String name;
    private int id;
    private int phone_number;
    private String email_address;
    private String admin_password;
    
    String dbName = "equationssystem";
    String DB_URL = "jdbc:mysql://localhost/";
    String USER = "CCT";
    String PASSWORD = "Dublin";

    //THESE METHODS ARE ALLOWED ONCE THE ADMIN IS LOGGED IN 
     /**
     * Constructor 
     *
     * @param name
     * @param phone_number 
     * @param email_address
     * @param admin_password
     */
    public Administrator(String name, int phone_number, String email_address, String admin_password) {
        this.name = name;
        this.phone_number = phone_number;
        this.email_address = email_address;
        this.admin_password = admin_password;
    }

    // This method allows administrator to login
    public boolean admin_login(String name, String admin_password) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

        ResultSet rs;//var of type result set as this is the type sql returns

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();//Creating the queries `statements`
                ) {
            stmt.execute("USE equationssystem;");

            rs = stmt.executeQuery("SELECT admin_name, admin_password from admin_info"); //rs receiving value from querie

            rs.next();//code for the first line of db table
            if (rs.getString("admin_name").equalsIgnoreCase(name) && (rs.getString("admin_password").equals(admin_password))) {
                return true;
            }

            while (rs.next()) {    //rest of the lines for the db table
                if (rs.getString("admin_name").equalsIgnoreCase(name) && (rs.getString("admin_password").equals(admin_password))) {
                    return true;
                }
            }

        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    // This method is used to retun a list of users
    public ArrayList<UserInterface> access_list() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        ArrayList<UserInterface> users = new ArrayList<>();

        ResultSet rs;//var of type result set as this is the type sql returns

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();//Creating the queries `statements`
                ) {
            stmt.execute("USE equationssystem;");

            rs = stmt.executeQuery("SELECT * from user_info"); //rs receiving value from querie

            while (rs.next()) {//loop to get info from the whole databases
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getInt("phone_number"),
                        rs.getString("user_password"),
                        rs.getString("email_address")
                ));
            }

        } catch (SQLException e) {
            return null;
        }
        return users;//RETURNED USERS
    }

    // Method used to delete a user from the database
    public void delete(int id) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        String sql = "DELETE FROM user_info WHERE id = ?";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.execute("USE equationssystem;");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Operation executed successfully.");
    }

    // Method used to review the equations and final results by the administartor
    public void review_operations() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        ArrayList<String> two_var_equations = new ArrayList<>(); // Create an ArrayList for equations with two variables
        ArrayList<String> three_var_equations = new ArrayList<>(); // Create an ArrayList for equations with three variables
        
        ResultSet rs;//var of type result set as this is the type sql returns

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();//Creating the queries `statements`
                ) {
            stmt.execute("USE equationssystem;");

            rs = stmt.executeQuery("SELECT * from two_var_equations"); //rs receiving value from querie

            while (rs.next()) {//loop to get info from the whole databases
                two_var_equations.add(rs.getString("first_equation")); // add to list first equation
                two_var_equations.add(rs.getString("second_equation")); // add to list second equation
                two_var_equations.add(rs.getString("equation_final_result")); // add to list final result with x and y values
            }
            System.out.println("Two variable equations:");
            for(String two_var : two_var_equations){
                System.out.println(two_var + " "); // print eqautions and result
            }
            
            rs = stmt.executeQuery("SELECT * from three_var_equations"); //rs receiving value from querie
            
            while (rs.next()) {//loop to get info from the whole databases
                three_var_equations.add(rs.getString("first_equation")); // add to list first equation
                three_var_equations.add(rs.getString("second_equation")); // add to list second equation
                three_var_equations.add(rs.getString("third_equation")); // add to list third equation
                three_var_equations.add(rs.getString("equation_final_result")); // add to list final result with x, y and z values
            }
            System.out.println("Three variable equations:");
            for(String three_var : three_var_equations){
                System.out.println(three_var + " "); // print eqautions and result
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to create the table for administartor
    public boolean admin_datadb_setup() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        String ADMIN_DB_NAME = "admin_info";//need a constructor and than pass a Admin type as par to insert things into db

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            //this part of the code is creating a connection between the db and this user and password
            Statement stmt = conn.createStatement();
            //THIS METHOD IS USED TO DO THE QUERYES
            stmt.execute("USE equationssystem;");

            stmt.execute(//if admin table does not exists create it
                    "CREATE TABLE IF NOT EXISTS " + ADMIN_DB_NAME + "("
                    + "`id` INT(100) NOT NULL AUTO_INCREMENT,"
                    + "`admin_name` VARCHAR(25),"
                    + "`phone_number` VARCHAR(10),"
                    + "`admin_password` VARCHAR(20),"
                    + "`email_address` VARCHAR (50),"
                    + "PRIMARY KEY(`id`)"
                    + ")");

            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    //Setters and Getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phone_number = phoneNumber;
    }

    public String getEmailAddress() {
        return email_address;
    }

    public void setEmailAddress(String emailAddress) {
        this.email_address = emailAddress;
    }

    // Method to update administrator information
    public String update_admin_info(String columnToBeChanged, String admin_name, String old_info, String new_info) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        ResultSet rs;
        boolean userExists;

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();//Creating the queries `statements`
            //IF USER NAME AND EMAIL ADDRESS MATCHES THAN WE ALLOW CHANGES 
            stmt.execute("USE equationssystem;");

            rs = stmt.executeQuery("SELECT admin_name FROM admin_info");

            rs.next();//code below is for the first line in db
            //user needs to be in the databases matching email address and name so they can make changes
            if (rs.getString("admin_name").equalsIgnoreCase(admin_name)) {

                stmt.execute("USE equationssystem;");
                stmt.executeUpdate("UPDATE admin_info SET " + columnToBeChanged + "='" + new_info + "' WHERE " + columnToBeChanged + "='" + old_info + "'");
                return "Updated successfully";
            }
            while (rs.next()) {//code for the rest of the lines in db
                if (rs.getString("admin_name").equalsIgnoreCase(admin_name)) {
                    stmt.execute("USE admin_info;");
                    stmt.executeUpdate("UPDATE user_info SET " + columnToBeChanged + "='" + new_info + "' WHERE " + columnToBeChanged + "='" + old_info + "'");
                    return "Updated successfully";
                }
            }
            return "Your administrator details are incorrect. Please try again";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Your administrator details are incorrect. Please try again";
        }
    }

}
