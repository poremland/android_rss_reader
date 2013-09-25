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
	private boolean methodWasCalled = false;
	private boolean executeWasCalled = false;
	private boolean modelsLoadedListenerWasCalled = false;
	private boolean feedItemSelectedListenerWasCalled = false;
	private OnFeedItemSelectedListener feedItemSelectedListener = null;
	private OnModelsLoadedListener<FeedItem> modelsLoadedListener = null;

	public void setUp()
	{
		methodWasCalled = false;
		executeWasCalled = false;
		modelsLoadedListenerWasCalled = false;
		feedItemSelectedListenerWasCalled = false;
		feedItemSelectedListener = this.createFeedItemSelectedListener();
		modelsLoadedListener = this.createModelsLoadedListener();
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
			protected void parseItemsFromData(OnModelsLoadedListener<FeedItem> listener, String data)
			{
				listener.onModelsLoaded(models);
			}

			@Override
			protected AsyncHttpDownloader getDownloader()
			{
				return new AsyncHttpDownloader()
				{
					@Override
					protected byte[] doInBackground(String... urls)
					{
						executeWasCalled = true;
						assertEquals(feed.getUrl(), urls[0]);
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

	public void test_displayModel_CallsOnFeedItemSelectedOnListener()
	{
		fragment.displayModel(models.get(0));
		assertTrue(feedItemSelectedListenerWasCalled);
	}

	public void test_displayModel_DoesNotCallOnFeedItemSelectedOnListener_WhenListenerIsNull()
	{
		try
		{
			fragment.displayModel(models.get(0));
		}
		catch(Exception e)
		{
			fail();
		}
	}

	public void test_createAdapter_CreatesFeedItemsAdapter()
	{
		FeedItemsAdapter adapter = fragment.createAdapter();
		assertNotNull(adapter);
		assertTrue(adapter instanceof FeedItemsAdapter);
	}

	public void test_loadModelList_DoesNotPassFeedItemsToListener_WhenListenerIsNull()
	{
		try
		{
			fragment.loadModelList(null);
		}
		catch(Exception e)
		{
			fail();
		}
	}

	public void test_loadModelList_DoesNotExecuteHttpRequest_IfFeedIsNull()
	{
		fragment.setFeed(null);
		fragment.loadModelList(modelsLoadedListener);
		assertFalse(executeWasCalled);
	}

	public void test_loadModelList_ExecutesHttpRequestForFeedItems()
	{
		fragment.loadModelList(modelsLoadedListener);
		this.sleepFor(250);
		assertTrue(executeWasCalled);
	}

	public void test_loadModelList_PassesFeedItemsToListener()
	{
		fragment.loadModelList(modelsLoadedListener);
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
