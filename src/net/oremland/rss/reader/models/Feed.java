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
package net.oremland.rss.reader.models;

import java.io.Serializable;

public class Feed
	implements
		BaseModel,
		Comparable<Feed>,
		Serializable
{
	private String name = null;
	private String url = null;

	public String getKey() { return this.getName(); }
	public String getName() { return this.name; }
	public String getUrl() { return this.url; }

	public Feed(String name, String url)
	{
		this.name = name;
		this.url = url;
	}

	@Override
	public int compareTo(Feed other)
	{
		if(other == null)
		{
			return 1;
		}

		if(other.getName().equals(this.getName())
			&& other.getUrl().equals(this.getUrl()))
		{
			return 0;
		}

		return -1;
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean isAFeed = obj != null && obj.getClass().equals(Feed.class);
		boolean isSameFeed = isAFeed && this.compareTo((Feed)obj) == 0;

		return isAFeed && isSameFeed;
	}
}
