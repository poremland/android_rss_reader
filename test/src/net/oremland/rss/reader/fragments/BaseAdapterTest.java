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
