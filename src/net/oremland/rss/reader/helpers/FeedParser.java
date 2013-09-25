/***********************************************************************************************************************
 * ==========================================
 *
 * Copyright (C) 2013 by Paul Oremland
 * http://www.linkedin.com/in/pauloremland
 * https://github.com/poremland
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************/
package net.oremland.rss.reader.helpers;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.*;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

import java.util.*;
import java.io.*;

import net.oremland.rss.reader.models.FeedItem;

public class FeedParser
	extends
		AsyncTask<String, Integer, List<FeedItem>>
{
	private OnParseListener listener = null;
	private Exception exception = null;

	public interface OnParseListener
	{
		void onProgress(int progress);
		void onComplete(List<FeedItem> items);
		void onError(Exception exception);
	}

	public void setOnParseListener(OnParseListener listener)
	{
		this.listener = listener;
	}

	@Override
	protected List<FeedItem> doInBackground(String... data)
	{
		if(this.listener != null && data != null)
		{
			try
			{
				return parse(data[0]);
			}
			catch(Exception e)
			{
				this.exception = e;
			}
		}
		return new ArrayList<FeedItem>();
	}

	@Override
	protected void onProgressUpdate(Integer... progress)
	{
		this.listener.onProgress(progress[0]);
	}

	@Override
	protected void onPostExecute(List<FeedItem> items)
	{
		if(exception != null)
		{
			this.listener.onError(this.exception);
			return;
		}
		this.listener.onComplete(items);
	}

	private List<FeedItem> parse(String feedXml)
	{
		List<SyndEntry> entries = this.entriesForFeed(feedXml);
		return this.convertEntriesListItemList(entries);
	}

	private List<FeedItem> convertEntriesListItemList(List<SyndEntry> entries)
	{
		List<FeedItem> map = new ArrayList<FeedItem>();

		for(SyndEntry entry : entries)
		{
			FeedItem item = this.convertEntryToFeedItem(entry);
			if(item != null)
			{
				map.add(item);
			}
		}

		return map;
	}

	private FeedItem convertEntryToFeedItem(SyndEntry entry)
	{
		if(entry != null)
		{
			String content = this.getEntryContent(entry);
			String description = entry.getDescription() == null ? content : entry.getDescription().getValue();
			String title = entry.getTitle();
			String url = entry.getLink();
			Date date = entry.getUpdatedDate() == null ? entry.getPublishedDate() : entry.getUpdatedDate();
			return new FeedItem(content, description, title, url, date);
		}
		return null;
	}

	private String getEntryContent(SyndEntry entry)
	{
		List contents = entry.getContents();
		StringBuffer buffer = new StringBuffer();
		for(int index=0;contents != null && index < contents.size();index++)
		{
			SyndContent content = (SyndContent)contents.get(index);
			buffer.append(content.getValue());
		}

		return (buffer.length() == 0) ? entry.getDescription().getValue() : buffer.toString();
	}

	private List<SyndEntry> entriesForFeed(String xml)
	{
		ArrayList<SyndEntry> entries = new ArrayList<SyndEntry>();
		XmlReader reader = this.xmlReaderForFeed(xml);
		if(reader != null)
		{
			try
			{
				SyndFeed feed = new SyndFeedInput().build(reader);

				for (Iterator i = feed.getEntries().iterator(); i.hasNext();)
				{
					entries.add((SyndEntry)i.next());
				}
			}
			catch(Exception exception)
			{
				exception.printStackTrace();
			}
			finally
			{
				if(reader != null)
				{
					try
					{
						reader.close();
					}
					catch(IOException ioException)
					{
						ioException.printStackTrace();
					}
				}
			}
		}
		return entries;
	}

	private XmlReader xmlReaderForFeed(String xml)
	{
		if(!TextUtils.isEmpty(xml))
		{
			try
			{
				InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
				return new XmlReader(stream, true);
			}
			catch(Exception exception)
			{
				exception.printStackTrace();
			}
		}
		return null;
	}
}
