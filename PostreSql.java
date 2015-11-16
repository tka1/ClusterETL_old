import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.text.ParseException;

public class PostreSql {

    public static void main(String[] args)throws ParseException {

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement pst = null;

    
        String url = "jdbc:postgresql://localhost/postgres";
        String user = "postgres";
        String password = "powerday1!";

        try {
            con = DriverManager.getConnection(url, user, password);
            String decall = "OH2BBT";
            String dxcall = "OH2GI";
            String S_N = "CW";
            
            String datetime = "2015-11-15 08:03";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm");
            java.util.Date date = sdf.parse(datetime);
            java.sql.Timestamp sqlDate = new Timestamp(date.getTime());
            System.out.println("String converted to java.sql.Date :" + sqlDate);
            System.out.println("Date :" + date);
            
            //st = con.createStatement();
            //rs = st.executeQuery("SELECT VERSION()");

            //if (rs.next()) {
              //  System.out.println(rs.getString(1));
            //}
            
            String stm = "INSERT INTO cluster.clustertable(decall, dxcall, datetime, sig_noise) VALUES(?, ?, ?, ?)";
            pst = con.prepareStatement(stm);
            pst.setString(1, decall);
            pst.setString(2, dxcall);
            pst.setTimestamp(3, sqlDate);
            pst.setString(4, S_N);
            pst.executeUpdate();


        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(PostreSql.class.getName());
            lgr.log(Level.WARNING, ex.getMessage(), ex);
            System.out.println(ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(PostreSql.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
                System.out.println(ex);
            }
        }
    }
}

