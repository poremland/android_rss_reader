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
