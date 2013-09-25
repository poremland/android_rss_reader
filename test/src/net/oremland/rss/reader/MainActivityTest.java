package net.oremland.rss.reader;

import android.os.Bundle;
import android.support.v4.app.*;
import android.test.ActivityInstrumentationTestCase2;

import net.oremland.rss.reader.fragments.*;
import net.oremland.rss.reader.models.*;

public class MainActivityTest
		extends
			ActivityInstrumentationTestCase2<MainActivity>
{
	private MainActivity activity;
	private boolean onRestoreInstanceStateWasCalled = false;
	private int displayedFragmentId;
	private Fragment displayedFragment = null;

	public MainActivityTest()
	{
		super("net.oremland.rss.reader", MainActivity.class);
	}

	public void setUp()
	{
		setActivityInitialTouchMode(false);
		displayedFragmentId = 0;
		displayedFragment = null;
		onRestoreInstanceStateWasCalled = false;
		activity = this.getActivity();
	}

	public void test_onCreate_ShowsAddFeedFragment_IfSavedInstanceStateIsNullAndThereAreNoSavedFeeds()
	{
		// TODO: test
	}

	public void test_onCreate_ShowsFeedsFragment_IfSavedInstanceStateIsNullAndThereAreSavedFeeds()
	{
		// TODO: test
	}

	public void test_onFeedSelected_DisplaysTheFeedItemsListFragment()
	{
		// TODO: test
	}

	public void test_onFeedSelected_SetsTheFeedOnTheFeedItemsListFragment()
	{
		// TODO: test
	}
}
