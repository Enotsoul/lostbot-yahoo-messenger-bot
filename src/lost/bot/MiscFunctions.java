package lost.bot;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;


public class MiscFunctions {
   private static Random rn = new Random();
   
   public static int rand(int lo, int hi)
   {
           int n = hi - lo + 1;
           int i = rn.nextInt() % n;
           if (i < 0)
                   i = -i;
           return lo + i;
   }
   
   public static String florinGoogleLink(String textToSearch) {
	   String toRemove = "Variante de răspuns";
	   String newString = textToSearch.replaceAll(toRemove, "");
	   
	   try {
		newString =   URLEncoder.encode(newString,"UTF-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	   return "http://www.google.com/search?client=ubuntu&channel=fs&q=" + newString +"&ie=utf-8&oe=utf-8";
   }
   public static void googleForMe() {

	    String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
	    String search = "stackoverflow";
	    String charset = "UTF-8";
	    /*
	    URL url = new URL(google + URLEncoder.encode(search, charset));
	    Reader reader = new InputStreamReader(url.openStream(), charset);
	   
	   * GoogleResult results = new Gson().fromJson(reader, Gson.class);

	    // Show title and URL of 1st result.
	    System.out.println(results.getResponseData().getResults().get(0).getTitle());
	    System.out.println(results.getResponseData().getResults().get(0).getUrl());
	    */
	
   }
   /*
   //google results class 
   public class GoogleResults {
	   
	    private ArrayList<Result> results;
	    public ArrayList<Result> getResults() { return results; }  
	  
	    static class Result 
	    {
	        private String url;
	        private String title;
	        public String getUrl() { return url; }
	        public String getTitle() { return title; }        
	    }
   }
   */
}
