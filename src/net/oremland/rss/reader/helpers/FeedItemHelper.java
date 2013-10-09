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

import android.util.Log;
import android.text.TextUtils;
 
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.*;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

import java.util.*;
import java.io.*;

import net.oremland.rss.reader.fragments.BaseListFragment.OnModelsLoadedListener;
import net.oremland.rss.reader.models.*;

public class FeedItemHelper
{
	private final static HashMap<String, FeedItemCache> caches = new HashMap<String, FeedItemCache>();

	public void getFeedItems(String url, OnModelsLoadedListener<FeedItem> listener)
	{
		FeedItemCache cache = this.cacheForUrl(url);
		if(cache.size() > 0)
		{
			listener.onModelsLoaded(cache.asList());
			return;
		}

		this.downloadFeedItems(url, listener);
	}

	private void downloadFeedItems(String url, OnModelsLoadedListener<FeedItem> listener)
	{
		if(!TextUtils.isEmpty(url) && listener != null)
		{
			AsyncHttpDownloader downloader = this.createDownloader(listener, url);
			downloader.execute(url);
		}
	}

	private AsyncHttpDownloader createDownloader(final OnModelsLoadedListener<FeedItem> listener, final String url)
	{
		AsyncHttpDownloader downloader = this.getDownloader();
		downloader.setOnDownloadListener(new AsyncHttpDownloader.OnDownloadListener()
		{
			public void onProgress(int progress)
			{
				onTaskProgress(listener, progress);
			}

			public void onComplete(byte[] result)
			{
				onDownloadComplete(listener, result, url);
			}

			public void onError(Exception exception)
			{
				onTaskError(listener, exception);
			}

		});
		return downloader;
	}

	protected AsyncHttpDownloader getDownloader()
	{
		return new AsyncHttpDownloader();
	}

	private void onTaskProgress(OnModelsLoadedListener<FeedItem> listener, int progress)
	{
	}

	private void onDownloadComplete(OnModelsLoadedListener<FeedItem> listener, byte[] result, String url)
	{
		if(listener != null
			&& result != null
			&& result.length > 0)
		{
			String data = new String(result);
			parseItemsFromData(listener, data, url);
		}
	}

	private void onTaskError(OnModelsLoadedListener<FeedItem> listener, Exception exception)
	{
		Log.e(this.getClass().getName(), exception.toString(), exception);
	}

	protected void parseItemsFromData(OnModelsLoadedListener<FeedItem> listener, String data, String url)
	{
		FeedParser parser = new FeedParser();
		parser.setOnParseListener(this.createOnParseListener(listener, url));
		parser.execute(data);
	}

	private FeedParser.OnParseListener createOnParseListener(final OnModelsLoadedListener<FeedItem> listener, final String url)
	{
		return new FeedParser.OnParseListener()
		{
			@Override
			public void onProgress(int progress)
			{
				onTaskProgress(listener, progress);
			}

			@Override
			public void onComplete(List<FeedItem> items)
			{
				onParseComplete(listener, items, url);
			}

			@Override
			public void onError(Exception exception)
			{
				onTaskError(listener, exception);
			}
		};
	}

	private void onParseComplete(OnModelsLoadedListener<FeedItem> listener, List<FeedItem> items, String url)
	{
		if(listener != null && items != null)
		{
			this.cacheFeedItems(url, items);
			listener.onModelsLoaded(items);
		}
	}

	private synchronized void cacheFeedItems(String url, List<FeedItem> items)
	{
		FeedItemCache cache = this.cacheForUrl(url);
		for(FeedItem item : items)
		{
			cache.add(item);
		}
	}

	protected synchronized FeedItemCache cacheForUrl(String url)
	{
		if(!caches.containsKey(url))
		{
			caches.put(url, new FeedItemCache());
		}

		return caches.get(url);
	}
}
