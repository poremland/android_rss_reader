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
package net.oremland.rss.reader.fragments;

import android.content.*;
import android.test.AndroidTestCase;

import com.google.gson.Gson;

import java.util.*;

import net.oremland.rss.reader.R;
import net.oremland.rss.reader.fragments.BaseListFragment.OnModelsLoadedListener;
import net.oremland.rss.reader.fragments.FeedItemsListFragment.OnFeedItemSelectedListener;
import net.oremland.rss.reader.helpers.*;
import net.oremland.rss.reader.mock.*;
import net.oremland.rss.reader.models.*;
 
public class FeedItemsListFragmentTest
	extends
		AndroidTestCase
{
	private List<FeedItem> models = Arrays.asList(new FeedItem[] { new FeedItem("Boo", "Foo", "Bar", "Baz", new Date()) });
	private Feed feed = new Feed("test", "test");
	private FeedItemsListFragment fragment = null;
	private boolean getFeedItemsWasCalled = false;
	private boolean modelsLoadedListenerWasCalled = false;
	private boolean feedItemSelectedListenerWasCalled = false;
	private OnFeedItemSelectedListener feedItemSelectedListener = null;
	private OnModelsLoadedListener<FeedItem> modelsLoadedListener = null;
	private OnModelsLoadedListener<FeedItem> listenerSentToHelper = null;
	private String urlSentToHelper = null;

	public void setUp()
	{
		getFeedItemsWasCalled = false;
		modelsLoadedListenerWasCalled = false;
		feedItemSelectedListenerWasCalled = false;
		feedItemSelectedListener = this.createFeedItemSelectedListener();
		modelsLoadedListener = this.createModelsLoadedListener();
		listenerSentToHelper = null;
		urlSentToHelper = null;
		fragment = this.createFragment();
		fragment.setFeed(feed);
	}

	private FeedItemsListFragment createFragment()
	{
		return new FeedItemsListFragment()
		{
			@Override
			protected OnFeedItemSelectedListener getOnFeedItemSelectedListener()
			{
				return feedItemSelectedListener;
			}

			@Override
			protected Context getContext()
			{
				return mContext;
			}

			@Override
			protected FeedItemHelper createHelper()
			{
				return new FeedItemHelper()
				{
					@Override
					public void getFeedItems(String url, OnModelsLoadedListener<FeedItem> listener)
					{
						getFeedItemsWasCalled = true;
						urlSentToHelper = url;
						listenerSentToHelper = listener;
					}
				};
			}
		};
	}

	private OnFeedItemSelectedListener createFeedItemSelectedListener()
	{
		return new OnFeedItemSelectedListener()
		{
			public void onFeedItemSelected(FeedItem item)
			{
				assertNotNull(item);
				feedItemSelectedListenerWasCalled = true;
			}
		};
	}

	private OnModelsLoadedListener<FeedItem> createModelsLoadedListener()
	{
		return new OnModelsLoadedListener<FeedItem>()
		{
			@Override
			public void onModelsLoaded(List<FeedItem> list)
			{
				assertEquals(models.size(), list.size());
				modelsLoadedListenerWasCalled = true;
			}
		};
	}

	public void test_getLayoutId_ReturnsFeedItemsLayoutId()
	{
		assertEquals(R.layout.feed_items, fragment.getLayoutId());
	}

	public void test_setFeed_SetsFeed()
	{
		assertSame(feed, fragment.getFeed());
	}

	public void test_createAdapter_CreatesFeedItemsAdapter()
	{
		FeedItemsAdapter adapter = fragment.createAdapter();
		assertNotNull(adapter);
		assertTrue(adapter instanceof FeedItemsAdapter);
	}

	public void test_loadModelList_DoesNotGetFeedItems_WhenListenerIsNull()
	{
		fragment.loadModelList(null);
		assertFalse(getFeedItemsWasCalled);
	}

	public void test_loadModelList_DoesNotGetFeedItems_IfFeedIsNull()
	{
		fragment.setFeed(null);
		fragment.loadModelList(modelsLoadedListener);
		assertFalse(getFeedItemsWasCalled);
	}

	public void test_loadModelList_GetsFeedItems()
	{
		fragment.loadModelList(modelsLoadedListener);
		assertTrue(getFeedItemsWasCalled);
	}

	public void test_loadModelList_PassesModelsLoadedListenerToHelper()
	{
		fragment.loadModelList(modelsLoadedListener);
		assertSame(modelsLoadedListener, listenerSentToHelper);
	}
}
