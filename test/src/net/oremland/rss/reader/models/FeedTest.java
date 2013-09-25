package net.oremland.rss.reader.models;

import android.test.AndroidTestCase;

public class FeedTest
		extends
			AndroidTestCase
{
	public void test_Ctr_SetsNameAndUrlToNull()
	{
		Feed f = new Feed(null, null);
		assertNull(f.getName());
		assertNull(f.getUrl());
	}

	public void test_Ctr_SetsNameToValueAndUrlToNull()
	{
		String expected = "foo";
		Feed f = new Feed(expected, null);
		assertEquals(expected, f.getName());
		assertNull(f.getUrl());
	}

	public void test_Ctr_SetsNameNullAndUrlToValue()
	{
		String expected = "foo";
		Feed f = new Feed(null, expected);
		assertNull(f.getName());
		assertEquals(expected, f.getUrl());
	}

	public void test_Ctr_SetsNameValueAndUrlToValue()
	{
		String expected = "foo";
		Feed f = new Feed(expected, expected);
		assertEquals(expected, f.getName());
		assertEquals(expected, f.getUrl());
	}

	public void test_compareTo_ReturnsZero_WhenNameAndUrlAreSame_CaseSensitive()
	{
		String expected = "foo";
		Feed f = new Feed(expected, expected);
		Feed f2 = new Feed(expected, expected);
		assertEquals(0, f.compareTo(f2));
		assertEquals(0, f2.compareTo(f));
	}

	public void test_compareTo_ReturnsNegativeOne_WhenNameAndUrlAreSame_CaseInsensitive()
	{
		String lower = "foo";
		String mixed = "Foo";
		Feed f = new Feed(lower, mixed);
		Feed f2 = new Feed(mixed, lower);
		assertEquals(-1, f.compareTo(f2));
		assertEquals(-1, f2.compareTo(f));
	}

	public void test_compareTo_ReturnsOne_WhenOtherIsNull()
	{
		String expected = "foo";
		Feed f = new Feed(expected, expected);
		Feed f2 = null;
		assertEquals(1, f.compareTo(f2));
	}

	public void test_equals_ReturnsTrue_WhenNameAndUrlAreSame_CaseSensitive()
	{
		String expected = "foo";
		Feed f = new Feed(expected, expected);
		Feed f2 = new Feed(expected, expected);
		assertTrue(f.equals(f2));
		assertTrue(f2.equals(f));
	}

	public void test_equals_ReturnsFalse_WhenNameAndUrlAreSame_CaseInsensitive()
	{
		String lower = "foo";
		String mixed = "Foo";
		Feed f = new Feed(lower, mixed);
		Feed f2 = new Feed(mixed, lower);
		assertFalse(f.equals(f2));
		assertFalse(f2.equals(f));
	}

	public void test_equals_ReturnsFalse_WhenOtherIsNull()
	{
		String expected = "foo";
		Feed f = new Feed(expected, expected);
		Feed f2 = null;
		assertFalse(f.equals(f2));
	}
}
