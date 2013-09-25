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

import android.content.Context;
import android.view.*;
import android.widget.*;

import java.util.List;

import net.oremland.rss.reader.R;

public abstract class BaseAdapter<TModel>
	extends
		ArrayAdapter<TModel>
{
	private int LAYOUT_ID;

	public BaseAdapter(Context context, int id)
	{
		super(context, id);
		LAYOUT_ID = id;
	}

	public void setModels(List<TModel> models)
	{
		this.clear();
		for(TModel model : models)
		{
			this.add(model);
		}
	}

	@Override
	public View getView(int position, View reusableView, ViewGroup parent)
	{
		if(getCount() == 0 || position >= getCount())
		{
			return null;
		}

		TModel model = getItem(position);

		if (model == null)
		{
			return null;
		}

		return this.createView(reusableView, model);
	}

	private View createView(View reusableView, TModel model)
	{
		View view = reusableView;
		if(view == null)
		{
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(LAYOUT_ID, null);
		}

		this.setupReusableView(view, model);

		return view;
	}

	protected abstract void setupReusableView(View view, TModel model);
}
