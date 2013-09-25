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
