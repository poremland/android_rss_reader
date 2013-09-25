package net.oremland.rss.reader.mock;

import net.oremland.rss.reader.models.*;

public class ModelMock
	implements
		BaseModel
{
	public String getKey()
	{
		return this.getClass().getName();
	}
}
