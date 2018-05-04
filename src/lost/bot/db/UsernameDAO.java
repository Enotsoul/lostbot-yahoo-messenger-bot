package lost.bot.db;

import java.sql.*;
import java.util.ArrayList;

import lost.bot.functions.Users;

/**
 *
 * @author lostone
 */
public class UsernameDAO {

    private static UsernameDAO instance = null;

    private UsernameDAO(){}


    /**
     * This method returns an instance of the UsernameDAO
     */
    public static UsernameDAO getInstance(){
        if(instance==null){
            instance = new UsernameDAO();
            return instance;
        }else
            return instance;
    }

    //(String usernameID,int points,int level,String name, Boolean mass,String gender, String website)
    
   //todo
    public ArrayList<Users> getUsers() throws SQLException {
        ArrayList<Users> users = new ArrayList<Users>();

        String sql = "SELECT * FROM lb_Users";

     //   Connection conn = MySqlDatabase.getInstance().getConnection();
      /*  
        PreparedStatement ps = conn.prepareStatement(sql);

        //Eventually put parameters... if any..?
        //ps.setObject(1, o);
        //Run the result..
        ResultSet res = ps.executeQuery();

        //Get the data.. for all the users..
        while (res.next()){
            int userID = res.getInt(1);
            String username = res.getString(2);
            String password = res.getString(3);
            String email = res.getString(4);
            Login l = new Login(username,password,email);
            l.setUserID(userID);
            users.add(l);
        }
         //Close the resultset
        res.close();
        
        conn.close();
        */
        return users;
    }

 //<editor-fold defaultstate="collapsed" desc="comment">
/*
    public void selectSQL(String sql,Connection conn, Object... object) throws SQLException {
    PreparedStatement ps = conn.prepareStatement(sql);

    //execute query...
    ResultSet res = ps.executeQuery();
    while (res.next()){
    int userID = res.getInt(1);
    String username = res.getString(2);
    String password = res.getString(3);
    String email = res.getString(4);
    Login l = new Login(username,password,email);
    l.setUserID(userID);
    users.add(l);
    }
    return everything
    }
     */// </editor-fold>

    /**
     * This method creates a new user in the database
      * @throws SQLException
 */
    public void newUser(Users u) throws SQLException {
    	  //(usernameID, points, level, name, mass, gender, website, privateIdentity);
        String sql = "INSERT INTO LostUsers (usernameID,points,level,name,mass,gender,website,privateIdentity) VALUES (?,?,?,?,?,?,?,?)";
        Connection conn = MySqlDatabase.getInstance().getConnection();
        //execute the SQL with ease
        MySqlDatabase.getInstance().executeSQL(sql,conn,u.getUsernameID(),u.getPoints(),u.getLevel(),u.getName(),u.getMass(),u.getGender(),u.getWebsite(),u.getPrivateIdentity());

        //always close connection atm
        // conn.close();
    }

     /**
     * This method deletes a player from the database
     * @param l The Login object for the specific user
     * @throws SQLException
*/
    public void deleteUsername (Users u) throws SQLException {
        String sql = "DELETE FROM LostUsers WHERE usernameID=?";
        Connection conn = MySqlDatabase.getInstance().getConnection();
       MySqlDatabase.getInstance().executeSQL(sql,conn,u.getUsernameID());
        //always close the connection
        //conn.close();
    }

    /**
     * This method returns the Login info for a user
     * @param user The username
     * @throws SQLException
	*/
    public Users getUsername(String user) throws SQLException {
       String sql = "SELECT * FROM LostUsers WHERE usernameID=?";
       Connection conn = MySqlDatabase.getInstance().getConnection();
       ResultSet res = MySqlDatabase.getInstance().getResultSet(sql,conn,user);
       Users u;
       		
       if (res.next())
       {
           String usernameID = res.getString(1);
           int points = res.getInt(2);
           int level = res.getInt(3);
           String name = res.getString(4);
           boolean mass = res.getBoolean(5);
           String gender = res.getString(6);
           String website = res.getString(7);
           boolean privateIdentity = res.getBoolean(8);
    
           u = new Users(usernameID, points, level, name, mass, gender, website, privateIdentity);
         
       } 
       else
       {
           //this is used when allowing a user to register/login so we don't
           //use too much code..
           u = new Users("0",0,0,"0",false,"0","0",false);

       }
       //Close the resultset
       res.close();
       //Close the connection
       //conn.close();
       return u;
   }

    //update username
   public void updateUser(Users u) throws SQLException {
	   //(usernameID, points, level, name, mass, gender, website, privateIdentity);
       String sql = "UPDATE  LostUsers  WHERE usernameID=? SET points=?,level=?";
       Connection conn = MySqlDatabase.getInstance().getConnection();
       //execute the SQL with ease
       MySqlDatabase.getInstance().executeSQL(sql,conn,u.getUsernameID(),u.getPoints(),u.getLevel());

       //always close connection atm
       // conn.close();
   }
   public void updateUsers(ArrayList<Users> users) throws SQLException {
		String sql = "UPDATE  LostUsers  SET points=?,level=? WHERE usernameID=?";
		Connection conn = MySqlDatabase.getInstance().getConnection();
		conn.setAutoCommit(false);
		//execute the SQL with ease
		for (Users u : users) {
			MySqlDatabase.getInstance().executeSQL(sql,conn,u.getUsernameID(),u.getPoints(),u.getLevel());
			System.out.println("how about " + u.getUsernameID() + " with " + u.getPoints() + " points");
		}

		conn.commit();
		conn.setAutoCommit(true);
   }
    /**
     * Test method to see if the connection works
     * @throws Exception
   
    public static void main(String[] args) throws Exception{
        System.out.println("Adding a username to the database..");
        //  UsernameDAO.getInstance().newUser(new Login("Test3","test123","test3@testerminators.net"));
        Login l;
        l = UsernameDAO.getInstance().getUsername("TeSt");
        System.out.println(l.getPassword());
        l = UsernameDAO.getInstance().getUsername("TeSter");
        System.out.println(l.getPassword());
        System.out.println("Username added successfully/or other things completed..");
    }
    */
    
    /*
     *          try {
                    UsernameDAO.getInstance().banUsername(pl.getUsername(), true);
                } catch (SQLException ex) {
                  frmServer.printMessage("SQL Error while trying to ban user! Does he have super powers? He's been disconnected anyway..");
                  ch.stopConnection();
                }
                
     */
 

}