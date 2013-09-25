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
import java.text.*;
import java.util.*;

public class FeedItem
	implements
		BaseModel,
		Comparable<FeedItem>,
		Serializable
{
	private String content = null;
	private String description = null;
	private String title = null;
	private String url = null;
	private Date date = null;

	public String getKey() { return this.getDateWithFormat("yyyy-MM-dd HH:mm:ss.SSS"); }
	public String getContent() { return this.content; }
	public String getDescription() { return this.description; }
	public String getTitle() { return this.title; }
	public String getUrl() { return this.url; }
	public Date getDate() { return this.date; }
	public String getDateAsString()
	{
		DateFormat formatter = DateFormat.getDateTimeInstance();
		return formatter.format(this.getDate());
	}

	public String getDateWithFormat(String format)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
		return formatter.format(this.getDate());
	}

	private FeedItem()
	{
	}

	public FeedItem(String content, String description, String title, String url, Date date)
	{
		this.content = content;
		this.description = description;
		this.title = title;
		this.url = url;
		this.date = date == null ? new Date(0) : date;
	}

	@Override
	public int compareTo(FeedItem other)
	{
		if(other == null)
		{
			return 1;
		}

		if(this.equalsFeedItemWithoutDate(other))
		{
			return this.getDate().compareTo(other.getDate());
		}

		int titleComparison = other.getTitle().toLowerCase().compareTo(this.getTitle().toLowerCase());
		int dateComparison = this.getDate().compareTo(other.getDate());

		return dateComparison == 0 ? titleComparison : dateComparison;
		
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean isAFeedItem = obj != null && obj.getClass().equals(FeedItem.class);
		boolean isSameFeedItem = isAFeedItem && this.equalsFeedItem((FeedItem)obj);

		return isAFeedItem && isSameFeedItem;
	}

	private boolean equalsFeedItem(FeedItem other)
	{
		return other != null
			&& this.equalsFeedItemWithoutDate(other)
			&& other.getDate().equals(this.getDate());
	}

	private boolean equalsFeedItemWithoutDate(FeedItem other)
	{
		return other != null
			&& other.getContent().equals(this.getContent())
			&& other.getDescription().equals(this.getDescription())
			&& other.getTitle().equals(this.getTitle())
			&& other.getUrl().equals(this.getUrl());
	}
}
