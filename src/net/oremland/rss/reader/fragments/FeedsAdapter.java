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
