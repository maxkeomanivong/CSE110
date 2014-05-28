package com.androidsurya.customviews;

import android.os.AsyncTask;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/*
 * This class handles communication between the application and the server. 
 * To send and write information into the server and database, this class
 * executes PHP scripts stored on the server through the method execPHP.
 * To read the information from the server, an XML Parser is used (see 
 * XMLParser class)
 */
public class XMLHandler{
	// Array lists to hold the information of each image displayed on feed
	ArrayList<String> URLlist = new ArrayList<String>();
	ArrayList<String> IDList = new ArrayList<String>();
	ArrayList<String> SmashList = new ArrayList<String>();
	ArrayList<String> PassList = new ArrayList<String>();
	int flag =0;
	// The URL of the PHP script to be executed
	String phpScript = null;
	
	// Flags to check what information is being parsed by XMLParser
	Boolean linkFlag = false;
	Boolean IDFlag = false;
	Boolean smashFlag= false;
	Boolean passFlag = false;
	
	/*
	 * Method to execute the PHP script on the server
	 */
    public void execPHP(String script)
    {
    	phpScript = script;
    	new XMLParser().execute();
    }
    
    public int getFlag()
    {
    	return flag;
    	
    }
    
    public void resetFlag()
    {
    	flag=0;
    	
    }
    /*
     * Getter method to obtain the list of URLs parsed by XMLParser
     */
    public ArrayList<String> getURLS()
    {
    	if(URLlist.size()==0)
    	{
    		return null;
    	}
    	return URLlist;
    }
    
    /*
     * Getter method to obtain the list of database IDs associated with the URLs
     */
    public ArrayList<String> getIDs()
    {
    	if(IDList.size()==0)
    	{
    		return null;
    	}
    	return IDList;
    }
    
    /*
     * Getter method to obtain the likes associated with the URLs
     */
    public ArrayList<String> getSmashes()
    {
    	if(SmashList.size()==0)
    	{
    		return null;
    	}
    	return SmashList;
    }
    
    /*
     * Getter method to obtain the dislikes associated with the URLs
     */
    public ArrayList<String> getPasses()
    {
    	if(PassList.size()==0)
    	{
    		return null;
    	}
    	return PassList;
    }
    
