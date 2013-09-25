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
import android.view.View;
import android.view.ViewStub;

import java.util.*;

import net.oremland.rss.reader.R;

public class BaseAdapterTest
	extends
		AndroidTestCase
{
	private BaseAdapter<String> adapter = null;
	private List<String> initialList = Arrays.asList(new String[] { "one", "two", "three" });
	private List<String> newList = Arrays.asList(new String[] { "four", });
	private String modelUsedForSetup = null;

	public void setUp()
	{
		adapter = this.createBaseAdapter();
		modelUsedForSetup = null;
		for(String s : initialList)
		{
			adapter.add(s);
		}
	}

	private BaseAdapter<String> createBaseAdapter()
	{
		return new BaseAdapter<String>(mContext, R.layout.test_layout)
		{
			protected void setupReusableView(View view, String model)
			{
				modelUsedForSetup = model;
			}
		};
	}

	public void test_ConstructsWithEmptyList()
	{
		adapter = this.createBaseAdapter();
		assertEquals(0, adapter.getCount());
	}

	public void test_setModels_ReplacesCurrentModelsWithNewList()
	{
		adapter.setModels(newList);
		this.adapterOnlyContainsList(adapter, newList);
	}

	private boolean adapterOnlyContainsList(BaseAdapter adapter, List<String> list)
	{
		if((adapter != null && list == null) || list.size() != adapter.getCount())
		{
			return false;
		}

		for(int index=0; index<adapter.getCount(); index++)
		{
			if(!adapter.getItem(index).equals(list.get(index)))
			{
				return false;
			}
		}
		return true;
	}

	public void test_getView_ReturnsNull_WhenAdapterHasNoModels()
	{
		adapter = this.createBaseAdapter();
		assertNull(adapter.getView(0, this.createViewStub(), null));
	}

	public void test_getView_ReturnsNull_WhenPositionIsGreaterThanNumberOfModels()
	{
		assertNull(adapter.getView(initialList.size(), this.createViewStub(), null));
	}

	public void test_getView_ReturnsView()
	{
		assertNotNull(adapter.getView(0, this.createViewStub(), null));
	}

	public void test_getView_SetsUpReusableViewWithSpecifiedItem()
	{
		adapter.getView(1, this.createViewStub(), null);
		assertEquals(initialList.get(1), modelUsedForSetup);
	}

	private View createViewStub()
	{
		return new ViewStub(mContext);
	}
}
