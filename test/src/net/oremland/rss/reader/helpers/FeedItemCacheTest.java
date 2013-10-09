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

import java.util.*;

import net.oremland.rss.reader.models.*;

public class FeedItemCacheTest
	extends
		AndroidTestCase
{
	private FeedItemCache cache = null;
	private int expirationMilliseconds = 1000 * (15 * 60);

	public void setUp()
	{
		expirationMilliseconds = 1000 * (15 * 60);
		cache = new FeedItemCache()
		{
			@Override
			protected int expireIfOlderThanMilliseconds()
			{
				return expirationMilliseconds;
			}
		};
	}

	public void test_instance_ReturnsSameInstance()
	{
		assertSame(FeedItemCache.instance(), FeedItemCache.instance());
	}

	public void test_size_ReturnsZeroWhenEmpty()
	{
		assertEquals(0, cache.size());
	}

	public void test_add_DoesNotAppendItemToList_WhenItemIsAlreadyInList()
	{
		FeedItem item = new FeedItem("Boo", "Foo", "Bar", "Baz", new Date());
		cache.add(item);
		cache.add(item);
		assertEquals(1, cache.size());
	}

	public void test_add_AppendsItemToList()
	{
		FeedItem item = new FeedItem("Boo", "Foo", "Bar", "Baz", new Date());
		cache.add(item);
		assertEquals(1, cache.size());
	}

	public void test_asList_ReturnsEmptyList_WhenCacheIsEmpty()
	{
		List<FeedItem> list = cache.asList();
		assertNotNull(list);
		assertEquals(0, list.size());
	}

	public void test_asList_ReturnsListWithFeedItems()
	{
		FeedItem item = new FeedItem("Boo", "Foo", "Bar", "Baz", new Date());
		cache.add(item);
		List<FeedItem> list = cache.asList();
		assertEquals(1, list.size());
		assertEquals(item, list.get(0));
	}

	public void test_asList_DoesNotIncludeExpiredFeedItems()
	{
		FeedItem item = new FeedItem("Boo", "Foo", "Bar", "Baz", new Date());
		cache.add(item);
		expirationMilliseconds = 10;
		sleepFor(15);
		List<FeedItem> list = cache.asList();
		assertEquals(0, list.size());
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
