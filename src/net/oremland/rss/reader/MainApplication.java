package net.oremland.rss.reader;

import android.app.Application;
import android.widget.Toast;

import net.oremland.rss.reader.helpers.*;

public class MainApplication
	extends
		Application
{
	private static MainApplication instance;

	public static MainApplication getContext() { return instance; }

	public MainApplication()
	{
		instance = this;
	}
}
