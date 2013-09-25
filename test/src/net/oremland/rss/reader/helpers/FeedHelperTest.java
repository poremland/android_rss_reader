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

import android.content.SharedPreferences;
import android.test.AndroidTestCase;

import com.google.gson.Gson;

import java.text.*;
import java.util.*;

import net.oremland.rss.reader.models.*;
import net.oremland.rss.reader.mock.*;

public class FeedHelperTest
		extends
			AndroidTestCase
{
	private FeedHelper helper;
	private Feed existingFeed = new Feed("Foo", "Bar");
	private Feed newFeed = new Feed("Foo1", "Bar1");

	private SharedPreferencesMock mockPreferences = null;

	public void setUp()
	{
		mockPreferences = new SharedPreferencesMock();
		helper = this.createHelper();

		ArrayList<Feed> feeds = new ArrayList<Feed>();
		feeds.add(existingFeed);
		SharedPreferences.Editor editor = mockPreferences.edit();
		editor.putString(FeedHelper.FEED_LIST, new Gson().toJson(feeds));
		editor.commit();
	}

	private FeedHelper createHelper()
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

	public void test_addFeed_DoesNotAddFeed_WhenFeedIsNull()
	{
		this.clearPreferences();
		helper.addFeed(null);
		assertEquals(0, helper.getFeeds().size());
	}

	public void test_addFeed_DoesNotAddFeed_WhenFeedAlreadyExistsInList()
	{
		helper.addFeed(existingFeed);
		assertEquals(1, helper.getFeeds().size());
	}

	public void test_addFeed_AddsFeed_WhenInListIsEmpty()
	{
		this.clearPreferences();
		helper.addFeed(existingFeed);
		assertEquals(1, helper.getFeeds().size());
	}

	public void test_addFeed_AddsFeed_WhenFeedDoesNotAlreadyExistInList()
	{
		helper.addFeed(newFeed);
		assertEquals(2, helper.getFeeds().size());
	}

	public void test_getFeeds_ReturnsEmptyList_WhenThereIsNoSavedList()
	{
		this.clearPreferences();
		assertNotNull(helper.getFeeds());
		assertEquals(0, helper.getFeeds().size());
	}

	public void test_getFeeds_ReturnsSavedFeeds()
	{
		assertNotNull(helper.getFeeds());
		assertEquals(1, helper.getFeeds().size());
	}

	public void test_removeFeed_DoesNothing_WhenFeedIsNull()
	{
		assertNotNull(helper.getFeeds());
		helper.removeFeed(null);
		assertEquals(1, helper.getFeeds().size());
	}

	public void test_removeFeed_DoesNothing_WhenFeedIsNotAlreadySaved()
	{
		assertNotNull(helper.getFeeds());
		String timestamp = new SimpleDateFormat("dd-MM-yyyy HHmmss").format(new Date());
		String name = "name: " + timestamp;
		String url = "url: " + timestamp;
		helper.removeFeed(new Feed(name, url));
		assertEquals(1, helper.getFeeds().size());
	}

	public void test_removeFeed_RemovesFeed()
	{
		helper.removeFeed(existingFeed);
		assertEquals(0, helper.getFeeds().size());
	}

	private void clearPreferences()
	{
		SharedPreferences.Editor editor = mockPreferences.edit();
		editor.clear();
		editor.commit();
	}
}
