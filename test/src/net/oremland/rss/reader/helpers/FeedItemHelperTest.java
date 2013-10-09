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

import android.test.AndroidTestCase;

import java.text.*;
import java.util.*;

import net.oremland.rss.reader.fragments.BaseListFragment.OnModelsLoadedListener;
import net.oremland.rss.reader.models.*;

public class FeedItemHelperTest
		extends
			AndroidTestCase
{
 	private List<FeedItem> models = Arrays.asList(new FeedItem[] { new FeedItem("Boo", "Foo", "Bar", "Baz", new Date()) });
	private FeedItemHelper helper;
 	private OnModelsLoadedListener<FeedItem> modelsLoadedListener = null;
	private boolean executeWasCalled = false;
	private boolean shouldReturnCachedItems = false;
 	private boolean modelsLoadedListenerWasCalled = false;
	private final static String url = "http://foo";
 	private List<FeedItem> loadedModels = null;
	private int cachedFeedItems = 0;

	public void setUp()
	{
		helper = this.createHelper();
		modelsLoadedListener = this.createListener();
		executeWasCalled = false;
		modelsLoadedListenerWasCalled = false;
		shouldReturnCachedItems = false;
		cachedFeedItems = 0;
	}

	private FeedItemHelper createHelper()
	{
		return new FeedItemHelper()
		{
			@Override
			protected AsyncHttpDownloader getDownloader()
 			{
				return createDownloader();
 			}

			@Override
			protected FeedItemCache cacheForUrl(String url)
			{
				return new FeedItemCache()
				{
					@Override
					public void add(FeedItem item)
					{
						cachedFeedItems++;
					}

					@Override
					public List<FeedItem> asList()
					{
						return shouldReturnCachedItems ? models : new ArrayList<FeedItem>();
					}
				};
				
			}
		};
	}

	private AsyncHttpDownloader createDownloader()
	{
		return new AsyncHttpDownloader()
		{
			@Override
			protected byte[] doInBackground(String... urls)
			{
				executeWasCalled = true;
				assertEquals(url, urls[0]);
				try
				{
					return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rss version=\"2.0\"><channel><item><title>t</title><link>l</link><description>d</description></item></channel></rss>".getBytes("UTF-8");
				}
				catch(Exception e)
				{
				}
				return new byte[0];
			}
		};
	}

 	private OnModelsLoadedListener<FeedItem> createListener()
	{
		return new OnModelsLoadedListener<FeedItem>()
		{
			@Override
			public void onModelsLoaded(List<FeedItem> models)
			{
				modelsLoadedListenerWasCalled = true;
			}
		};
	}
 
	public void test_getFeedItems_DoesNotExecuteHttpRequest_WhenListenerIsNull()
 	{
		helper.getFeedItems(url, null);
		assertFalse(executeWasCalled);
 	}
 
	public void test_getFeedItems_DoesNotExecuteHttpRequest_WhenUrlIsNull()
 	{
 		helper.getFeedItems(null, modelsLoadedListener);
		assertFalse(executeWasCalled);
 	}
 
	public void test_getFeedItems_DoesNotExecuteHttpRequest_WhenUrlIsEmpty()
 	{
 		helper.getFeedItems("", modelsLoadedListener);
		assertFalse(executeWasCalled);
 	}

	public void test_getFeedItems_PassesCachedFeedItemsToListener()
	{
		shouldReturnCachedItems = true;
 		helper.getFeedItems(url, modelsLoadedListener);
		assertFalse(executeWasCalled);
		assertTrue(modelsLoadedListenerWasCalled);
	}
 
	public void test_getFeedItems_ExecutesHttpRequest()
 	{
 		helper.getFeedItems(url, modelsLoadedListener);
		this.sleepFor(250);
		assertTrue(executeWasCalled);
 	}
 
	public void test_getFeedItems_PassesFeedItemsToListener()
 	{
 		helper.getFeedItems(url, modelsLoadedListener);
		this.sleepFor(250);
		assertTrue(modelsLoadedListenerWasCalled);
	}

	public void test_getFeedItems_CachesDownloadedFeedItems()
	{
		// TODO: figure out how to test correctly
	}

	private void sleepFor(int milliseconds)
	{
		try
		{
			Thread.sleep(milliseconds);
		}
		catch(Exception exception)
		{
			fail();
		}
 	}
 }
