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

import android.content.*;
import android.graphics.*;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.util.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;

import net.oremland.rss.reader.R;
import net.oremland.rss.reader.helpers.*;
import net.oremland.rss.reader.models.*;

public class BrowserFragment
	extends
		Fragment
{
	private final static String FEED_ITEM_KEY = "feedItem";
	private FeedItem feedItem;
	private Intent shareIntent;

	private static final FrameLayout.LayoutParams ZOOM_PARAMS =
		new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT,
			Gravity.BOTTOM);

	public BrowserFragment()
	{
		setRetainInstance(true);
		setHasOptionsMenu(true);
	}

	public void setFeedItem(FeedItem item)
	{
		this.feedItem = item;
	}

	public FeedItem getFeedItem()
	{
		return this.feedItem;
	}

	protected Context getContext()
	{
		return getActivity();
	}
	 
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.browser, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		this.feedItem = this.getSavedFeedItem(savedInstanceState);
		this.initializeViews(savedInstanceState);
		this.loadFeed();
	}

	private FeedItem getSavedFeedItem(Bundle savedInstanceState)
	{
		if(savedInstanceState != null)
		{
			String s = savedInstanceState.getString(FEED_ITEM_KEY, "");
			if(!TextUtils.isEmpty(s))
			{
				return ObjectSerializer.fromString(s);
			}
		}
		return this.feedItem;
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		this.saveFeedItem(outState);
	}

	private void saveFeedItem(Bundle outState)
	{
		if(this.feedItem != null && outState != null)
		{
			String serializedItem  = ObjectSerializer.toString(this.feedItem);
			if(serializedItem != null)
			{
				outState.putString(FEED_ITEM_KEY, serializedItem );
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.browser_menu, menu);
		setupShareActionProvider(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return this.handledViewOriginal(item)
			|| this.handledCopyText(item)
			|| super.onOptionsItemSelected(item);
	}

	private void setupShareActionProvider(Menu menu)
	{
		MenuItem item = menu.findItem(R.id.menu_share);
 
		if (item != null)
		{
			ShareActionProvider provider = (ShareActionProvider)MenuItemCompat.getActionProvider(item);

			if (provider != null)
			{
				provider.setShareIntent(this.getShareIntent());
			}
		}
	}

	private boolean handledViewOriginal(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_viewOriginal)
		{
			this.viewOriginialLink();
			return true;
		}
		return false;
	}

	private boolean handledCopyText(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_copyUrl)
		{
			this.copyUrlToClipboard();
			return true;
		}
		return false;
	}

	private void initializeViews(Bundle savedInstanceState)
	{
		if(savedInstanceState == null && getContext() != null)
		{
			boolean isTablet = getContext().getResources().getBoolean(R.bool.isTablet);
			WebSettings.ZoomDensity zoomDensity = isTablet ? WebSettings.ZoomDensity.MEDIUM : WebSettings.ZoomDensity.FAR;

			WebView description = (WebView)getView().findViewById(R.id.description);
			description.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
			description.getSettings().setJavaScriptEnabled(true);
			description.getSettings().setPluginState(WebSettings.PluginState.ON);
			description.getSettings().setDefaultTextEncodingName("utf-8");
			description.getSettings().setLoadWithOverviewMode(true);
			description.getSettings().setDefaultZoom(zoomDensity);
			description.getSettings().setSupportZoom(true);
			description.getSettings().setBuiltInZoomControls(true);
			description.requestFocus(View.FOCUS_DOWN);
			description.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
			description.getSettings().setUseWideViewPort(isTablet);
			description.setWebChromeClient(this.getWebChromeClient());
			description.setWebViewClient(this.getWebViewClient());
		}
	}

	public void loadFeed()
	{
		if(getFeedItem() != null)
		{
			this.setTitleText();
			this.loadUrl();
			this.updateShareIntent();
		}
	}

	private void setTitleText()
	{
		TextView titleView = (TextView)getView().findViewById(R.id.title);
		titleView.setText(getFeedItem().getTitle());
	}

	private void loadUrl()
	{
		String base64Data = Base64.encodeToString(getFeedItem().getContent().getBytes(), Base64.DEFAULT);
		String dataUri = String.format("data:text/html;charset=utf-8;base64,%s", base64Data);
		WebView description = (WebView)getView().findViewById(R.id.description);
		description.loadUrl(dataUri);
	}

	private WebChromeClient getWebChromeClient()
	{
		return new WebChromeClient()
		{
			public void onProgressChanged(WebView view, int progress)
			{
				if(getActivity() != null)
				{
					getActivity().setProgress(progress * 100);
				}
			}
		};
	}

	protected WebViewClient getWebViewClient()
	{
		return new WebViewClient()
		{
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				updateProgressBarVisibility(true);
			}

			@Override
			public void onPageFinished(WebView view, String url)
			{
				updateProgressBarVisibility(false);
			}
		};
	}

	private void updateProgressBarVisibility(boolean visible)
	{
		if(this.getActivity() != null)
		{
			this.getActivity().setProgressBarVisibility(visible);
		}
	}

	public Intent getShareIntent()
	{
		if(shareIntent == null)
		{
			shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			this.updateShareIntent();
		}
		return shareIntent;
	}

	private void updateShareIntent()
	{
		if(getFeedItem() != null && shareIntent != null)
		{
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, getFeedItem().getTitle());
			shareIntent.putExtra(Intent.EXTRA_TEXT, getFeedItem().getUrl());
		}
	}

	public void viewOriginialLink()
	{
		try
		{
			Uri uri = Uri.parse(getFeedItem().getUrl());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			this.startActivity(intent); 
		}
		catch(Exception e)
		{
			Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	private void copyUrlToClipboard()
	{
		ClipboardManager clipboard = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE); 
		ClipData clip = ClipData.newPlainText("Feed Item Url", getFeedItem().getUrl());
		clipboard.setPrimaryClip(clip);
		Toast.makeText(getContext(), "Url Copied To Clipboard", Toast.LENGTH_SHORT).show();
	}
}
