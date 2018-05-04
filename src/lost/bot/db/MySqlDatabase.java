package lost.bot.db;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Hashtable;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import lost.bot.functions.Users;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author lostone
 */
public class MySqlDatabase {
    private static MySqlDatabase instance=new MySqlDatabase();

    //This is the main mysql connection that's opened ONCE and closed when the
    //application closes:)
  //  private static Connection connection = pool.getConnection();
    Connection con = null;
    private String sequenceTable;
    private boolean sequenceSupported;

    private MySqlDatabase() {
           Hashtable<String,String> settings = new Hashtable<String,String>();
   
        String url = "jdbc:mysql://localhost:3306/lostbot";
        String user = "lostbot";
        String password = "TheLostB0t";

        try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    /**
     * This method returns an instance of the MySQLDatabase Object
     */
    public static MySqlDatabase getInstance(){
        return instance;
    }

     /**
     * This method returns a connection with the database
     * @return A connection
     * @throws SQLException
     */
    public  Connection getConnection() throws SQLException{
        return con;
    }

    /**
     * This method returns a new id for a tabel and a colum, in case
     * a sequence is being used, the tabel and column are being generated.
     * @param conn A connection with the database
     * @param tabel The tabel name
     * @param kolom The column name
     */
    public int createNewID(Connection conn, String tabel,String kolom){
        try{
            if ( sequenceSupported){
                String sql = "select nextval('"+sequenceTable+"')";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if ( rs.next()){
                    return rs.getInt(1);
                }
                return -1;
            }else{
                if ( conn != null && !conn.isClosed()){
                    String sql = "select max("+kolom+") as maxid from "+tabel;
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    if ( rs.next() ){
                        int maxid = rs.getInt("maxid");
                        return maxid+1;
                    }
                    return -1;
                }
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return -1;
    }

    /**
     * This method can be used to test what data can be found in a resultset
     * Warning: The resultset can not be used anymore after using this function!
     * @param rs The resultset to print
     */
    public void printResultSet(ResultSet rs){
        try{
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i=1;i <= rsmd.getColumnCount();i++){
                System.out.print(rsmd.getColumnLabel(i));
                System.out.print(",");
            }
            System.out.println();
            while(rs.next()){
                for ( int i = 1 ; i <= rsmd.getColumnCount(); i++){
                    System.out.print(rs.getString(i));
                    System.out.print(",");
                }
                System.out.println();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * A method to test the connection with the database
     * @throws Exception
     */
    public static void main(String[] args)throws Exception{
        System.out.println("Execute this main method to test the connection !");
        MySqlDatabase db = MySqlDatabase.getInstance();
        Connection c = db.getConnection();
        //UsernameDAO.getInstance().newUser(new Users("the.lostone",100,7,"LostOne",true,"baiat","http://lba.im",true));
        Users user = UsernameDAO.getInstance().getUsername("the.lostone");
        System.out.println(user.getUsernameID());
        c.close();
        System.out.println("it works!");
    }

    /*transaction? ok use  conn.setAutoCommit(false);
     *  do your executes
     *   conn.setAutoCommit(true);
     */
    /**
     * Executes a sql command
     * @param sql The sql command to be executed
     * @param conn The connection to the database
     * @param pstatements SQL parameters
     * @throws SQLException
     */
    public void executeSQL(String sql,Connection conn, Object... pstatements) throws SQLException {
        //prepared statement..
        PreparedStatement ps = conn.prepareStatement(sql);
        int i = 1;
        //loop through all statements
        for (Object o : pstatements) {
            ps.setObject(i, o);
            ++i;
        }
        ps.executeUpdate();
        ps.close();
    }

   /**
     * Returns a ResultSet returned by the Database
     * @param sql The sql command
     * @param conn The connection to the database
     * @param pstatements The parameters for the sql command
     * @return A ResultSet object
     * @throws SQLException
     * always close res ALWAYS
     */
    public ResultSet getResultSet(String sql,Connection conn, Object... pstatements) throws SQLException {
       //prepared statement..
        PreparedStatement ps = conn.prepareStatement(sql);
        int i = 1;
        //loop through all statements
        for (Object o : pstatements) {
            ps.setObject(i, o);
            ++i;
        }
        ResultSet res = ps.executeQuery();
        return res;
    }

}