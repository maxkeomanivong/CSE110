package com.learn2crack;

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
 ArrayList<String> uidList = new ArrayList<String>();
 ArrayList<String> nameList = new ArrayList<String>();
 
 //Array lists to hold the information of the restaurant profiles
 ArrayList<String> restIDList = new ArrayList<String>();
 ArrayList<String> resnameList = new ArrayList<String>();
 ArrayList<String> locList = new ArrayList<String>();
 ArrayList<String> phoneList = new ArrayList<String>();
 ArrayList<String> callforList = new ArrayList<String>();
 ArrayList<String> descList = new ArrayList<String>();
 
 ArrayList<String> timeList = new ArrayList<String>();
 
 int flag =0;
 
 // The URL of the PHP script to be executed
 String phpScript = null;
 
 // Flags to check what information is being parsed by XMLParser
 Boolean linkFlag = false;
 Boolean IDFlag = false;
 Boolean smashFlag= false;
 Boolean passFlag = false;
 Boolean resnameFlag = false;
 
 // Flags to check what restaurant information is being parsed by XMLParser
 Boolean nameFlag = false;
 Boolean locFlag = false;
 Boolean phoneFlag = false;
 Boolean callforFlag = false;
 Boolean descFlag = false;
 
 Boolean timeFlag = false;
 Boolean uidFlag = false;
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
    public ArrayList<String> getUID()
    {
     if(uidList.size()==0)
     {
      return null;
     }
     return uidList;
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
    public ArrayList<String> getResname()
    {
     if(resnameList.size()==0)
     {
      return null;
     }
     return resnameList;
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
    public ArrayList<String> getTime()
    {
     if(timeList.size()==0)
     {
      return null;
     }
     return timeList;
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
     * Getter method to obtain the restaurant name
     */
    public ArrayList<String> getNames()
    {
     if(nameList.size()==0)
     {
      return null;
     }
     return nameList;
    }
    /*
     * Getter method to obtain the restaurant address
     */
    public ArrayList<String> getAddress()
    {
     if(locList.size()==0)
     {
      return null;
     }
     return locList;
    }
    /*
     * Getter method to obtain the restaurant number
     */
    public ArrayList<String> getNumbers()
    {
     if(phoneList.size()==0)
     {
      return null;
     }
     return phoneList;
    }
    /*
     * Getter method to obtain the call for
     */
    public ArrayList<String> getCallfor()
    {
     if(callforList.size()==0)
     {
      return null;
     }
     return callforList;
    }
    /*
     * Getter method to obtain the call for
     */
    public ArrayList<String> getDescription()
    {
     if(descList.size()==0)
     {
      return null;
     }
     return descList;
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
             if(name.equals("id")){
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
             else if(name.equals("name"))
             {
              nameFlag = true;
              result = readName(parser);
             }
             else if(name.equals("address"))
             {
              locFlag = true;
              result = readLoc(parser);
             }
             else if(name.equals("number"))
             {
              phoneFlag = true;
              result = readNumber(parser);
             }
             else if(name.equals("callfor"))
             {
              callforFlag = true;
              result = readCallfor(parser);
             }
             else if(name.equals("time"))
             {
              timeFlag = true;
              result = readTime(parser);
             }
             else if(name.equals("description"))
             {
              descFlag = true;
              result = readDescription(parser);
             }
             else if(name.equals("uid"))
             {
              uidFlag = true;
              result = readUID(parser);
             }
             else if(name.equals("resname"))
             {
              resnameFlag = true;
              result = readResname(parser);
             }
             else {
                 skip(parser);
             }
         }
         return result;
     }
     // Layer that reads the title
     private String readID(XmlPullParser parser) throws IOException, XmlPullParserException {
         parser.require(XmlPullParser.START_TAG, null, "id");
         String title = readText(parser);
         parser.require(XmlPullParser.END_TAG, null, "id");
         IDFlag = false;
         return title;
     }
  // Layer that reads the link
     private String readTime(XmlPullParser parser) throws IOException, XmlPullParserException {
         parser.require(XmlPullParser.START_TAG, null, "time");
         String title = readText(parser);
         parser.require(XmlPullParser.END_TAG, null, "time");
         timeFlag = false;
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
     // Parser that reads the description of the item
     private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
         parser.require(XmlPullParser.START_TAG, null, "name");
         String title = readText(parser);
         parser.require(XmlPullParser.END_TAG, null, "name");
         nameFlag = false;
         return title;
     }
     // Parser that reads the description of the item
     private String readLoc(XmlPullParser parser) throws IOException, XmlPullParserException {
         parser.require(XmlPullParser.START_TAG, null, "address");
         String title = readText(parser);
         parser.require(XmlPullParser.END_TAG, null, "address");
         locFlag = false;
         return title;
     }
     // Parser that reads the description of the item
     private String readNumber(XmlPullParser parser) throws IOException, XmlPullParserException {
         parser.require(XmlPullParser.START_TAG, null, "number");
         String title = readText(parser);
         parser.require(XmlPullParser.END_TAG, null, "number");
         phoneFlag = false;
         return title;
     }
     // Parser that reads the description of the item
     private String readCallfor(XmlPullParser parser) throws IOException, XmlPullParserException {
         parser.require(XmlPullParser.START_TAG, null, "callfor");
         String title = readText(parser);
         parser.require(XmlPullParser.END_TAG, null, "callfor");
         callforFlag = false;
         return title;
     }
     private String readResname(XmlPullParser parser) throws IOException, XmlPullParserException {
         parser.require(XmlPullParser.START_TAG, null, "resname");
         String title = readText(parser);
         parser.require(XmlPullParser.END_TAG, null, "resname");
         resnameFlag = false;
         return title;
     }
     // Parser that reads the description of the item
     private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
         parser.require(XmlPullParser.START_TAG, null, "description");
         String title = readText(parser);
         parser.require(XmlPullParser.END_TAG, null, "description");
         descFlag = false;
         return title;
     }
     private String readUID(XmlPullParser parser) throws IOException, XmlPullParserException {
         parser.require(XmlPullParser.START_TAG, null, "uid");
         String title = readText(parser);
         parser.require(XmlPullParser.END_TAG, null, "uid");
         uidFlag = false;
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
         // Add the restaurant name
         if(nameFlag == true)
         {
          nameList.add(result);
         }
         // Add the location of the restaurant
         if(locFlag == true)
         {
          locList.add(result);
         }
         // Add the phone number of the restaurant
         if(phoneFlag == true)
         {
          phoneList.add(result);
         }
         // Add the call for name of the restaurant
         if(callforFlag == true)
         {
          callforList.add(result);
         }
         if(timeFlag == true)
         {
          timeList.add(result);
         }
         // Add the description of the restaurant
         if(descFlag == true)
         {
          descList.add(result);
         }
         if(uidFlag ==true)
         {
          uidList.add(result);
         }
         if(resnameFlag ==true)
         {
          resnameList.add(result);
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