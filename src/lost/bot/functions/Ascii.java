package lost.bot.functions;

import java.io.IOException;

import lost.bot.LostBot;

import org.openymsg.network.event.SessionEvent;

public class Ascii {
	public static String Heart() {
		return "        @@@@@@           @@@@@@\n"+
"      @@@@@@@@@@       @@@@@@@@@@\n"+
"    @@@@@@@@@@@@@@   @@@@@@@@@@@@@@\n"+
"  @@@@@@@@@@@@@@@@@ @@@@@@@@@@@@@@@@@\n"+
" @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"+
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"+
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"+
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"+
" @@@@@@@@@@@@@@@@@:)@@@@@@@@@@@@@@@\n"+
"  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"+
"   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"+
"    @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"+
"      @@@@@@@@@@@@@@@@@@@@@@@@@@@\n"+
"        @@@@@@@@@@@@@@@@@@@@@@@\n"+
"          @@@@@@@@@@@@@@@@@@@\n"+
"            @@@@@@@@@@@@@@@\n"+
"              @@@@@@@@@@@\n"+
"                @@@@@@@\n"+
"                  @@@\n"+
"                   @";
	}
	public static String textQuestion() {
		return "";
	}
	
	public static void HandleAsciiCommands(SessionEvent ev) {
		String goodMsg = ev.getMessage();
		goodMsg = goodMsg.replaceAll("</font>", "");
		
		int start = goodMsg.indexOf("!ascii");
		String newstr = goodMsg.substring(start);
		String msg[] =  newstr.split(" ", 3);
		
		try {
			if (msg[1].toLowerCase().equalsIgnoreCase("inima")) {
					LostBot.session.sendMessage(msg[2], Heart());
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
