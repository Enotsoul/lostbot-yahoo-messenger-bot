package lost.bot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import lost.bot.functions.Ascii;
import lost.bot.functions.Spy;
import lost.bot.functions.Users;
import lost.bot.functions.Users.Verify;

import org.openymsg.network.AccountLockedException;
import org.openymsg.network.FailedLoginException;
import org.openymsg.network.FireEvent;
import org.openymsg.network.LoginRefusedException;
import org.openymsg.network.NoSuchConferenceException;
import org.openymsg.network.ServiceType;
import org.openymsg.network.Session;
import org.openymsg.network.SessionState;
import org.openymsg.network.YahooUser;
import org.openymsg.network.event.SessionAdapter;
import org.openymsg.network.event.SessionAuthorizationEvent;
import org.openymsg.network.event.SessionChatEvent;
import org.openymsg.network.event.SessionConferenceEvent;
import org.openymsg.network.event.SessionConferenceInviteEvent;
import org.openymsg.network.event.SessionConferenceMessageEvent;
import org.openymsg.network.event.SessionErrorEvent;
import org.openymsg.network.event.SessionEvent;
import org.openymsg.network.event.SessionExceptionEvent;
import org.openymsg.network.event.SessionFileTransferEvent;
import org.openymsg.network.event.SessionFriendAcceptedEvent;
import org.openymsg.network.event.SessionFriendEvent;
import org.openymsg.network.event.SessionFriendFailureEvent;
import org.openymsg.network.event.SessionFriendRejectedEvent;
import org.openymsg.network.event.SessionGroupEvent;
import org.openymsg.network.event.SessionListEvent;
import org.openymsg.network.event.SessionListener;
import org.openymsg.network.event.SessionNewMailEvent;
import org.openymsg.network.event.SessionNotifyEvent;
import org.openymsg.network.event.SessionPictureEvent;


public class SessionHandler extends SessionAdapter  {
	public static ArrayList<YahooUser> usersOnList = new ArrayList<YahooUser>();
	public static HashMap<String,Users>  users = new HashMap<String,Users>();
	
	public void messageReceived(SessionEvent ev) {
		try {
		//	LostBot.session.sendMessage(ev.getFrom(), "I got your message!" + ev.getMessage());
			SwitchMessages(ev);
			LogCommand(ev);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("A booboo..");
			e1.printStackTrace();
		}
	}
	
