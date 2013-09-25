package net.oremland.rss.reader.fragments;

import android.content.*;
import android.test.AndroidTestCase;

import com.google.gson.Gson;

import java.util.*;

import net.oremland.rss.reader.R;
import net.oremland.rss.reader.fragments.BaseListFragment.OnModelsLoadedListener;
import net.oremland.rss.reader.fragments.FeedsListFragment.OnFeedSelectedListener;
import net.oremland.rss.reader.helpers.*;
import net.oremland.rss.reader.mock.*;
import net.oremland.rss.reader.models.*;

public class FeedsListFragmentTest
	extends
		AndroidTestCase
{
	private List<Feed> models = Arrays.asList(new Feed[] { new Feed("Foo", "Bar") });
	private FeedsListFragment fragment = null;
	private SharedPreferencesMock mockPreferences = null;
	private boolean methodWasCalled = false;

	public void setUp()
	{
		methodWasCalled = false;
		mockPreferences = new SharedPreferencesMock();
		SharedPreferences.Editor editor = mockPreferences.edit();
		editor.putString(FeedHelper.FEED_LIST, new Gson().toJson(new ArrayList(models)));
		editor.commit();
		fragment = this.createFragment();
	}

	private FeedsListFragment createFragment()
	{
		return new FeedsListFragment()
		{
			@Override
			protected Context getContext()
			{
				return mContext;
			}

			@Override
			public FeedHelper getFeedHelper()
			{
				return new FeedHelper()
				{
					@Override
					protected SharedPreferences getSharedPreferences()
					{
						return mockPreferences;
					}
				};
			}
		};
	}

	public void test_getLayoutId_ReturnsFeedsLayoutId()
	{
		assertEquals(R.layout.feeds, fragment.getLayoutId());
	}

	public void test_removeFeed_RemovesFeed()
	{
		fragment.removeFeed(models.get(0));
		assertEquals(0, fragment.getFeedHelper().getFeeds().size());
	}

	public void test_displayModel_CallsOnFeedSelectedOnListener()
	{
		OnFeedSelectedListener listener = new OnFeedSelectedListener()
		{
			public void onFeedSelected(Feed feed)
			{
				assertSame(models.get(0), feed);
				methodWasCalled = true;
			}
		};
		fragment.displayModel(models.get(0));
	}

	public void test_displayModel_DoesNotCallOnFeedSelectedOnListener_WhenListenerIsNull()
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

	public void test_createAdapter_CreatesFeedsAdapter()
	{
		FeedsAdapter adapter = fragment.createAdapter();
		assertNotNull(adapter);
		assertTrue(adapter instanceof FeedsAdapter);
	}

	public void test_loadModelList_DoesNotPassFeedsToListener_WhenListenerIsNull()
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

	public void test_loadModelList_PassesFeedsToListener()
	{
		OnModelsLoadedListener<Feed> listener = new OnModelsLoadedListener<Feed>()
		{
			@Override
			public void onModelsLoaded(List<Feed> list)
			{
				assertEquals(models.size(), list.size());
				methodWasCalled = true;
			}
		};
		fragment.loadModelList(listener);
	}
}
