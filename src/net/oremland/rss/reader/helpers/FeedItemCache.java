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
	private ArrayList<FeedItem> items = new ArrayList<FeedItem>();
	private static FeedItemCache instance;
	static
	{
		instance = new FeedItemCache();
	}

	protected FeedItemCache()
	{
	}

	public static FeedItemCache instance()
	{
		return instance;
	}

	public int size()
	{
		return items.size();
	}

	
	public synchronized void add(FeedItem item)
	{
		if(!items.contains(item))
		{
			items.add(item);
		}
	}

	public List<FeedItem> asList()
	{
		return new ArrayList<FeedItem>(items);
	}
}
