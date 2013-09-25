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

import java.util.*;

import net.oremland.rss.reader.R;
import net.oremland.rss.reader.helpers.*;
import net.oremland.rss.reader.models.*;

import org.json.JSONArray;

public class FeedsListFragment
	extends
		BaseListFragment<Feed, FeedsAdapter>
{
	public interface OnFeedSelectedListener
	{
		public void onFeedSelected(Feed feed);
	}

	private OnFeedSelectedListener feedSelectedListener;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		if(getActivity() instanceof OnFeedSelectedListener)
		{
			feedSelectedListener = (OnFeedSelectedListener)getActivity();
		}
	}

	@Override
	protected int getLayoutId()
	{
		return R.layout.feeds;
	}

	@Override
	protected boolean canCacheModels()
	{
		return false;
	}

	public void removeFeed(Feed feed)
	{
		this.getFeedHelper().removeFeed(feed);
		this.removeModel(feed.getName());
	}

	protected void displayModel(Feed feed)
	{
		if(feedSelectedListener != null)
		{
			feedSelectedListener.onFeedSelected(feed);
		}
	}

	protected FeedsAdapter createAdapter()
	{
		return new FeedsAdapter(getContext(), R.layout.feeds_row);
	}

	protected void loadModelList(OnModelsLoadedListener<Feed> listener)
	{
		if(listener != null)
		{
			List<Feed> feeds = getFeedHelper().getFeeds();
			listener.onModelsLoaded(feeds);
		}
	}

	protected Context getContext()
	{
		return getActivity();
	}

	protected FeedHelper getFeedHelper()
	{
		 return new FeedHelper();
	}
}
