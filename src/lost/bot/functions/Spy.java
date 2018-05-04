package lost.bot.functions;

import java.io.IOException;
import java.util.ArrayList;

import lost.bot.LostBot;
import lost.bot.SessionHandler;

import org.openymsg.network.YahooUser;
import org.openymsg.network.event.SessionEvent;

public class Spy {
	
	public String toSpyUser,userRequested;
	public static ArrayList<Spy> spyList = new ArrayList<Spy>();
	
	public Spy(String toSpyUser, String userRequested) {
		super();
		this.toSpyUser = toSpyUser;
		this.userRequested = userRequested;
	}

	public String getToSpyUser() {
		return toSpyUser;
	}


	public void setToSpyUser(String toSpyUser) {
		this.toSpyUser = toSpyUser;
	}


	public String getUserRequested() {
		return userRequested;
	}


	public void setUserRequested(String userRequested) {
		this.userRequested = userRequested;
	}
	
	//this might give you multiple people :)
	 public static  ArrayList<Spy> searchUsers(String toSpy) {
		 ArrayList<Spy> spiesFound = new ArrayList<Spy>();
		 
		 for (int i = 0; i<spyList.size();i++) {
			if (spyList.get(i).getToSpyUser().toLowerCase().toString().equals(toSpy)) {
				spiesFound.add(spyList.get(i));
			}
		 }
		 
		 return spiesFound;
	 }
	 
	 public static void  addSpy(Spy spy) {
		 spyList.add(spy);
	 }
	 
	 public static void  removeSpy(Spy spy) {
		 for (int i = 0; i<spyList.size();i++) {
				if (spyList.get(i).getToSpyUser().equals(spy.getToSpyUser()) && spyList.get(i).getUserRequested().equals(spy.getUserRequested())) {
					spyList.remove(i);
				}
		 }
	 }
	
	public static void spyUser(SessionEvent ev) {
		ArrayList<Spy> spyList = searchUsers(ev.getFrom());
		
		if (!spyList.isEmpty()) {
			try {
				for (Spy s : spyList) {
					LostBot.session.sendMessage(s.userRequested, "Spying on (" +  s.getToSpyUser() + "): " + ev.getMessage());
				}
			} catch (IllegalStateException e) {
		
			} catch (IOException e) {
	
			}
		}
	}
	
	 public static String getSpyList() {
			String list = "";
			 for (int i = 0; i<spyList.size();i++) {
				 list += spyList.get(i).getUserRequested() + " il spioneaza pe "  + spyList.get(i).getToSpyUser() + " /!\\ ";
			 }
			 return list;
	}
	 
	public static void handleSpy(SessionEvent ev) {
		String goodMsg = ev.getMessage();
		goodMsg = goodMsg.replaceAll("</font>", "");
		
		int start = goodMsg.indexOf("!spy");
		String newstr = goodMsg.substring(start);
		String msg[] =  newstr.split(" ", 3);
		
		try {
			if (msg[1].toLowerCase().equalsIgnoreCase("start")) {
				if (SessionHandler.verifyUser(ev.getFrom(), 1, 10)) {
					if (Users.getUser(msg[2]).getLevel()>Users.getUser(ev.getFrom()).getLevel()) {
						LostBot.session.sendMessage(ev.getFrom(), "Nu poti spiona pe cineva cu un nivel mai mare ca al tau:)");
					} else {
						addSpy(new Spy(msg[2].toLowerCase(),ev.getFrom().toLowerCase()));
						LostBot.session.sendMessage(ev.getFrom(), "De acuma il voi spiona pe " +  msg[2] + " pentru tine. Ca sa nu il mai spionez trebuie sa scrii !spy stop " + msg[2]);
					}
				}
			} else if (msg[1].toLowerCase().equalsIgnoreCase("stop")) {
				removeSpy(new Spy(msg[2].toLowerCase(),ev.getFrom().toLowerCase()));
				LostBot.session.sendMessage(ev.getFrom(), "Gata, nu-l mai spionez pe " +  msg[2]);
			} else if (msg[1].toLowerCase().equalsIgnoreCase("list")) {
				if (SessionHandler.verifyUser(ev.getFrom(), 4, 0)) {
					LostBot.session.sendMessage(ev.getFrom(),"Poftim lista cu spionaje:");
					LostBot.session.sendMessage(ev.getFrom(),getSpyList());
				}
			} else {
				LostBot.session.sendMessage(ev.getFrom(),"Varianta corecta !spy <start/stop> <id>");
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
