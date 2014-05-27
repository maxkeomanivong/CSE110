package com.androidsurya.customviews;


import android.app.Activity;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

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
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RssReader{
	String feedRSS = null;
	ArrayList<String> links = new ArrayList<String>();
    public ArrayList<String> getLinks(String RSS)
    {System.err.println("4");
    	feedRSS = RSS;
    	try {
    		System.err.println("4.5");
			links = new XMLParser().execute().get();
			System.err.println("5");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.err.println("6");
    	return links;
    }
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
	public class XMLParser extends AsyncTask<Void, Void, ArrayList<String>> {
		ArrayList<String> URLlist = new ArrayList<String>();
		int index = 0;
	    @Override
	    protected ArrayList<String> doInBackground(Void... voids) {
	        ArrayList<String> result = null;
	        try {
	            String feed = getRssFeed(feedRSS);
	            result = parse(feed);
	        } catch (XmlPullParserException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
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
	            if (name.equals("link")) {
	                result = readLink(parser);
	            } else {
	                skip(parser);
	            }
	        }
	        return result;
	    }
	    // Layer that reads the title
	    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
	        parser.require(XmlPullParser.START_TAG, null, "title");
	        String title = readText(parser);
	        parser.require(XmlPullParser.END_TAG, null, "title");
	        return title;
	    }
	    // Layer that reads the link
	    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
	        parser.require(XmlPullParser.START_TAG, null, "link");
	        String title = readText(parser);
	        parser.require(XmlPullParser.END_TAG, null, "link");
	        return title;
	    }
	    // Parser that reads the description of the item
	    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
	        parser.require(XmlPullParser.START_TAG, null, "description");
	        String title = readText(parser);
	        parser.require(XmlPullParser.END_TAG, null, "description");
	        return title;
	    }
	    // Reads the text of the URL
	    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	        String result = "";
	        if (parser.next() == XmlPullParser.TEXT) {
	            result = parser.getText();
	            parser.nextTag();
	        }
	        Log.d(result, "URL WRITTEN INTO ARRAYLIST");
	        URLlist.add(result);
	        Log.d("INDEX OF THE URL", Integer.toString(index));
	        Log.d(URLlist.get(index), "URL HAS BEEN ADDED TO THE LIST");
	        index++;
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