	//TODO.. MANY MANY THINGS
	//1. Make sqlite database
	//2, Add people
	//3. Control level!
	//4. More functions:D
	//5. Debug conference where bot types everything that happens to him..
	 private void SwitchMessages(SessionEvent ev) throws IllegalStateException, IOException {
		String goodMsg = ev.getMessage();
		String usernameID = ev.getFrom();
		goodMsg = goodMsg.replaceAll("</font><>", "");
		goodMsg = goodMsg.replaceAll("</FADE>", "");

		//spy user in an external class.. (best to do this with all bigger functions..)
		Spy.spyUser(ev);
		
		if (goodMsg.toLowerCase().contains("!msg")) {
			if (verifyUser(usernameID, 1, 1)) {
				 int start = goodMsg.indexOf("!msg");
				 String newstr = goodMsg.substring(start);
				 String msg[] =  newstr.split(" ", 3);
				 if (!LostBot.session.getLoginID().getId().equals(msg[1].toLowerCase())) {
					//id & msg
					 LostBot.session.sendMessage(msg[1], msg[2]);
					 Raspunde(ev.getFrom(),"I-am trimis mesajul id-ului " + msg[1]);
				 } else {
					 Raspunde(ev.getFrom(),"lasa ca stiu eu cum sta treaba.. endless loop:Pp");
				 }
			}
		 } else if (goodMsg.toLowerCase().contains("!help")) {
				LostBot.session.sendMessage(ev.getFrom(), "ATENTIE! Cel mai bine cand imi scrii o comanda este sa nu folosesti font/bold/underline ...\n" +
						"\nO lista cu functiile pe care le am:\n" +
						"Functii nivel 1:\n" +
						"!msg <id> <mesaj> - (1p) - Trimite un mesaj unui id..\n"+
						"!spy <start/stop> <id> - (10p) - Iti trimit toate mesajele pe care mi le trimite un anumit utilizator.. \n"+
						"!status <new status> - (10p) -\n"+
						"!buzz <id> - (1p) - Ce crezi ca face?\n"+
						"!users - (3p) - Lista cu toti userii care ii am pe lista mea - Nivel minim 2\n"+
						"!info <me/user>  - (0p) - Informatii despre un utilizator. Sau despre tine(me). \n" +
						"!addfriend <user> - (1p) - Il adauga pe utilizator-ul acela din partea ta! \n" +
						"!massmsg <mesaj> - (33p) - Trimite un mesaj [mass] tuturor persoanelor de pe lista bot-ului. Mesajul este semnat cu id-ul tau.\n" +
						"!version - (0p) -Informatii despre mine..\n" +
						"!trivia - (0p) - INformatii despre trivia (NEIMPLEMENTATA)" +
						"More to come...");
				verifyUserWrite(usernameID, 4, "Functii nivel 4:\n" +
						"!spy list - (0p) - lista cu toate spionajele actuale\n" +
						"!modify <id> <settings> - (0p) - Modifica userul..\n" +
						"!debugchan - Primesti o invitatie pe conferinta de debug a bot-ului");
				LostBot.session.sendMessage(ev.getFrom(), "(1p) defineste punctele utilizate pentru a utiliza acea comanda. Inlocuiti <> cu valorile voastre. Comanda finala este ceva de genu !msg the.lostbot salut! \nDISCLAIMER: Utilizand acest bot esti deacord cu licenta BSD :D\n\n" +
						"THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" " +
						"AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES" +
						" OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. " +
						"IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, " +
						"SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; " +
						"LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, " +
						"STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.");
		 } else if (goodMsg.toLowerCase().contains("!buzz")) {
			 if (verifyUser(usernameID, 1, 1)) {
				int start = goodMsg.indexOf("!buzz");
				String newstr = goodMsg.substring(start);
				String msg[] =  newstr.split(" ", 3);
					
				if (!LostBot.session.getLoginID().toString().toLowerCase().equals(msg[1])) {
					LostBot.session.sendBuzz(msg[1]);
					Raspunde(ev.getFrom(),"L-am buzzuit:D.");
				} else {
					 Raspunde(ev.getFrom(),"Doar n'am sa ma buzoiesc singur...");
				}
			 }
		 }  else if (goodMsg.toLowerCase().contains("!status")) {
			 if (verifyUser(usernameID, 1, 10)) {
				 int start = goodMsg.indexOf("!status");
				 String newstr = goodMsg.substring(start);
				 String msg[] =  newstr.split(" ", 2);
	
				//id & msg
				 LostBot.session.setStatus(msg[1],false);
				 Raspunde(ev.getFrom(),"Draga, pentru tine.. statusu meu.");
			 }
		 } else if (goodMsg.toLowerCase().contains("!users")) {		
			 if (verifyUser(usernameID, 1, 3)) {
				 Raspunde(ev.getFrom(),"Am " + usersOnList.size() + "de utilizatori pe lista, uite-i:");
				 LostBot.session.refreshFriends();
				 Raspunde(ev.getFrom(), GetMyList());
			 }
		 } else if (goodMsg.toLowerCase().contains("!info")) {
			 if (verifyUser(usernameID, 1, 0)) {
				 int start = goodMsg.indexOf("!info");
				 String newstr = goodMsg.substring(start);
				 String msg[] =  newstr.split(" ", 2);
				 
				 Raspunde(ev.getFrom(),"Functia inca nu este completa conform standardului.. Informatii despre "+ msg[1] + " :");
			
				 YahooUser searchUser = searchUser(msg[1]);
				 Raspunde(ev.getFrom()," Nickname " + searchUser.getNickName() + " Prenume " + searchUser.getFirstName() + " Nume " + searchUser.getLastName() + " Stealth " + searchUser.getStealth() + "  Status " + searchUser.getStatus() + " Custom status " + searchUser.getCustomStatusMessage());
				 if (msg[1].equals("me")) {
					 Raspunde(usernameID, "Ai "+ Users.getUser(usernameID).getPoints() + " puncte ramase..");
				 }
			 }
		 }else if (goodMsg.toLowerCase().contains("!adduser")) {
			 int start = goodMsg.indexOf("!adduser");
			 String newstr = goodMsg.substring(start);
			 String msg[] =  newstr.split(" ", 2);
			 //TODO: verify 
			/*
			 String msg[] =  ev.getMessage().split(" ", 2);
			 Raspunde(ev.getFrom(),"Informatii despre "+ msg[1] + " :");
			 
			 YahooUser searchUser = searchUser(msg[1]);
			 Raspunde(ev.getFrom()," Nickname " + searchUser.getNickName() + " Prenume " + searchUser.getFirstName() + " Nume " + searchUser.getLastName() + " Stealth " + searchUser.getStealth() + "  Status " + searchUser.getStatus() + " Custom status " + searchUser.getCustomStatusMessage());
			 */
			 Raspunde(ev.getFrom(),"I-am cerut sa-mi fie prieten..");
			 Raspunde(msg[1],"Salut, " + ev.getFrom() + " mi-a zis sa te adaug ca prieten.. sunt un ROBOT, dupa ce ma adaugi (te voi adauga automat si eu) poti scrie !help pentru o lista cu comenzi:)");
	
		 } else if (goodMsg.toLowerCase().contains("!version")) {
			 Raspunde(ev.getFrom(),"VERSIUNEA MEA: LostBot 0.1  de LostOne\n Creat pentru mici distractii pe messneger:D");
		 } else if (goodMsg.toLowerCase().contains("!florin")) {
			 int start = goodMsg.indexOf("!florin");
			 String newstr = goodMsg.substring(start);
			 String msg[] =  newstr.split(" ", 2);
			 
			 Raspunde(ev.getFrom(),MiscFunctions.florinGoogleLink(msg[1]));
		 } else if (goodMsg.toLowerCase().contains("!ascii")) {
			Ascii.HandleAsciiCommands(ev);
		 }else if (goodMsg.toLowerCase().contains("!spy")) {
			Spy.handleSpy(ev);	 
		 } else if (goodMsg.toLowerCase().contains("!massmsg")) {
			 if (verifyUser(usernameID, 1, 33)) {
				 int start = goodMsg.indexOf("!massmsg");
				 String newstr = goodMsg.substring(start);
				 String msg[] =  newstr.split(" ", 2);
				 int users = 0;
				 for (YahooUser yu: usersOnList) {
					 Raspunde(yu.getId(), "[mass by "  + ev.getFrom() + " ]: " + msg[1] );
					 users++;
				 }
			 }
			 Raspunde(ev.getFrom(), "Am trimis mass-ul acela la " + users + " persoane.");
		 } else if (goodMsg.toLowerCase().contains("!debugchan")) {
			 if (verifyUser(usernameID, 4, 0)) {
				LostBot.session.extendConference(LostBot.debugChan, ev.getFrom(), "Master you are welcome in my conference channel!");
		 	}
		 }  else if (goodMsg.toLowerCase().contains("!modify")) {
			 if (verifyUser(usernameID, 4, 0)) {
				//TODO: implement modification of stuff.. ATM it modifies the POINTS you have..
				 int start = goodMsg.indexOf("!modify");
				 String newstr = goodMsg.substring(start);
				 String msg[] =  newstr.split(" ", 2);
		 	}
		 }  
	}
	 
