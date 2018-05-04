package lost.bot.db;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author lostone
 */
public class MySqlDatabasewResourcePool {
    private static MySqlDatabasewResourcePool instance=new MySqlDatabasewResourcePool();

    //This is the main mysql connection that's opened ONCE and closed when the
    //application closes:)
  //  private static Connection connection = pool.getConnection();

    private MysqlConnectionPoolDataSource pool;
    private String sequenceTable;
    private boolean sequenceSupported;

    private MySqlDatabasewResourcePool() {
           Hashtable<String,String> settings = new Hashtable<String,String>();
           /*xml
            //keep the order in this
            String expressions = "//databasename //mysqlserverhost //mysqlport //mysqluser //mysqlpassword";
            String variables = "databasename mysqlserverhost mysqlport mysqluser mysqlpassword";


            //get the server ip from a XML file
            XPath xpath = XPathFactory.newInstance().newXPath();


            //The input file... use the long method so the jar works:)
            InputSource inputSource = new InputSource("src/mastermindserver/serverSettings.xml");

            //Split the strings into arrays
            String[] exprArray = expressions.split(" ");
            String[] vars = variables.split(" ");

            //Evaluate everything in a for loop so you can then
            for (int i = 0; i < exprArray.length; i++) {
                //The NODE you're trying to select
                String expression = exprArray[i];

                //Select the node..
                NodeList nodes;
                try {
                    nodes = (NodeList) xpath.evaluate(expression, inputSource, XPathConstants.NODESET);
                    settings.put(vars[i],nodes.item(0).getTextContent());
                } catch (XPathExpressionException ex) {
                      System.out.println("There was a little problem evaluating the node");
                }

            }
         
            System.out.println("Reading xml file ok!");
            pool = new MysqlConnectionPoolDataSource();
           

        pool.setDatabaseName(settings.get("databasename"));
        pool.setServerName(settings.get("mysqlserverhost"));
        pool.setPort(Integer.parseInt(settings.get("mysqlport")));
        pool.setUser(settings.get("mysqluser"));
        pool.setPassword(settings.get("mysqlpassword"));
   
              *     
              */
        pool.setDatabaseName("lostbot");
        pool.setServerName("localhost");
        pool.setPort(3306);
        pool.setUser("lostbot");
        pool.setPassword("TheLostB0t");
      
   
        //sequenceTable="pkgenerator";
        sequenceSupported = false;
          

    }

    /**
     * This method returns an instance of the MySQLDatabase Object
     */
    public static MySqlDatabasewResourcePool getInstance(){
        return instance;
    }

     /**
     * This method returns a connection with the database
     * @return A connection
     * @throws SQLException
     */
    public  Connection getConnection() throws SQLException{
        return pool.getConnection();
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
        MySqlDatabasewResourcePool db = MySqlDatabasewResourcePool.getInstance();
        Connection c = db.getConnection();
        c.close();
    }

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
    }

   /**
     * Returns a ResultSet returned by the Database
     * @param sql The sql command
     * @param conn The connection to the database
     * @param pstatements The parameters for the sql command
     * @return A ResultSet object
     * @throws SQLException
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