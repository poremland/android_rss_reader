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
 	private boolean modelsLoadedListenerWasCalled = false;
	private final static String url = "http://foo";

	public void setUp()
	{
		helper = this.createHelper();
		modelsLoadedListener = this.createListener();
		executeWasCalled = false;
		modelsLoadedListenerWasCalled = false;
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
					return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><feed />".getBytes("UTF-8");
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
