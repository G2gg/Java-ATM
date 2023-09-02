import java.sql.*;

import com.mysql.cj.xdevapi.Statement;

public class ATM_dbms {
    public static void main(String[] args) throws SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "Schoolmate235*");
            Statement stm = con.createStatement();
            
        }
        catch(ClassNotFoundException c){
            e.printStackTrace();
        }

    }
}
