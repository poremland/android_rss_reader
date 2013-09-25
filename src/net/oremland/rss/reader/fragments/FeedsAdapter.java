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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import net.oremland.rss.reader.R;
import net.oremland.rss.reader.models.Feed;

public class FeedsAdapter
	extends
		BaseAdapter<Feed>
{
	public FeedsAdapter(Context context, int id)
	{
		super(context, id);
	}

	protected void setupReusableView(View view, Feed model)
	{
		if(view != null && model != null)
		{
			this.setupTitleView(view, model);
			this.setupUrlView(view, model);
		}
	}

	private void setupTitleView(View container, Feed model)
	{
		if(container != null)
		{
			TextView title = (TextView)container.findViewById(R.id.title);
			if(title != null)
			{
				title.setText(model.getName());
			}
		}
	}

	private void setupUrlView(View container, Feed model)
	{
		if(container != null)
		{
			TextView url = (TextView)container.findViewById(R.id.url);
			if(url != null)
			{
				url.setText(model.getUrl());
			}
		}
	}
}
