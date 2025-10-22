package Banking_system;

import java.sql.*;
import java.util.Scanner;

public class AccountManager {

    private Connection connection;

    private Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void credit_money(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter security pin: ");
        String security_pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(account_number != 0){
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? and security_pin = ?");
                ps.setLong(1,account_number);
                ps.setString(2,security_pin);
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    String credit_query = "UPDATE accounts SET balance = balance + ? where account_number = ?";
                    PreparedStatement ps1 = connection.prepareStatement(credit_query);
                    ps1.setDouble(1,amount);
                    ps1.setLong(2,account_number);
                    int rowsAffected = ps1.executeUpdate();
                    if(rowsAffected > 0){
                        System.out.println("Rs." + amount + "Credited Successfully.");
                        connection.commit();
                        connection.setAutoCommit(true);
                    }else{
                        System.out.println("Failed to Credit Rs." + amount);
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }

                }else{
                    System.out.println("Invalid Security pin");
                }
            }else{
                System.out.println("Invalid account number.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }


    public void debit_money(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security pin: ");
        String security_pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(account_number != 0){
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM accounts where account_number = ? and security_pin = ?");
                ps.setLong(1,account_number);
                ps.setString(2,security_pin);

                ResultSet resultSet = ps.executeQuery();
                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");
                    if(amount <= current_balance){
                        String debit_query = "Update accounts set balance = balance - ? where account_number = ?";
                        PreparedStatement ps1 = connection.prepareStatement(debit_query);
                        ps1.setDouble(1,amount);
                        ps1.setLong(2,account_number);
                        int rowsAffected = ps1.executeUpdate();
                        if(rowsAffected > 0){
                            System.out.println("RS."+amount+" debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("Transaction Failde");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }

                    }else{
                        System.out.println("Insufficient Balance!");
                    }
                }else{
                    System.out.println("Invalid Pin!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);      // this line is doubtfull.
    }


    public void transfer_money(long sender_account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number = scanner.nextLong();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security pin: ");
        String security_pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(sender_account_number != 0 && receiver_account_number != 0){
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM accounts where account_number = ? and security_pin = ?");
                ps.setLong(1,sender_account_number);
                ps.setString(2,security_pin);
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    double current_balance = rs.getDouble("balance");
                    if(amount <= current_balance){

                        // debit and credit queries
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";


                        // debit and credit prepared statements
                        PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);

                        // set values for debit and credit prepared statements
                        creditPreparedStatement.setDouble(1,amount);
                        creditPreparedStatement.setLong(2,receiver_account_number);
                        debitPreparedStatement.setDouble(1,amount);
                        debitPreparedStatement.setLong(2,sender_account_number);

                        int rowAffected1 = debitPreparedStatement.executeUpdate();
                        int rowAffected2 = creditPreparedStatement.executeUpdate();

                        if(rowAffected1 > 0 && rowAffected2 > 0){
                            System.out.println("Transaction Successful");
                            System.out.println("Rs." + amount + "Transferred successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("Transaction Failed !!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient balance!");
                    }
                }else{
                    System.out.println("Invalid security pin!");
                }
            }else{
                System.out.println("Invalid Account number");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void getBalance(long account_number){
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        try{
            PreparedStatement ps = connection.prepareStatement("SELECT balance from accounts where account_number = ? and security_pin = ?");
            ps.setLong(1,account_number);
            ps.setString(2,security_pin);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                double balance = rs.getDouble("balance");
                System.out.println("Balance : " + balance);
            }else{
                System.out.println("Invalid account_number or password.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }


}
