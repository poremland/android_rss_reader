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
package net.oremland.rss.reader;

import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.text.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import net.oremland.rss.reader.fragments.*;
import net.oremland.rss.reader.helpers.*;
import net.oremland.rss.reader.models.*;

public class MainActivity
	extends
		ActionBarActivity
	implements
		FeedsListFragment.OnFeedSelectedListener,
		FeedItemsListFragment.OnFeedItemSelectedListener
{
	private int optionsMenuId = this.getDefaultOptionsMenuId();

	protected interface FragmentLifecycleHelper<T extends Fragment>
	{
		T create(int id);
		void update(T fragment);
		void addToFragmentTransaction(FragmentTransaction transaction);
		boolean shouldIncludeMainMenuItems();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if(savedInstanceState == null)
		{
			this.showDefaultFragment();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater minflater = getMenuInflater();
		minflater.inflate(this.optionsMenuId, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return this.handledHome(item)
			|| this.handledAdd(item)
			|| super.onOptionsItemSelected(item);
	}

	private boolean handledHome(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			this.popBackStack();
			return true;
		}
		return false;
	}

	private boolean handledAdd(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_add)
		{
			this.displayAddFeedFragment();
			return true;
		}
		return false;
	}

	@Override
	public void onResume()
	{
		super.onResume();

		this.updateHomeButton();
	}

	@Override
	public void onBackPressed()
	{
		this.popBackStack();

		this.updateHomeButton();
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		outState.putInt("optionsMenuId", this.optionsMenuId);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);

		this.optionsMenuId = savedInstanceState.getInt("optionsMenuId", this.getDefaultOptionsMenuId());
		this.restoreFragmentInstanceState();
	}

	private void restoreFragmentInstanceState()
	{
		Fragment addFeedFragment = this.getFragment(R.id.add_feed_fragment);
		Fragment browserFragment = this.getFragment(R.id.browser_fragment);
		int id = addFeedFragment == null ?
				browserFragment == null ? -1 : R.id.browser_fragment
				: R.id.add_feed_fragment;
		FragmentManager manager = getSupportFragmentManager();
		if(id != -1 && manager != null)
		{
			FragmentTransaction transaction = manager.beginTransaction();
			this.hideFragmentInLayout(R.id.feeds_list_fragment, transaction);
			this.hideFragmentInLayout(R.id.feed_items_list_fragment, transaction);
			transaction.commit();
		}
	}

	@Override
	public void onFeedSelected(Feed feed)
	{
		this.displayFeedItemsListFragment(feed);
	}

	@Override
	public void onFeedItemSelected(FeedItem item)
	{
		this.displayBrowserFragment(item);
	}

	private void showDefaultFragment()
	{
		this.displayFeedsListFragment();
		if(this.shouldDisplayAddFeedFragment())
		{
			this.displayAddFeedFragment();
		}
	}

	private boolean shouldDisplayAddFeedFragment()
	{
		FeedHelper helper = new FeedHelper();
		return helper.getFeeds().size() == 0;
	}

	private void displayAddFeedFragment()
	{
		this.displayFragment(R.id.add_feed_fragment, new FragmentLifecycleHelper<AddFeedFragment>()
		{
			public void addToFragmentTransaction(FragmentTransaction transaction)
			{
				hideFragmentInLayout(R.id.feeds_list_fragment, transaction);
				hideFragmentInLayout(R.id.feed_items_list_fragment, transaction);
			}

			public AddFeedFragment create(int id)
			{
				return new AddFeedFragment();
			}

			public boolean shouldIncludeMainMenuItems()
			{
				return false;
			}

			public void update(AddFeedFragment fragment)
			{
			}
		});
	}

	private void displayFeedsListFragment()
	{
		this.displayFragment(R.id.feeds_list_fragment, new FragmentLifecycleHelper<FeedsListFragment>()
		{
			public void addToFragmentTransaction(FragmentTransaction transaction)
			{
			}

			public FeedsListFragment create(int id)
			{
				FeedsListFragment fragment = new FeedsListFragment();
				return fragment;
			}

			public boolean shouldIncludeMainMenuItems()
			{
				return true;
			}

			public void update(FeedsListFragment fragment)
			{
			}
		});
	}

	private void displayFeedItemsListFragment(final Feed feed)
	{
		this.displayFragment(R.id.feed_items_list_fragment, new FragmentLifecycleHelper<FeedItemsListFragment>()
		{
			public void addToFragmentTransaction(FragmentTransaction transaction)
			{
			}

			public FeedItemsListFragment create(int id)
			{
				FeedItemsListFragment fragment = new FeedItemsListFragment();
				fragment.setFeed(feed);
				return fragment;
			}

			public boolean shouldIncludeMainMenuItems()
			{
				return true;
			}

			public void update(FeedItemsListFragment fragment)
			{
				fragment.setFeed(feed);
				fragment.load();
				LinearLayout.LayoutParams parameters = (LinearLayout.LayoutParams)fragment.getView().getLayoutParams();
				parameters.weight = 2f;
				fragment.getView().setLayoutParams(parameters);
			}
		});
	}

	private void displayBrowserFragment(final FeedItem item)
	{
		this.displayFragment(R.id.browser_fragment, new FragmentLifecycleHelper<BrowserFragment>()
		{
			public void addToFragmentTransaction(FragmentTransaction transaction)
			{
				hideFragmentInLayout(R.id.feeds_list_fragment, transaction);
				hideFragmentInLayout(R.id.feed_items_list_fragment, transaction);
			}

			public BrowserFragment create(int id)
			{
				BrowserFragment fragment = new BrowserFragment();
				fragment.setFeedItem(item);
				return fragment;
			}

			public boolean shouldIncludeMainMenuItems()
			{
				return false;
			}

			public void update(BrowserFragment fragment)
			{
				fragment.setFeedItem(item);
				fragment.loadFeed();
			}
		});
	}

	private void hideFragmentInLayout(int id, FragmentTransaction transaction)
	{
		Fragment fragment = this.getFragment(id);
		if(fragment != null && transaction != null && fragment.isInLayout())
		{
			transaction.hide(fragment);
		}
	}

	protected FeedsListFragment.OnFeedSelectedListener getOnFeedSelectedListener()
	{
		return this;
	}

	protected FeedItemsListFragment.OnFeedItemSelectedListener getOnFeedItemSelectedListener()
	{
		return this;
	}

	private void displayFragment(int id, FragmentLifecycleHelper helper)
	{
		Fragment fragment = this.createOrUpdateFragment(id, helper);
		this.addFragmentToBackStack(fragment, id, helper);
		this.updateHomeButton(id);
		this.optionsMenuId = helper.shouldIncludeMainMenuItems() ? getDefaultOptionsMenuId() : R.menu.empty_menu;
		this.invalidateOptionsMenu();
	}

	private Fragment createOrUpdateFragment(int id, FragmentLifecycleHelper helper)
	{
		Fragment fragment = this.getFragment(id);
		if(fragment == null)
		{
			return helper.create(id);
		}
		helper.update(fragment);
		return fragment;
	}

	private Fragment getFragment(int id)
	{
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentByTag(this.getFragmentIdAsString(id));
		if(fragment == null)
		{
			fragment = manager.findFragmentById(id);
		}
		return fragment;
	}

	protected void addFragmentToBackStack(Fragment fragment, int id, FragmentLifecycleHelper helper)
	{
		FragmentManager manager = getSupportFragmentManager();
		if(fragment != null && manager != null && !fragment.isInLayout())
		{
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
			// alternatively you can have more control over the fragment
			// lifecycle by choosing to hide/show fragments by calling
			// transaction.add instead of replace. Calling replace forces
			// each fragment through it's lifecycle.
			helper.addToFragmentTransaction(transaction);
			transaction.replace(R.id.fragment_container, fragment, this.getFragmentIdAsString(id));
			transaction.addToBackStack(this.getFragmentIdAsString(id));
			transaction.commit();
		}
	}

	private String getFragmentIdAsString(int id)
	{
		return Integer.toString(id);
	}

	private void popBackStack()
	{
		FragmentManager manager = getSupportFragmentManager();
		int backStackEntryCount = manager.getBackStackEntryCount();
		boolean isTablet = this.getResources().getBoolean(R.bool.isTablet);
		int initialBackStackCount = isTablet ? 0 : 1;
		if(backStackEntryCount == initialBackStackCount)
		{
			this.finish();
		}
		if(backStackEntryCount > 0)
		{
			this.popBackStack(manager);
		}
		int id = backStackEntryCount > 1 ?
			Integer.parseInt(manager.getBackStackEntryAt(backStackEntryCount - 2).getName()) :
			R.id.feeds_list_fragment;
		this.updateHomeButton(id);
		this.optionsMenuId = this.getDefaultOptionsMenuId();
		this.invalidateOptionsMenu();
	}

	private int getDefaultOptionsMenuId()
	{
		return R.menu.main_menu;
	}

	private void popBackStack(FragmentManager manager)
	{
		try
		{
			manager.popBackStackImmediate();
		}
		catch(Exception e)
		{
			// TODO: set flag to pop on resume?
			e.printStackTrace();
		}
	}

	private void updateHomeButton()
	{
		FragmentManager manager = getSupportFragmentManager();
		if(manager != null)
		{
			int id = this.fragmentIdFromBackStackEntry(manager);
			this.updateHomeButton(id);
		}
	}

	private int fragmentIdFromBackStackEntry(FragmentManager manager)
	{
		int backStackEntryCount = manager.getBackStackEntryCount();
		if(backStackEntryCount > 0)
		{
			String name = manager.getBackStackEntryAt(backStackEntryCount - 1).getName();
			if(!TextUtils.isEmpty(name))
			{
				return Integer.parseInt(name);
			}
		}
		return R.id.feeds_list_fragment;
	}

	private void updateHomeButton(int id)
	{
		FragmentManager manager = getSupportFragmentManager();
		if(manager != null)
		{
			int backStackEntryCount = manager.getBackStackEntryCount();
			String name = this.getFragmentIdAsString(id);
			boolean isTablet = this.getResources().getBoolean(R.bool.isTablet);
			boolean isFeedsListCurrent = name.equals(this.getFragmentIdAsString(R.id.feeds_list_fragment));
			boolean isNotTabletAndNotOnFirstFragment = !isTablet && !isFeedsListCurrent;
			boolean isInLayout = this.getFragment(id) != null && this.getFragment(id).isInLayout();
			boolean isTabletAndOnBrowser = isTablet && !isInLayout;

			this.updateHomeButton(isNotTabletAndNotOnFirstFragment || isTabletAndOnBrowser);
		}
	}

	private void updateHomeButton(boolean enableHomeButton)
	{
		getSupportActionBar().setDisplayHomeAsUpEnabled(enableHomeButton);
		getSupportActionBar().setHomeButtonEnabled(enableHomeButton);
	}
}
