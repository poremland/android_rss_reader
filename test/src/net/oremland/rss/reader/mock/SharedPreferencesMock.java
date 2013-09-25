package net.oremland.rss.reader.mock;

import java.util.*;

import android.content.SharedPreferences;

public class SharedPreferencesMock
	implements
		SharedPreferences
{
	public HashMap<String, Object> map = new HashMap<String, Object>();
	public OnSharedPreferenceChangeListener preferenceChangeListener;

	@Override
	public boolean contains(String key)
	{
		return map.containsKey(key);
	}
	
	@Override
	public Map<String, ?> getAll()
	{
		return null;
	}
	
	@Override
	public Set<String> getStringSet(String key, Set<String> defValues)
	{
		return null;
	}
	
	@Override
	public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener)
	{
		preferenceChangeListener = listener;
	}

	@Override
	public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener)
	{
		preferenceChangeListener = null;
	}					

	@Override
	public boolean getBoolean(String key, boolean defValue)
	{
		Object storedValue = map.get(key);
		if (storedValue != null && storedValue instanceof Boolean)
		{
			return ((Boolean)storedValue).booleanValue();
		}
		return defValue;
	}
	@Override
	public float getFloat(String key, float defValue)
	{
		Object storedValue = map.get(key);
		if (storedValue != null && storedValue instanceof Float)
		{
			return (Float)storedValue;
		}
		return defValue;
	}

	@Override
	public long getLong(String key, long defValue)
	{
		Object storedValue = map.get(key);
		if (storedValue != null && storedValue instanceof Long)
		{
			return (Long)storedValue;
		}
		return defValue;
	}

	@Override
	public int getInt(String key, int defValue)
	{
		Object storedValue = map.get(key);
		if (storedValue != null && storedValue instanceof Integer)
		{
			return (Integer)storedValue;
		}
		return defValue;
	}

	@Override
	public String getString(String key, String defValue)
	{
		Object storedValue = map.get(key);
		if (storedValue != null && storedValue instanceof String)
		{
			return (String)storedValue;
		}
		return defValue;
	}

	@Override
	public Editor edit()
	{
		return new Editor()
		{
			@Override
			public void apply()
			{
			}
			
			@Override
			public Editor clear()
			{
				map.clear();
				return this;
			}
			
			@Override
			public boolean commit()
			{
				return true;
			}
			
			@Override
			public Editor remove(String key)
			{
				map.remove(key);
				return this;
			}
			
			@Override
			public Editor putStringSet(String key, Set<String> values)
			{
				map.put(key, values);
				return this;
			}
			
			@Override
			public Editor putString(String key, String value)
			{
				map.put(key, value);
				return this;
			}
			
			@Override
			public Editor putLong(String key, long value)
			{
				map.put(key, value);
				return this;
			}
			
			@Override
			public Editor putInt(String key, int value)
			{
				map.put(key, value);
				return this;
			}
			
			@Override
			public Editor putFloat(String key, float value)
			{
				map.put(key, value);
				return this;
			}
			
			@Override
			public Editor putBoolean(String key, boolean value)
			{
				map.put(key, value);
				return this;
			}
		};
	}
}
