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

import net.oremland.rss.reader.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.*;
import android.text.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.*;

import java.util.*;
import java.util.Map.Entry;

import net.oremland.rss.reader.helpers.*;
import net.oremland.rss.reader.models.*;

import org.json.JSONArray;

public abstract class BaseListFragment<TModel extends BaseModel, TAdapter extends BaseAdapter>
	extends
		ListFragment
{
	private final static String MODELS_KEY = "models";
	private ListView list;
	private TAdapter adapter;
	private TreeMap<String, TModel> models;

	protected interface OnModelsLoadedListener<TModel extends BaseModel>
	{
		public void onModelsLoaded(List<TModel> models);
	}

	public BaseListFragment()
	{
		// we use state management and need the onSavedInstanceState
		// and onCreate, onCreateView, and onActivityCreated methods
		// to be called for the lifecycle events of the fragment.
		setRetainInstance(false);
	}

	protected abstract void loadModelList(OnModelsLoadedListener<TModel> listener);
	protected abstract void displayModel(TModel model);
	protected abstract TAdapter createAdapter();
	protected abstract int getLayoutId();
	protected Comparator getListComparator()
	{
		return Collections.reverseOrder(Collections.reverseOrder());
	}

	protected void shouldUpdateModelList()
	{
		if(this.clearModels() || this.clearAdapter())
		{
			this.updateListView();
		}
	}

	private boolean clearAdapter()
	{
		if(this.adapter != null)
		{
			this.adapter.clear();
			return true;
		}
		return false;
	}

	private boolean clearModels()
	{
		if(this.models != null)
		{
			this.models.clear();
			return true;
		}
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(this.getLayoutId(), container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		this.initializeList();
		this.setFieldsFromBundle(this.getArguments());
		this.models = this.getSavedModels(savedInstanceState);
		this.ensureModelsTreeMapIsInitialized();
	}

	@Override
	public void onResume()
	{
		super.onResume();

		this.load();
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		this.saveModels(outState);
	}

	public void load()
	{
		if(!this.didLoadExistingModels() || !this.canCacheModels())
		{
			this.setProgressBarVisibility(View.VISIBLE);
			this.loadModelList(new OnModelsLoadedListener<TModel>()
			{
				@Override
				public void onModelsLoaded(List<TModel> models)
				{
					TreeMap<String, TModel> map = getModelMap(models);
					setModels(map);
					updateListView();
				}
			});
		}
	}

	protected boolean canCacheModels()
	{
		return true;
	}

	private boolean didLoadExistingModels()
	{
		if(this.hasModels())
		{
			updateListView();
			return true;
		}
		return false;
	}

	private boolean hasModels()
	{
		return this.models != null && this.models.size() > 0;
	}

	private void initializeList()
	{
		this.list = getListView();
		this.list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		this.list.setOnItemClickListener(this.getListViewClickListener());
	}

	private OnItemClickListener getListViewClickListener()
	{
		return new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				list.setItemChecked(position, true);
				list.setSelection(position);
				TModel item = (TModel)parent.getAdapter().getItem(position);
				displayModel(item);
			}
		};
	}

	protected void setFieldsFromBundle(Bundle arguments)
	{
	}

	private void ensureModelsTreeMapIsInitialized()
	{
		if(this.models == null)
		{
			this.models = this.createTreeMap();
		}
	}

	private TreeMap<String, TModel> createTreeMap()
	{
		return new TreeMap<String, TModel>(this.getListComparator());
	}

	protected void removeModel(String name)
	{
		this.ensureModelsTreeMapIsInitialized();
		this.models.remove(name);
		this.updateListView();
	}

	private TModel getModel(String name)
	{
		this.ensureModelsTreeMapIsInitialized();
		return this.models.get(name);
	}

	private TModel getModel(int position)
	{
		return (TModel)this.adapter.getItem(position);
	}

	private void setModels(TreeMap<String, TModel> models)
	{
		this.models = models;
	}

	private void updateListView()
	{
		this.ensureModelsTreeMapIsInitialized();
		this.updateListView(this.models);
		this.setProgressBarVisibility(View.GONE);
	}

	private void updateListView(TreeMap<String, TModel> items)
	{
		if(getActivity() != null)
		{
			this.adapter = this.createAdapter();
			List<TModel> models = this.getModelList(items);
			this.adapter.setModels(models);
			if(this.list != null && !this.isDetached())
			{
				this.list.setAdapter(this.adapter);
			}
		}
	}

	private List<TModel> getModelList(TreeMap<String, TModel> items)
	{
		List<TModel> models = new ArrayList<TModel>();
		for(Map.Entry<String, TModel> item : items.entrySet())
		{
			models.add(item.getValue());
		}
		return models;
	}

	private TreeMap<String, TModel> getModelMap(List<TModel> models)
	{
		TreeMap<String, TModel> map = this.createTreeMap();
		for(TModel model : models)
		{
			map.put(model.getKey(), model);
		}
		return map;
	}

	private void setProgressBarVisibility(int visible)
	{
		if(this.hasProgressBar())
		{
			getView().findViewById(R.id.progressContainer).setVisibility(visible);
		}
	}

	private boolean hasProgressBar()
	{
		return getView() != null
			&& getView().findViewById(R.id.progressContainer) != null;
	}

	private void saveModels(Bundle outState)
	{
		if(this.models!= null && outState != null)
		{
			String serializedModels  = ObjectSerializer.toString(this.models);
			if(serializedModels != null)
			{
				outState.putString(MODELS_KEY, serializedModels );
			}
		}
	}

	private TreeMap<String, TModel> getSavedModels(Bundle savedInstanceState)
	{
		if(savedInstanceState != null)
		{
			String s = savedInstanceState.getString(MODELS_KEY, "");
			if(!TextUtils.isEmpty(s))
			{
				return ObjectSerializer.fromString(s);
			}
		}
		return null;
	}
}
