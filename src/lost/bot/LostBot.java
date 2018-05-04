package lost.bot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import lost.bot.functions.Users;

import org.openymsg.addressBook.YahooAddressBookEntry;
import org.openymsg.network.*;
import org.openymsg.network.chatroom.ChatroomManager;
import org.openymsg.network.chatroom.YahooChatLobby;
import org.openymsg.roster.Roster;


public class LostBot  {

	public static Session session = new Session();
	static SessionHandler sessionHandle = new SessionHandler();	


	//settings..
	public static String owner = "the.lostone";
	public static YahooProtocol protocol;
	public static YahooConference debugChan;
	public static YahooConference trivia;
	
	public static void main(String [ ] args){ 
		/*DirectConnectionHandler dch = new DirectConnectionHandler(getServerName(), getServerPort());
		Session session = new Session(dch);*/
	
		session.addSessionListener(sessionHandle);
			try {
				session.login("yourbotHere", "yourbotpassword");
		
			} catch (AccountLockedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (LoginRefusedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FailedLoginException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		
		if (session !=null && session.getSessionStatus()== SessionState.LOGGED_ON) {
			//logged in!
			try {
				session.sendMessage("the.lostone", "I'm online boss. At your service!");
				String[] conf = {owner};
				debugChan=	session.createConference(conf, "Debug Session of LostBot");
				//session.refreshStats();
				trivia =	session.createConference(conf, "Triviuta");
				//Start db update..
				Users.StartDbUpdate(1);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
}
