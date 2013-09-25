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

import android.test.AndroidTestCase;
import android.view.*;
import android.widget.*;

import java.util.*;

import net.oremland.rss.reader.R;
import net.oremland.rss.reader.models.*;

public class FeedsAdapterTest
	extends
		AndroidTestCase
{
	private FeedsAdapter adapter = null;
	private List<Feed> feeds = Arrays.asList(new Feed[] { new Feed("Foo", "bar") });

	public void setUp()
	{
		adapter = this.createFeedsAdapter();
		adapter.add(feeds.get(0));
	}

	private FeedsAdapter createFeedsAdapter()
	{
		return new FeedsAdapter(mContext, R.layout.feeds_row);
	}

	public void test_getView_DoesNotThrow_WithNoTitleOrUrlViewInLayout()
	{
		adapter.getView(0, new ViewStub(mContext), null);
	}

	public void test_getView_SetsTitleViewToFeedTitleForRow()
	{
		View view = adapter.getView(0, getConvertView(), null);
		TextView title = (TextView)view.findViewById(R.id.title);
		assertEquals(feeds.get(0).getName(), title.getText());
	}

	public void test_getView_SetsUrlViewToFeedUrlForRow()
	{
		View view = adapter.getView(0, getConvertView(), null);
		assertNotNull(view);
		TextView url = (TextView)view.findViewById(R.id.url);
		assertNotNull(url);
		assertEquals(feeds.get(0).getUrl(), url.getText());
	}

	private View getConvertView()
	{
		return LayoutInflater.from(mContext).inflate(R.layout.feeds_row, null);
	}
}
