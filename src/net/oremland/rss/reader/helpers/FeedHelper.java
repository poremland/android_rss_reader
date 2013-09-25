package net.oremland.rss.reader.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.*;
import java.util.*;

import net.oremland.rss.reader.MainApplication;
import net.oremland.rss.reader.models.*;

public class FeedHelper
{
	private final static String PREFERENCE = "net.oremland.rss.reader.FEED_PREFERENCE";
	public final static String FEED_LIST = "FEED_LIST";

	public void addFeed(Feed feed)
	{
		if(feed != null)
		{
			SharedPreferences prefs = this.getSharedPreferences();
			ArrayList<Feed> feeds = this.getSavedList(prefs);
			if(!feeds.contains(feed))
			{
				feeds.add(feed);
				this.saveList(feeds, prefs);
			}
		}
	}

	public void removeFeed(Feed feed)
	{
		if(feed != null)
		{
			SharedPreferences prefs = this.getSharedPreferences();
			ArrayList<Feed> feeds = this.getSavedList(prefs);
			if(feeds.contains(feed))
			{
				feeds.remove(feed);
				this.saveList(feeds, prefs);
			}
		}
	}

	public List<Feed> getFeeds()
	{
		SharedPreferences prefs = this.getSharedPreferences();
		return this.getSavedList(prefs);
	}

	private ArrayList<Feed> getSavedList(SharedPreferences prefs)
	{
		ArrayList<Feed> feeds = new ArrayList<Feed>();
		if(prefs != null)
		{
			String feedList = prefs.getString(FEED_LIST, null);
			if(feedList != null)
			{
				feeds = this.feedListFromJson(feedList);
			}
		}
		return feeds;
	}

	private void saveList(ArrayList<Feed> feeds, SharedPreferences prefs)
	{
		if(feeds != null && prefs != null)
		{
			SharedPreferences.Editor editor = prefs.edit();
			if(editor != null)
			{
				String json = this.feedListToJson(feeds);
				editor.putString(FEED_LIST, json);
				editor.commit();
			}
		}
	}

	protected SharedPreferences getSharedPreferences()
	{
		Context context = MainApplication.getContext();
		return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
	}

	private String feedListToJson(ArrayList<Feed> feeds)
	{
		Gson gson = new Gson();
		return gson.toJson(feeds);
	}

	private ArrayList<Feed> feedListFromJson(String json)
	{
		Type listType = new TypeToken<ArrayList<Feed>>(){}.getType();
		Gson gson = new Gson();
		return gson.fromJson(json, listType);
	}
}
