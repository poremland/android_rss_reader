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

import java.util.*;

import net.oremland.rss.reader.models.*;

public class FeedItemCache
{
	private final static int expirationMilliseconds = 1000 * (15 * 60);
	private ArrayList<CacheItem> items = new ArrayList<CacheItem>();

	public FeedItemCache()
	{
	}

	protected int expireIfOlderThanMilliseconds()
	{
		return expirationMilliseconds;
	}

	public int size()
	{
		return this.asList().size();
	}

	
	public synchronized void add(FeedItem item)
	{
		if(!items.contains(new CacheItem(item)))
		{
			items.add(new CacheItem(item));
		}
	}

	public synchronized List<FeedItem> asList()
	{
		ArrayList<FeedItem> list = new ArrayList<FeedItem>();
		ArrayList<CacheItem> purged = new ArrayList<CacheItem>();
		for(CacheItem item : items)
		{
			if(!this.isExpired(item))
			{
				purged.add(item);
				list.add(item.item);
			}
		}
		items = purged;
		return list;
	}

	private boolean isExpired(CacheItem item)
	{
		long diff = new Date().getTime() - item.cachedAt.getTime();
		return diff >= this.expireIfOlderThanMilliseconds();
	}

	private class CacheItem
	{
		public final Date cachedAt = new Date();
		public final FeedItem item;

		public CacheItem(FeedItem item)
		{
			this.item = item;
		}

		@Override
		public boolean equals(Object obj)
		{
			if(obj == null)
			{
				return false;
			}

			if(obj instanceof CacheItem)
			{
				return ((CacheItem)obj).item.equals(this.item);
			}

			return false;
		}
	}
}
