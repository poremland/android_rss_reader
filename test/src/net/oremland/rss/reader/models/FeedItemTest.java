package net.oremland.rss.reader.models;

import android.test.AndroidTestCase;

import java.text.*;
import java.util.*;

public class FeedItemTest
		extends
			AndroidTestCase
{
	private final static String CONTENT = "CONTENT";
	private final static String DESCRIPTION = "DESCRIPTION";
	private final static String TITLE = "TITLE";
	private final static String URL = "URL";
	private final static Date DATE = new Date();
	private FeedItem item = null;

	public void setUp()
	{
		item = new FeedItem(CONTENT, DESCRIPTION, TITLE, URL, DATE);
	}

	public void test_Ctr_SetsDescriptionTitleAndUrlToNull()
	{
		item = new FeedItem(null, null, null, null, null);
		assertNull(item.getContent());
		assertNull(item.getDescription());
		assertNull(item.getTitle());
		assertNull(item.getUrl());
	}

	public void test_Ctr_SetsContentToValue()
	{
		assertEquals(CONTENT, item.getContent());
	}

	public void test_Ctr_SetsDescriptionToValue()
	{
		assertEquals(DESCRIPTION, item.getDescription());
	}

	public void test_Ctr_SetsTitleToValue()
	{
		assertEquals(TITLE, item.getTitle());
	}

	public void test_Ctr_SetsUrlToValue()
	{
		assertEquals(URL, item.getUrl());
	}

	public void test_getKey_ReturnsFormattedDate()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
		String key = formatter.format(DATE);
		assertEquals(key, item.getKey());
	}

	public void test_compareTo_ReturnsZero_WhenValuesAreSame_CaseSensitive()
	{
		FeedItem item2 = new FeedItem(item.getContent(), item.getDescription(), item.getTitle(), item.getUrl(), item.getDate());
		assertEquals(0, item.compareTo(item2));
		assertEquals(0, item2.compareTo(item));
	}

	public void test_compareTo_ReturnsDateComparison_WhenValuesAreSameWithDifferentDates_CaseInsensitive()
	{
		FeedItem item2 = new FeedItem(item.getContent(), item.getDescription().toLowerCase(), item.getTitle().toUpperCase(), item.getUrl(), new Date());
		assertEquals(item.getDate().compareTo(item2.getDate()), item.compareTo(item2));
		assertEquals(item2.getDate().compareTo(item.getDate()), item2.compareTo(item));
	}

	public void test_compareTo_ReturnsTitleComparison_WhenValuesAreSameWithSameDates_CaseInsensitive()
	{
		FeedItem item2 = new FeedItem(item.getContent(), item.getDescription().toLowerCase(), item.getTitle().toUpperCase(), item.getUrl(), item.getDate());
		assertEquals(item.getTitle().compareTo(item2.getTitle()), item.compareTo(item2));
		assertEquals(item2.getTitle().compareTo(item.getTitle()), item2.compareTo(item));
	}

	public void test_compareTo_ReturnsOne_WhenOtherIsNull()
	{
		FeedItem item2 = null;
		assertEquals(1, item.compareTo(item2));
	}

	public void test_equals_ReturnsTrue_WhenNameAndUrlAreSame_CaseSensitive()
	{
		FeedItem item2 = new FeedItem(item.getContent(), item.getDescription(), item.getTitle(), item.getUrl(), item.getDate());
		assertTrue(item.equals(item2));
		assertTrue(item2.equals(item));
	}

	public void test_equals_ReturnsFalse_WhenNameAndUrlAreSame_CaseInsensitive()
	{
		FeedItem item2 = new FeedItem(item.getContent(), item.getDescription().toLowerCase(), item.getTitle().toUpperCase(), item.getUrl(), item.getDate());
		assertFalse(item.equals(item2));
		assertFalse(item2.equals(item));
	}

	public void test_equals_ReturnsFalse_WhenOtherIsNull()
	{
		FeedItem item2 = null;
		assertFalse(item.equals(item2));
	}
}
