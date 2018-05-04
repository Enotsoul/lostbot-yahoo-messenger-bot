package lost.bot.functions;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openymsg.network.NoSuchConferenceException;
import org.openymsg.network.event.SessionEvent;

import lost.bot.LostBot;
import lost.bot.db.UsernameDAO;

public class Users {
	//Hashtable with the users... 
	public static HashMap<String,Users> usersList = new HashMap<String,Users>();
	public enum Verify {  DBPROBLEM, OK, NOLVL, NOPOINTS,  BANNED  }
        private static Timer updateDB;
	
	public String usernameID,name,gender,website;
	public int level,points;
	public Boolean mass,privateIdentity;
	public Boolean changed = false;
	public long lastChange=0; //= System.currentTimeMillis();
	
	//level 0 = banned
	//level 1 = orice utilizator
	//level 2 = utilizator de incredere
	//level 3 = prieten
	//level 4 = admin
	//level 5 = owner
	public Users(String usernameID,int points,int level,String name, Boolean mass,String gender, String website,Boolean privateIdentity) {
		this.usernameID = usernameID;
		this.points = points;
		this.level = level;
		this.name = name;
		this.mass=mass;
		this.gender = gender;
		this.website = website;
		this.privateIdentity = privateIdentity;
	}
	
	//*********************************************************************
	//Verify existent & access
	//It verifies if the username exists in the hashMap
	//then verify if it exists in the database..
	//If it doesn't exist, we add him to the database & hashmap
	//if he exists, we verify the level...
	//if he's banned.. return "banned" and do nothing
	//if his/her level is below the required one return "nolvl"
	//if his points are below the required points return "noPoints"
	//TODO: list of all users in an hashmap with key/value for usernameID to Users
	//then verify locally & update to db only when necessary..like every 10 minutes or so:)
	//use integer instead of strings?(overhead..)
	public static Verify verifyUser(String usernameID,int level,int points) {
		try {
			Users user = usersList.get(usernameID.toLowerCase());
			
			if (user == null) {
				user = UsernameDAO.getInstance().getUsername(usernameID);
				System.out.println("User doesn't exist, trying to verify if he exists in db.. " + user.getUsernameID());
			}
			
			//if user doesn't exist... add him..
			if (user.getUsernameID().equals("0")) {
				user = new Users(usernameID.toLowerCase(),25,1,"not set",true,"not set","none",false);
				UsernameDAO.getInstance().newUser(user);
				System.out.println("Creating user & Updating to database.");
			} 
			if (user.getLevel()==0) {
				try {
					LostBot.session.ignoreContact(usernameID.toLowerCase(), true);
				} catch (Exception e) {
					System.out.println("Problem when ignoring contact..");
				}
				return Verify.BANNED;
			}
			if (user.getLevel()<level) {
				return Verify.NOLVL;
			}
			if (user.getPoints()<points) {
				return Verify.NOPOINTS;
			}
			//Substract the points & set that it has changed
			user.setPoints(user.getPoints()-points);
			user.setChanged(true);
			user.setLastChange(System.currentTimeMillis());
			
			//update userlist:D
			usersList.put(user.getUsernameID(),user);
			
			return Verify.OK;
		} catch (SQLException e) {
			System.out.println("FUNCTION verifyUser : OOps when verifying the user..");
			e.printStackTrace();
			return Verify.DBPROBLEM;
		}
	}
	
	//TODO: Timer every 10 minutes to update the CHANGED users..
	//Check if changed is true, and the lastChange time is of any revelance..
	//if changed => true.. then update the mysql..  set it to FALSE and set the lastChange to 0
	
	/*
	 * 		for (Users u : usersList.values()) {
			
		}
	 */
	
	//Get user
	public static Users getUser(String usernameID) {
		Users user = usersList.get(usernameID.toLowerCase());
		
		if (user == null) {
			try {
				user = UsernameDAO.getInstance().getUsername(usernameID);
			} catch (SQLException e) {
				System.out.println("OOps when verifying the user..");
			}
		}
		
		//if user doesn't exist.. return an empty username..
		if (user.getUsernameID().equals("0")) {
			user = new Users("0",0,0,"0",true,"0","0",false);
		}
		return user;
	}
	//Modify user settings...
	public static void modifyUser(SessionEvent ev) {
		String goodMsg = ev.getMessage();
		goodMsg = goodMsg.replaceAll("</font>", "");
		
		int start = goodMsg.indexOf("!modify");
		String newstr = goodMsg.substring(start);
		String msg[] =  newstr.split(" ", 3);
		
		try {
			if (msg[1].toLowerCase().equalsIgnoreCase("bla")) {
					LostBot.session.sendMessage(msg[2], "");
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
        //Timer & stuff
        
        public static void StartDbUpdate(int minutes) {
            try {
                updateDB = new Timer();
                updateDB.scheduleAtFixedRate(new UsersDatabaseUpdate(),minutes * 60* 1000, minutes * 60* 1000);
                LostBot.session.sendConferenceMessage(LostBot.debugChan, "Started DB update timer");
            } catch (IllegalStateException ex) {
                Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchConferenceException ex) {
                Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
	public static void StopDbUpdate() {
		updateDB.cancel();
	}
	
	static class UsersDatabaseUpdate extends TimerTask {
               @Override
		public void run() {
			try {
				ArrayList<Users> users= new ArrayList<Users>();
				int total = 0;
				//verify all users and if something changed.. update the db.. and set the update to 0
				for (Users u : usersList.values()) {
					if (u.getChanged()) {
						u.setChanged(false);
						users.add(u);
						usersList.put(u.getUsernameID(), u);
						total++;
					}
				}
				UsernameDAO.getInstance().updateUsers(users);
				LostBot.session.sendConferenceMessage(LostBot.debugChan, "Done saving " + total + " users to DB..");
			} catch (SQLException ex) {
				System.out.println("Problem when saving all the users to the db..");
				Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IllegalStateException ex) {
				Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
			} catch (NoSuchConferenceException ex) {
				Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
			}
	    }
	 }
	//Getters & setters
	
	public String getUsernameID() {
		return usernameID;
	}
	public void setUsernameID(String usernameID) {
		this.usernameID = usernameID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public Boolean getMass() {
		return mass;
	}
	public void setMass(Boolean mass) {
		this.mass = mass;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Boolean getPrivateIdentity() {
		return privateIdentity;
	}

	public void setPrivateIdentity(Boolean privateIdentity) {
		this.privateIdentity = privateIdentity;
	}

	public static HashMap<String, Users> getUsersList() {
		return usersList;
	}

	public static void setUsersList(HashMap<String, Users> usersList) {
		Users.usersList = usersList;
	}

	public Boolean getChanged() {
		return changed;
	}

	public void setChanged(Boolean changed) {
		this.changed = changed;
	}

	public long getLastChange() {
		return lastChange;
	}

	public void setLastChange(long lastChange) {
		this.lastChange = lastChange;
	}
	
	
	
}
