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
package net.oremland.rss.reader.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.text.TextUtils;
 
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.*;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

import java.util.*;
import java.io.*;

import net.oremland.rss.reader.R;
import net.oremland.rss.reader.helpers.*;
import net.oremland.rss.reader.models.*;

public class FeedItemsListFragment
	extends
		BaseListFragment<FeedItem, FeedItemsAdapter>
{
	public interface OnFeedItemSelectedListener
	{
		public void onFeedItemSelected(FeedItem item);
	}

	private OnFeedItemSelectedListener itemSelectedListener;
	protected OnFeedItemSelectedListener getOnFeedItemSelectedListener()
	{
		return itemSelectedListener;
	}
	private Feed feed = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		if(getActivity() instanceof OnFeedItemSelectedListener)
		{
			itemSelectedListener = (OnFeedItemSelectedListener)getActivity();
		}
	}

	@Override
	protected int getLayoutId()
	{
		return R.layout.feed_items;
	}

	public Feed getFeed()
	{
		return feed;
	}

	public void setFeed(Feed feed)
	{
		this.feed = feed;
		this.shouldUpdateModelList();
	}

	@Override
	protected void displayModel(FeedItem item)
	{
		if(getOnFeedItemSelectedListener() != null)
		{
			getOnFeedItemSelectedListener().onFeedItemSelected(item);
		}
	}

	@Override
	protected FeedItemsAdapter createAdapter()
	{
		return new FeedItemsAdapter(getContext(), R.layout.feed_items_row);
	}

	@Override
	protected void loadModelList(OnModelsLoadedListener<FeedItem> listener)
	{
		if(listener != null && this.feed != null)
		{
			FeedItemHelper helper = this.createHelper();
			helper.getFeedItems(this.feed.getUrl(), listener);
		}
	}

	protected FeedItemHelper createHelper()
	{
		return new FeedItemHelper();
	}

	@Override
	protected Comparator getListComparator()
	{
		return Collections.reverseOrder();
	}

	protected Context getContext()
	{
		return getActivity();
	}
}
