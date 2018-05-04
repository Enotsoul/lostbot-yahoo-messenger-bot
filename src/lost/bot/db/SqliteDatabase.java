package lost.bot.db;

import java.sql.*;

import org.sqlite.Function;
import org.sqlite.JDBC;

public class SqliteDatabase {

	public SqliteDatabase() {
		
	}
	public static void Sqlite(String[] args) throws SQLException  {
		
        Connection conn = DriverManager.getConnection("jdbc:sqlite:lostbot.db");
        
        conn.close();
        
    }
	public static Connection getConnecton() throws SQLException {
		  Connection conn = DriverManager.getConnection("jdbc:sqlite:lostbot.db");
		  return conn;
	}
	//todo
	public static void createDb() {
		Connection conn;
		try {
			conn = getConnecton();			
		    Statement stat = conn.createStatement();
		 
			conn.setAutoCommit(false);	
		
		    stat.executeUpdate("drop table if exists LostUsers;");
		    //String usernameID,int points,int level,String name, Boolean mass,String gender, String website,Boolean privateIdentity
		    stat.executeUpdate("create table LostUsers (name, occupation);");
		
			conn.commit();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	

	public static void test() throws SQLException {
	    Connection conn =
	      DriverManager.getConnection("jdbc:sqlite:test.db");
	    Statement stat = conn.createStatement();
	    stat.executeUpdate("drop table if exists people;");
	    stat.executeUpdate("create table people (name, occupation);");
	    PreparedStatement prep = conn.prepareStatement(
	      "insert into people values (?, ?);");

	    prep.setString(1, "Gandhi");
	    prep.setString(2, "politics");
	    prep.addBatch();
	    prep.setString(1, "Turing");
	    prep.setString(2, "computers");
	    prep.addBatch();
	    prep.setString(1, "Wittgenstein");
	    prep.setString(2, "smartypants");
	    prep.addBatch();

	    conn.setAutoCommit(false);
	    prep.executeBatch();
	    conn.setAutoCommit(true);

	    ResultSet rs = stat.executeQuery("select * from people;");
	    while (rs.next()) {
	      System.out.println("name = " + rs.getString("name"));
	      System.out.println("job = " + rs.getString("occupation"));
	    }
	    rs.close();
	    conn.close();
	  }

	public static void executeSqlTrans(String sql,Connection conn, Object... pstatements) throws SQLException {
		//fast transaction
		 	PreparedStatement ps = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			//to modify
			for (int i=0; i < 10000; i++) {
				  for (Object o : pstatements) {
			            ps.setObject(i, o);
			            ++i;
			        }
			    ps.executeUpdate();
			}
			conn.commit();
			conn.close();
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
    	conn.close();
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
        //close the connection
        conn.close();
        return res;
    }

	
	
}
