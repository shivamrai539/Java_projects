package Banking_system;

import java.sql.*;
import java.util.Scanner;

public class Accounts {

    private Connection connection;

    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public long open_account(String email){
        if(!account_exist(email)){
            String open_account_query = "INSERT INTO accounts(account_number, full_name, email, balance, security_pin) VALUES(?,?,?,?,?)";
            scanner.nextLine();
            System.out.print("Enter full Name: ");
            String full_name = scanner.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter Security pin: ");
            String security_pin = scanner.nextLine();

            try{
                long account_number = generateAccountNumber();
                PreparedStatement ps = connection.prepareStatement(open_account_query);
                ps.setLong(1,account_number);
                ps.setString(2,full_name);
                ps.setString(3,email);
                ps.setDouble(4,balance);
                ps.setString(5,security_pin);
                int rowsAffected = ps.executeUpdate();

                if(rowsAffected > 0){
                    return account_number;
                }else{
                    throw new RuntimeException("Account Creation Failed!!");
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account Already Exist");
    }

    public long getAccount_number(String email){
        String query = "SELECT account_number from accounts WHERE email = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getLong("account_number");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Account Number Doesn't Exist!");
    }

    private long generateAccountNumber(){
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_number from Accounts ORDER BY account_number DESC LIMIT 1");
            if(resultSet.next()){
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number+1;
            }else{
                return 10000100;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 10000100;
    }

    public boolean account_exist(String email){
        String query = "SELECT * FROM accounts WHERE email = ?";

        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }else {
                return false;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
