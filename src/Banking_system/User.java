package Banking_system;

import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    private Connection connection;

    private Scanner scanner;

    public User(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(){
        scanner.nextLine();
        System.out.print("Full name: ");
        String full_name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if(user_exist(email)){
            System.out.println("User Already Exists for this Email Address!! ");
            return;
        }

        String register_query = "INSERT INTO user(full_name, email, password ) VALUES (?,?,?)";
        try{
            PreparedStatement ps = connection.prepareStatement(register_query);
            ps.setString(1,full_name);
            ps.setString(2,email);
            ps.setString(3,password);

            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0){
                System.out.println("Registration Successfull!");
            }else{
                System.out.println("Registration Failde!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String login(){
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String login_query = "SELECT * FROM user WHERE email = ? and password = ?";

        try{
            PreparedStatement ps = connection.prepareStatement(login_query);
            ps.setString(1,email);
            ps.setString(2,password);
            ResultSet resultset = ps.executeQuery();
            if(resultset.next()){
                return email;
            }else{
                return null;
            }

        }catch( SQLException e ){
            e.printStackTrace();
        }

        return null;
    }

    private boolean user_exist(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
