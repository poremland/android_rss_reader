package net.oremland.rss.reader.helpers;

import android.text.*;
import android.util.*;

import java.io.*;

public class ObjectSerializer
{
	public static <TObject extends Serializable> String toString(TObject object)
	{
		try
		{
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
			objectStream.writeObject(object);
			objectStream.close();
			byte[] encodedObject = Base64.encode(byteStream.toByteArray(), Base64.DEFAULT);
			return new String(encodedObject);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static <TObject extends Serializable> TObject fromString(String object)
	{
		if(TextUtils.isEmpty(object))
		{
			return null;
		}
		try
		{
			byte[] serializedObject = Base64.decode(object, Base64.DEFAULT);
			ByteArrayInputStream byteStream = new ByteArrayInputStream(serializedObject);
			ObjectInputStream objectStream = new ObjectInputStream(byteStream);
			Object deserializedObject  = objectStream.readObject();
			objectStream.close();
			return (TObject)deserializedObject;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
