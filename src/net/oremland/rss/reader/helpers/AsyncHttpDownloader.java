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
package net.oremland.rss.reader.helpers;

import android.os.AsyncTask;

import java.io.*;
import java.net.*;
import java.util.*;

public class AsyncHttpDownloader
	extends
		AsyncTask<String, Integer, byte[]>
{
	public interface OnDownloadListener
	{
		void onProgress(int progress);
		void onComplete(byte[] result);
		void onError(Exception exception);
	}

	private OnDownloadListener listener = null;
	private Exception exception = null;
	private static final int BUFFER_SIZE = 4096;
	private static final int TIMEOUT = 30000;
	private Map<String, String> parameters = new HashMap<String, String>();

	public void setOnDownloadListener(OnDownloadListener listener)
	{
		this.listener = listener;
	}

	public void addRequestParameter(String key, String value)
	{
		this.parameters.put(key, value);
	}

	@Override
	protected byte[] doInBackground(String... urls)
	{
		if(this.listener != null && urls != null)
		{
			try
			{
				return download(urls[0]);
			}
			catch(Exception e)
			{
				this.exception = e;
			}
		}
		return new byte[0];
	}

	@Override
	protected void onProgressUpdate(Integer... progress)
	{
		this.listener.onProgress(progress[0]);
	}

	@Override
	protected void onPostExecute(byte[] result)
	{
		if(exception != null)
		{
			this.listener.onError(this.exception);
			return;
		}
		this.listener.onComplete(result);
	}

	private byte[] download(String requestUrl)
		throws IOException
	{
		HttpURLConnection connection = this.createHttpConnection(requestUrl);

		try
		{
			return this.getBytesFromConnection(connection);
		}
		finally
		{
			connection.disconnect();
		}
	}

	private HttpURLConnection createHttpConnection(String requestUrl)
		throws IOException
	{
		URL url = new URL(requestUrl);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();

		this.addParametersToConnection(connection);

		return connection;
	}

	private void addParametersToConnection(HttpURLConnection connection)
	{
		connection.setConnectTimeout(TIMEOUT);
		connection.setReadTimeout(TIMEOUT);

		if (this.parameters == null)
		{
			return;
		}

		for (String key : this.parameters.keySet())
		{
			String value = this.parameters.get(key);

			connection.addRequestProperty(key, value);
		}
	}

	private byte[] getBytesFromConnection(HttpURLConnection connection)
		throws IOException
	{
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		InputStream stream = connection.getInputStream();

		byte[] buffer = new byte[BUFFER_SIZE];
		int length;

		while ((length = stream.read(buffer)) > 0)
		{
			data.write(buffer, 0, length);
		}
		stream.close();

		return data.toByteArray();
	}
}