    /*
     * Connects to the PHP script stored in the server and obtains RSS feed
     */
	public String getRssFeed(String FeedURL) throws IOException {
	    InputStream in = null;
	    String rssFeed = null;
	    try {
	        URL url = new URL(FeedURL);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        in = conn.getInputStream();
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        for (int count; (count = in.read(buffer)) != -1; ) {
	            out.write(buffer, 0, count);
	        }
	        byte[] response = out.toByteArray();
	        rssFeed = new String(response, "UTF-8");
	    } finally {
	        if (in != null) {
	            in.close();
	        }
	    }
	    return rssFeed;
	}
	// Parses the XML File fetched from the server
	private class XMLParser extends AsyncTask<Void, Void, ArrayList<String>> {
	    @Override
	    protected ArrayList<String> doInBackground(Void... voids) {
	        ArrayList<String> result = null;
	        try {
	            String feed = getRssFeed(phpScript);
	            result = parse(feed);
	        } catch (XmlPullParserException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        flag=1;
	        return result;
	    }
	    // Top layer of the parser
	    private ArrayList<String> parse(String rssFeed) throws XmlPullParserException, IOException {
	        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        XmlPullParser xpp = factory.newPullParser();
	        xpp.setInput(new StringReader(rssFeed));
	        xpp.nextTag();
	        return readRss(xpp);
	    }
	    // Layer that reads the RSS feed
	    private ArrayList<String> readRss(XmlPullParser parser)
	            throws XmlPullParserException, IOException {
	        ArrayList<String> items = new ArrayList<String>();
	        parser.require(XmlPullParser.START_TAG, null, "rss");
	        while (parser.next() != XmlPullParser.END_TAG) {
	            if (parser.getEventType() != XmlPullParser.START_TAG) {
	                continue;
	            }
	            String name = parser.getName();
	            if (name.equals("channel")) {
	                items.addAll(readChannel(parser));
	            } else {
	                skip(parser);
	            }
	        }
	        return items;
	    }
	    // Layer that reads the channel 
	    private ArrayList<String> readChannel(XmlPullParser parser)
	            throws IOException, XmlPullParserException {
	        ArrayList<String> items = new ArrayList<String>();
	        parser.require(XmlPullParser.START_TAG, null, "channel");
	        while (parser.next() != XmlPullParser.END_TAG) {
	            if (parser.getEventType() != XmlPullParser.START_TAG) {
	                continue;
	            }
	            String name = parser.getName();
	            if (name.equals("item")) {
	                items.add(readItem(parser));
	            } else {
	                skip(parser);
	            }
	        }
	        return items;
	    }
	    // Layer that reads the item
	    private String readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
	        String result = null;
	        parser.require(XmlPullParser.START_TAG, null, "item");
	        while (parser.next() != XmlPullParser.END_TAG) {
	            if (parser.getEventType() != XmlPullParser.START_TAG) {
	                continue;
	            }
	            String name = parser.getName();
	            if(name.equals("title")){
	            	IDFlag = true;
	            	result = readID(parser);
	            }
	            else if (name.equals("link")) {
	            	linkFlag = true;
	                result = readLink(parser);
	            }
	            else if (name.equals("smash")) {
	            	smashFlag = true;
	                result = readLikes(parser);
	            }
	            else if (name.equals("pass")) {
	            	passFlag = true;
	                result = readDislikes(parser);
	            }
	            else {
	                skip(parser);
	            }
	        }
	        return result;
	    }
	    // Layer that reads the title
	    private String readID(XmlPullParser parser) throws IOException, XmlPullParserException {
	        parser.require(XmlPullParser.START_TAG, null, "title");
	        String title = readText(parser);
	        parser.require(XmlPullParser.END_TAG, null, "title");
	        IDFlag = false;
	        return title;
	    }
	    // Layer that reads the link
	    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
	        parser.require(XmlPullParser.START_TAG, null, "link");
	        String title = readText(parser);
	        parser.require(XmlPullParser.END_TAG, null, "link");
	        linkFlag = false;
	        return title;
	    }
	    // Parser that reads the description of the item
	    private String readLikes(XmlPullParser parser) throws IOException, XmlPullParserException {
	        parser.require(XmlPullParser.START_TAG, null, "smash");
	        String title = readText(parser);
	        parser.require(XmlPullParser.END_TAG, null, "smash");
	        smashFlag = false;
	        return title;
	    }
	    // Parser that reads the description of the item
	    private String readDislikes(XmlPullParser parser) throws IOException, XmlPullParserException {
	        parser.require(XmlPullParser.START_TAG, null, "pass");
	        String title = readText(parser);
	        parser.require(XmlPullParser.END_TAG, null, "pass");
	        passFlag = false;
	        return title;
	    }
	    // Reads the text
	    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	        String result = "";
	        if (parser.next() == XmlPullParser.TEXT) {
	            result = parser.getText();
	            parser.nextTag();
	        }
	        // Add URL to the list
	        if(linkFlag == true)
	        {
	        	URLlist.add(result);
	        }
	        // Add ID to the ID list
	        if(IDFlag == true)
	        {
	        	IDList.add(result);
	        }
	        // Add smashes to the smashes list
	        if(smashFlag == true)
	        {
	        	SmashList.add(result);
	        }
	        // Add passes to the passes list
	        if(passFlag == true)
	        {
	        	PassList.add(result);
	        }
	        return result;
	    }
	    // Skip the tags
	    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            throw new IllegalStateException();
	        }
	        int depth = 1;
	        while (depth != 0) {
	            switch (parser.next()) {
	                case XmlPullParser.END_TAG:
	                    depth--;
	                    break;
	                case XmlPullParser.START_TAG:
	                    depth++;
	                    break;
	            }
	        }
	    }
	}
}