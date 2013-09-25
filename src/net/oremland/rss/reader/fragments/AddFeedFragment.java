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
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import net.oremland.rss.reader.R;
import net.oremland.rss.reader.helpers.FeedHelper;
import net.oremland.rss.reader.models.Feed;

public class AddFeedFragment
	extends
		Fragment
{
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.add_feed_fragment, container, false);
		this.setSubmitOnClickListener(view);
		return view;
	}

	private void setSubmitOnClickListener(View view)
	{
		Button button = (Button)view.findViewById(R.id.submit);
		if(button != null)
		{
			button.setOnClickListener(this.getSubmitOnClickListener());
		}

	}

	protected View.OnClickListener getSubmitOnClickListener()
	{
		return new OnClickListener()
		{
			public void onClick(View view)
			{
				submit(view);
			}
		};
	}

	private void submit(View view)
	{
		if(getView() != null)
		{
			String name = this.getEditTextValue(getView(), R.id.feed_name);
			String url = this.getEditTextValue(getView(), R.id.feed_url);
			if(TextUtils.isEmpty(name) || TextUtils.isEmpty(url))
			{
				this.displayValidationError();
				return;
			}

			this.saveFeed(name, url);
		}
	}

	private String getEditTextValue(View view, int id)
	{
		if(view != null)
		{
			EditText widget = (EditText)view.findViewById(id);
			if(widget != null)
			{
				return widget.getText().toString();
			}
		}
		return null;
	}

	protected void displayValidationError()
	{
		Toast.makeText(getContext(), "Name and Url cannot be blank", Toast.LENGTH_LONG).show();
	}

	private void saveFeed(String name, String url)
	{
		Feed feed = new Feed(name, url);
		FeedHelper helper = this.getFeedHelper();
		helper.addFeed(feed);
		Toast.makeText(getContext(), name + " has been added", Toast.LENGTH_SHORT).show();
	}

	protected FeedHelper getFeedHelper()
	{
		return new FeedHelper();
	}

	protected Context getContext()
	{
		return getActivity();
	}
}
