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

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import net.oremland.rss.reader.R;
import net.oremland.rss.reader.models.FeedItem;

public class FeedItemsAdapter
	extends
		BaseAdapter<FeedItem>
{
	public FeedItemsAdapter(Context context, int id)
	{
		super(context, id);
	}

	protected void setupReusableView(View view, FeedItem model)
	{
		if(view != null && model != null)
		{
			this.setupViewText(R.id.description, view, model.getDescription());
			this.setupViewText(R.id.title, view, model.getTitle());
			this.setupViewText(R.id.url, view, model.getUrl());
			this.setupViewText(R.id.date, view, model.getDateAsString());
		}
	}

	private void setupViewText(int id, View container, String html)
	{
		if(container != null)
		{
			TextView view = (TextView)container.findViewById(id);
			if(view != null)
			{
				setTextFromHtml(view, html);
			}
		}
	}

	private void setTextFromHtml(TextView view, String html)
	{
		String decodedHtml = Html.fromHtml(Html.fromHtml(html).toString()).toString();
		String text = decodedHtml.replaceAll("<[^\\s>][^>]*>", "");
		view.setText(text);
	}
}
