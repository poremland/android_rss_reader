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
