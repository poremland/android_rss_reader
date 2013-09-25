package net.oremland.rss.reader.fragments;

import android.test.AndroidTestCase;
import android.view.*;
import android.widget.*;

import java.util.*;

import net.oremland.rss.reader.R;
import net.oremland.rss.reader.models.*;

public class FeedItemsAdapterTest
	extends
		AndroidTestCase
{
	private FeedItemsAdapter adapter = null;
	private List<FeedItem> items = Arrays.asList(new FeedItem[] { new FeedItem("Boo", "Foo", "bar", "Baz", new Date()) });

	public void setUp()
	{
		adapter = this.createFeedItemsAdapter();
		adapter.add(items.get(0));
	}

	private FeedItemsAdapter createFeedItemsAdapter()
	{
		return new FeedItemsAdapter(mContext, R.layout.feed_items_row);
	}

	public void test_getView_DoesNotThrow_WithNoDescriptionTitleOrUrlViewInLayout()
	{
		adapter.getView(0, new ViewStub(mContext), null);
	}

	public void test_getView_SetsDescriptionViewToFeedItemDescriptionForRow()
	{
		View view = adapter.getView(0, getConvertView(), null);
		TextView description = (TextView)view.findViewById(R.id.description);
		assertEquals(items.get(0).getDescription(), description.getText());
	}

	public void test_getView_SetsTitleViewToFeedItemTitleForRow()
	{
		View view = adapter.getView(0, getConvertView(), null);
		TextView title = (TextView)view.findViewById(R.id.title);
		assertEquals(items.get(0).getTitle(), title.getText());
	}

	public void test_getView_SetsUrlViewToFeedItemUrlForRow()
	{
		View view = adapter.getView(0, getConvertView(), null);
		assertNotNull(view);
		TextView url = (TextView)view.findViewById(R.id.url);
		assertNotNull(url);
		assertEquals(items.get(0).getUrl(), url.getText());
	}

	private View getConvertView()
	{
		return LayoutInflater.from(mContext).inflate(R.layout.feed_items_row, null);
	}
}