	public void buzzReceived(SessionEvent ev)  {
			try {
				LostBot.session.sendMessage(ev.getFrom(), "Why do you bother me?");
				LostBot.session.sendBuzz(ev.getFrom());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	 }
     	                 
	 //user doesn't want to join conference.. PM him! 
	 public void 	conferenceInviteDeclinedReceived(SessionConferenceEvent event)  {
		 System.out.println("YOU DECLINED, ME?!?");
	 }
	           
	 public void 	conferenceInviteReceived(SessionConferenceInviteEvent ev)  {
	//	 LostBot.session.
		 System.out.println("Got invited to a conference.. by" +ev.getFrom() + " :" + ev.getMessage());
		 LogOther("Got invited to a conference.. by " +ev.getFrom() + " :" + ev.getMessage());
		 LogOther("I'm " + LostBot.session.getSessionID() + " got invited by " + ev.getFrom());
		 try {
			if (ev.getFrom().equals(LostBot.owner)) {
				LostBot.session.acceptConferenceInvite(ev.getRoom());
			}  else if (!ev.getFrom().equals(LostBot.session.getLoginID().getId())) { 
				LostBot.session.declineConferenceInvite(ev.getRoom(), "Scuze, nu sunt interesat de conferintele tale.. SUNT UN BOT");
			}
		 } catch (Exception e) {
			 //oopsie
		 }
	 }
	           
	 public void 	conferenceLogoffReceived(SessionConferenceEvent event)  {
		 System.out.println("Logoff?");
	 }
	           
	 public void 	conferenceLogonReceived(SessionConferenceEvent event)  {
		 System.out.println("Logon??");
	 }
	 
	 public void conferenceMessageReceived(SessionConferenceMessageEvent ev)   {	
		try {
			 if (ev.getRoom().equals(LostBot.debugChan)) {
				 SwitchMessages(ev);
			 } else if (ev.getRoom().equals(LostBot.trivia)) {
				 LostBot.session.sendConferenceMessage(ev.getRoom(), "Sorry, nothing implemented yet!");
			 } else {
				 LogOther("Conference msg from " +ev.getFrom() + " :" + ev.getMessage());	 
			 }
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	           
	 public void 	connectionClosed(SessionEvent event)  {
		try {
			if (LostBot.session.getSessionStatus() != SessionState.LOGGED_ON) {
				LostBot.session.login("the.lostbot", "LostOne1sCool");
			}
		} catch (AccountLockedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LoginRefusedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FailedLoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	           
	 public void 	contactAcceptedReceived(SessionFriendAcceptedEvent ev)  {
		try {
			LostBot.session.sendMessage(ev.getFrom(), "Acuma suntem \"prieteni\".. Cum te cheama?");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	 }
	 
	 //user doesn't like you:(:(:(
	 public void 	contactRejectionReceived(SessionFriendRejectedEvent  event)  {
		 
	 }
	           
	 public void 	contactRequestReceived(SessionAuthorizationEvent e)  {
			try {

				boolean accept = true;
				if (accept) { 
					// accepting // first parameter is friend ym id 
					// second parameter is your friend network (eg. Yahoo, MSN, Lotus, etc...) 
					LostBot.session.acceptFriendAuthorization(e.getFrom(), e.getProtocol()); 
					// if you like to act like official YM client 
					// you can send contact request after you accept your friend request 
					// second parameter is which group you want to put your friend in your contact list 
					LostBot.session.sendNewFriendRequest(e.getFrom(), "Friends", e.getProtocol()); 
					//double this to be sure..it works:D
					LostBot.session.acceptFriendAuthorization(e.getFrom(), e.getProtocol());  
					//LostBot.session.sendNewFriendRequest(e.getFrom(), "Friends", e.getProtocol()); 
					LogOther("Friend request accepted from " + e.getFrom());
				} else { 
					// rejecting 
					LostBot.session.rejectFriendAuthorization(e, e.getFrom(), "Nu vreau sa iti fiu prieten!"); 
					LogOther("Friend request REJECTED from " + e.getFrom());
				}
			
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	 }
	           

	 public void 	errorPacketReceived(SessionErrorEvent event) {
		 
	 }
	           
	 public void 	fileTransferReceived(SessionFileTransferEvent event) {
		 
	 }
	           
	 public void 	friendAddedReceived(SessionFriendEvent event) {
		 
	 }
	           
	 public void 	friendRemovedReceived(SessionFriendEvent event) {
		 
	 }
	           
	 public void 	friendsUpdateFailureReceived(SessionFriendFailureEvent event) {
		 
	 }
	           
	 public void 	friendsUpdateReceived(SessionFriendEvent ev) {
	//	 System.out.println(ev.getFrom() + " might went offline/online:)");
		// LogOther("Friends update.." +  ev.getMessage());
	 }
	           
	 public void 	groupRenameReceived(SessionGroupEvent event) {
		 
	 }
	           
	 public void 	inputExceptionThrown(SessionExceptionEvent event) {
		 System.out.println("Helloz");
	 }
	           
	 public void 	listReceived(SessionListEvent ev) {
		 //frefresh friends & list
		 for (int i = 0; i< usersOnList.size(); i++) {
			 usersOnList.remove(i);
		 }
		for (YahooUser yu : ev.getContacts()) {
			usersOnList.add(yu);
		}
	
	 }
        
	 public void 	newMailReceived(SessionNewMailEvent ev)  {
		 LogOther("I got a new mail" + ev.getFrom());
	 }
/*	           
	 public void 	notifyReceived(SessionNotifyEvent event)  {
		 
	 }*/
	           
	 public void 	offlineMessageReceived(SessionEvent event) {
		 messageReceived(event);
	 }
	           
	 public void 	pictureReceived(SessionPictureEvent ev) {
		 LogOther("Pic recieved from " + ev.getFrom());
	 }
	 /*
	  * My functions..
	  */
	 //My functions
	 public String GetMyList() {
		String list = "";
		 for (int i = 0; i<usersOnList.size();i++) {
			 list += usersOnList.get(i).getId() + " , ";
		 }
		 
		 return list;
	 }
	  //Search user
	 public YahooUser searchUser(String id) {
		 for (int i = 0; i<usersOnList.size();i++) {
			if (usersOnList.get(i).getId().toLowerCase().toString().equals(id)) {
				return usersOnList.get(i);
			}
		 }
		 return new YahooUser("inexistent");
	 }
	 
	 //conference Log
	 public static void LogCommand(SessionEvent ev) {
		 //do sql logging here..:P
		 //output.. command
		 try {
			LostBot.session.sendConferenceMessage(LostBot.debugChan, "[" + ev.getTimestamp() + "] Received msg from (" + ev.getFrom() + "): " + ev.getMessage());
		} catch (Exception e) {
			System.out.println("Conference debugchan failed:(");
		}
	 }
	 public static void LogOther(String msg) {
		 //do sql logging here..:P
		 //output.. command
		 try {
			LostBot.session.sendConferenceMessage(LostBot.debugChan, msg);
		} catch (Exception e) {
			System.out.println("Conference debugchan failed:(");
		}
	 }
		 
	 //Simple response..
	 public static void Raspunde(String user, String text) {
		 try {
			LostBot.session.sendMessage(user, text);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 //Move this to Users..?
	 //verifyUser
	 public static boolean verifyUser(String usernameID, int level, int points) {
		Verify v = Users.verifyUser(usernameID, level, points);
		if (v.equals(Verify.NOLVL)) {
			Raspunde(usernameID, "Nivelul tau este prea mic pentru aceasta functie");
			return false;
		} else if (v.equals(Verify.NOPOINTS)) {
			Raspunde(usernameID, "Nu ai destule puncte pentru aceasta functie..");
			return false;
		} else if (v.equals(Verify.BANNED)) {
			return false;
		}
		
		return true;
		//IF DBPROBLEM.. well.. too bad:P
	 }
	 
	 //Verify User... Write text
	 public static void verifyUserWrite(String usernameID, int level, String text) {
			Verify v = Users.verifyUser(usernameID, level, 0);
			if (v.equals(Verify.OK)) {
				Raspunde(usernameID,text);
			}
			//IF DBPROBLEM.. well.. too bad:P
		 }
}
