package net.oremland.rss.reader;

import android.test.ApplicationTestCase;

import net.oremland.rss.reader.helpers.*;

public class MainApplicationTest
	extends
		ApplicationTestCase<MainApplication>
{
	private MainApplication application;

	public MainApplicationTest()
	{
		super(MainApplication.class);
	}

	public void setUp()
	{
		application = this.getApplication();
	}

	public void test_getContext_ReturnsStaticInstanceOfMainApplication()
	{
		assertTrue(application.getContext() instanceof MainApplication);
	}
}
