import java.util.Scanner;
import java.util.Random;
import java.util.Date;
import java.sql.*;


class Random_ID{
    public static String RID(){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    // create random string builder
    StringBuilder sb = new StringBuilder();

    // create an object of Random class
    Random random = new Random();

    // specify length of random string
    int length = 9;

    for(int i = 0; i < length; i++) {

      // generate random index number
      int index = random.nextInt(alphabet.length());
      // get character specified by index
      // from the string
      char randomChar = alphabet.charAt(index);
      // append the character to string builder
      sb.append(randomChar);
    }

    String randomString = sb.toString();
    return(randomString);

    }
}
class Money{
    static int amount;
    static String id;
    static void withdrawMoney(int a, String n){
        amount = a;
        id = n;
        try {
        Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "Schoolmate235*");
        Statement stm2 = con2.createStatement();
        
        try {
        	DatabaseMetaData dbmd = con2.getMetaData();
        	ResultSet rs4 = dbmd.getTables(null, null, id, null);
        	if (rs4.next()) {
        		System.out.println("Transaction previously occured.");
        	}
        	else {
        		String table2 = "create table "+id+"(dt TIMESTAMP, Amount_Withdrawn integer(20), Remaining_Amount integer(20));";
                stm2.executeUpdate(table2);
        	}
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        
        
        String get_data = "Select Amount from bank where ID = \'"+id+"\';";
        ResultSet rs1 = stm2.executeQuery(get_data);
        int sum = 0;
        while (rs1.next()){
            int amt = rs1.getInt("Amount");
            sum = sum + amt;
        }
        if (amount > sum){
            System.out.println("You do not have enough money");
        }
        else{
        int updated_amt  = sum - amount;
        String updated = "update bank set Amount = "+updated_amt+" where ID = \'"+id+"\';";
        stm2.executeUpdate(updated);
        String updateTable2 = "insert into "+id+" (dt, Amount_Withdrawn, Remaining_Amount) values (CURRENT_TIMESTAMP,"+amount+","+updated_amt+");";
        stm2.executeUpdate(updateTable2);
        System.out.println("You have withdrawn Rs. "+amount);
        System.out.println("Amount left in your account is Rs."+ updated_amt);

        }
        }
        catch(Exception e) {
        	System.out.println("Exception is: "+e);
        }
    }
    static void getPreviousTransactionDetails(String i){
        id = i;
        try {
        Connection con3 = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "Schoolmate235*");
        Statement stm3 = con3.createStatement();
        String total = "Select * from "+id+";";
        ResultSet rs2 = stm3.executeQuery(total);
        while (rs2.next()){
            int amt1 = rs2.getInt("Amount_Withdrawn");
            int amt2 = rs2.getInt("Remaining_Amount");
            Timestamp dt1 = rs2.getTimestamp("dt");
            System.out.println("Date and Time: "+dt1+" Amt. Withdrawn: "+amt1+" Amt. Remaining: "+amt2);
        }
        }
        catch(Exception e) {
        	System.out.println("Exception is: "+e);
        }

    }
}



class ATM {
    public static void main(String[] args) throws SQLException{
        Scanner s = new Scanner(System.in);
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "Schoolmate235*");
            Statement stm = con.createStatement();
            System.out.println("Please enter: \n1. For new customer\n2. For existing customer");
            int customer_status = s.nextInt();
           
            if (customer_status == 1){
                System.out.println("Please enter your name");
                String name = s.next();
                System.out.println("Please Enter your 4 digit PIN that is to be set");
                int pin = s.nextInt();
                String set_Customer_id = Random_ID.RID();
                System.out.println("Enter initial balamce you want to put in your account");
                int balance = s.nextInt();
                String sql2 = "INSERT INTO bank (ID, Name, Password, Amount) VALUES (\'"+set_Customer_id+"\',\'"+name+"\',"+pin+","+balance+");";
                boolean ex1 = stm.execute(sql2);
                System.out.println("Thanks for creating your account. Your initial balance is "+balance+"\nYour customer ID is "+set_Customer_id);
            }
            else if (customer_status == 2){
                System.out.println("Enter your customer ID");
                String check_customerid = s.next();
                System.out.println("Enter your pin:-");
                int check_pin = s.nextInt();
                String compare_pin = "Select Password from bank where ID = \'"+check_customerid+"\';";
                ResultSet rs = stm.executeQuery(compare_pin);
                boolean flag = false;
                while(rs.next()){
                    int cpin = rs.getInt("Password");
                    flag = check_pin == cpin;
                }
                if (flag == true){
                    System.out.println("Login Successful");
                    System.out.println("Choose from the following:- \n1.Withdraw Money\n2.Check Balance\n3.Get Previous Transactions' Details");
                    int get_choice = s.nextInt();
                    if (get_choice == 1){
                        System.out.println("Enter the amount to be withdrawn:-");
                        int amt_w = s.nextInt();
                        Money.withdrawMoney(amt_w, check_customerid);
                    }
                    else if (get_choice == 2){
                        String checkb = "Select Amount from bank where ID = \'"+check_customerid+"\';";
                        ResultSet rs3 = stm.executeQuery(checkb);
                        while(rs3.next()){
                            int fb = rs3.getInt("Amount");
                            System.out.println("Your Current Balance is Rs. "+fb);
                        }
                    }
                    else if (get_choice == 3){
                        Money.getPreviousTransactionDetails(check_customerid);
                    }
                }
                else{
                    System.out.println("Invalid Credentials!! TRY AGAIN.");
                }
                }
        }
        catch(Exception e){
            System.out.println("Exception is: "+e);
        }
}

}
