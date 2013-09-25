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

import java.util.*;

import net.oremland.rss.reader.R;
import net.oremland.rss.reader.mock.*;
import net.oremland.rss.reader.models.*;

public abstract class BaseListFragmentTest
	extends
		AndroidTestCase
{
	private List<Feed> models = Arrays.asList(new Feed[] { new Feed("foo", "bar") });
	private BaseListFragment fragment = null;
	private boolean instanceRetained = false;
	private Feed displayedModel = null;

	public void setUp()
	{
		instanceRetained = false;
		displayedModel = null;
		fragment = this.createFragment();
	}

	private BaseListFragment createFragment()
	{
		return new BaseListFragment<Feed, BaseAdapter<Feed>>()
		{
			@Override
			public void setRetainInstance(boolean retain)
			{
				super.setRetainInstance(retain);
				instanceRetained = retain;
			}

			protected void loadModelList(OnModelsLoadedListener<Feed> listener)
			{
				listener.onModelsLoaded(models);
			}

			protected void displayModel(Feed model)
			{
				displayedModel = model;
			}

			protected BaseAdapter<Feed> createAdapter()
			{
				return new BaseAdapter<Feed>(mContext, R.layout.test_layout)
				{
					@Override
					protected void setupReusableView(View view, Feed model)
					{
					}
				};
			}

			protected int getLayoutId()
			{
				return R.layout.test_layout;
			}

		};
	}

	public void test_Ctor_SetsRetainInstanceTrue()
	{
		assertTrue(instanceRetained);
	}

	public void test_onCreateView_InflatesViewGivenBygetLayoutId()
	{
		View view = fragment.onCreateView(LayoutInflater.from(mContext), null, null);
		assertNotNull(view);
	}

	public void test_onActivityCreated_SetsOnItemClickListener_OnListView()
	{
		fragment.onActivityCreated(null);
		assertNotNull(fragment.getListView().getOnItemClickListener());
	}

	public void test_onActivityCreated_LoadsModelList()
	{
		fragment.onActivityCreated(null);
		assertEquals(models.size(), fragment.getListAdapter().getCount());
	}

	public void test_SetsModels_WhenModelsAreLoaded()
	{
		// TODO: Figure out the best way to test this
	}

	public void test_UpdatesListView_WhenModelsAreLoaded()
	{
		// TODO: Figure out the best way to test this
	}

	public void test_DisplaysModel_OnListViewItemClick()
	{
		fragment.getListView().setSelection(0);
		assertNotNull(displayedModel);
		assertSame(models.get(0), displayedModel);
	}

	public void test_removeModel_RemovesModelWithName()
	{
		fragment.removeModel(models.get(0).getName());
		assertEquals(0, fragment.getListAdapter().getCount());
	}

	public void test_removeModel_UpdatesListView()
	{
		fragment.removeModel(models.get(0).getName());
		assertEquals(0, fragment.getListView().getCount());
	}
}
