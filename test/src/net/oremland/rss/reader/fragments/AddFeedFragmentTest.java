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
import android.test.AndroidTestCase;
import android.view.*;
import android.widget.*;

import net.oremland.rss.reader.R;
import net.oremland.rss.reader.helpers.FeedHelper;
import net.oremland.rss.reader.models.Feed;

public class AddFeedFragmentTest
		extends
			AndroidTestCase
{
	private AddFeedFragment fragment;
	private View createdView = null;
	private Button button = null;
	private EditText feedName = null;
	private EditText feedUrl = null;
	private Feed addedFeed = null;
	private View.OnClickListener submitClickListener = null;
	private boolean submitWasCalled = false;
	private boolean validationErrorWasDisplayed = false;

	public void setUp()
	{
		fragment = this.createFragment(true);
		this.setGlobalsWithFragment(fragment);
	}

	private AddFeedFragment createFragment(final boolean shouldReturnView)
	{
		return new AddFeedFragment()
		{
			@Override
			public View getView()
			{
				if(shouldReturnView)
				{
					return createdView;
				}
				return null;
			}

			@Override
			protected Context getContext()
			{
				return mContext;
			}

			@Override
			protected FeedHelper getFeedHelper()
			{
				return new FeedHelper()
				{
					@Override
					public void addFeed(Feed feed)
					{
						addedFeed = feed;
					}
				};
			}

			@Override
			protected View.OnClickListener getSubmitOnClickListener()
			{
				submitClickListener = super.getSubmitOnClickListener();
				return new View.OnClickListener()
				{
					public void onClick(View view)
					{
						submitWasCalled = true;
						submitClickListener.onClick(view);
					}
				};
			}

			@Override
			protected void displayValidationError()
			{
				validationErrorWasDisplayed = true;
			}
		};
	}

	private void setGlobalsWithFragment(AddFeedFragment fragment)
	{
		createdView = fragment.onCreateView(LayoutInflater.from(mContext), null, null);
		button = (Button)createdView.findViewById(R.id.submit);
		feedName = (EditText)createdView.findViewById(R.id.feed_name);
		feedUrl = (EditText)createdView.findViewById(R.id.feed_url);
		submitWasCalled = false;
		validationErrorWasDisplayed = false;
	}

	public void test_onCreateView_ReturnsInflatedView()
	{
		assertNotNull(createdView);
	}

	public void test_onCreateView_SetsSubmitButtonOnClickListener()
	{
		assertNotNull(submitClickListener);
	}

	public void test_Submit_DoesNotSaveFeed_IfGetViewReturnsNull()
	{
		fragment = this.createFragment(false);
		this.setGlobalsWithFragment(fragment);
		button.performClick();
		assertNull(addedFeed);
	}

	public void test_Submit_SavesFeed()
	{
		feedName.setText("foo");
		feedUrl.setText("bar");
		button.performClick();
		assertTrue(submitWasCalled);
		assertNotNull(addedFeed);
	}

	public void test_Submit_DisplaysValidationError_WhenNameIsNull()
	{
		button.performClick();
		assertTrue(submitWasCalled);
		assertTrue(validationErrorWasDisplayed);
	}

	public void test_Submit_DisplaysValidationError_WhenUrlIsNull()
	{
		button.performClick();
		assertTrue(submitWasCalled);
		assertTrue(validationErrorWasDisplayed);
	}

	public void test_Submit_DisplaysValidationError_WhenNameIsEmpty()
	{
		feedName.setText("");
		feedUrl.setText("bar");
		button.performClick();
		assertTrue(submitWasCalled);
		assertTrue(validationErrorWasDisplayed);
	}

	public void test_Submit_DisplaysValidationError_WhenUrlIsEmpty()
	{
		feedName.setText("foo");
		feedUrl.setText("");
		button.performClick();
		assertTrue(submitWasCalled);
		assertTrue(validationErrorWasDisplayed);
	}
}
