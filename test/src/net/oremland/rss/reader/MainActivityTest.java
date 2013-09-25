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
