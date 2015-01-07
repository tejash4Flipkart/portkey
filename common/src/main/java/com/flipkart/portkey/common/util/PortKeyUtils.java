/**
 * 
 */
package com.flipkart.portkey.common.util;

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.flipkart.portkey.common.entity.Entity;
import com.flipkart.portkey.common.exception.InvalidAnnotationException;

/**
 * @author santosh.p
 */
public class PortKeyUtils
{
	private static final Logger logger = Logger.getLogger(PortKeyUtils.class);

	public static <T extends Entity> Field getFieldFromBean(T bean, String fieldName) throws InvalidAnnotationException
	{
		Field field = null;
		try
		{

			field = bean.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
		}
		catch (NoSuchFieldException e)
		{
			throw new InvalidAnnotationException("exception while trying to get field from field name, bean=" + bean
			        + "\nfieldName=" + fieldName, e);
		}
		catch (SecurityException e)
		{
			throw new InvalidAnnotationException("exception while trying to get field from field name, bean=" + bean
			        + "\nfieldName=" + fieldName, e);
		}
		return field;
	}

	public static <T extends Entity> Object getFieldValueFromBean(T bean, String fieldName)
	        throws InvalidAnnotationException
	{
		Object value = null;
		Field field = null;
		field = getFieldFromBean(bean, fieldName);
		try
		{
			if (!field.isAccessible())
			{
				field.setAccessible(true);
			}
			value = field.get(bean);
		}
		catch (IllegalArgumentException e)
		{
			throw new InvalidAnnotationException("exception while trying to get value from bean and field, bean="
			        + bean + "\nfield=" + field, e);
		}
		catch (IllegalAccessException e)
		{
			throw new InvalidAnnotationException("exception while trying to get value from bean and field, bean="
			        + bean + "\nfield=" + field, e);
		}
		return value;
	}

	public static <T extends Entity> void setFieldValueInBean(T bean, String fieldName, Object value)
	        throws InvalidAnnotationException
	{
		Field field = null;
		field = getFieldFromBean(bean, fieldName);
		try
		{
			if (!field.isAccessible())
			{
				field.setAccessible(true);
			}
			field.set(bean, value);
		}
		catch (IllegalArgumentException e)
		{
			throw new InvalidAnnotationException("exception while trying to set value of bean,, bean=" + bean
			        + "\nfield=" + field, e);
		}
		catch (IllegalAccessException e)
		{
			throw new InvalidAnnotationException("exception while trying to set value of bean, bean=" + bean
			        + "\nfield=" + field, e);
		}
	}

	/**
	 * @param fieldValueFromBean
	 * @return
	 */
	public static String toString(Object obj)
	{
		return obj == null ? null : obj.toString();
	}

	public static String serialize(Object obj) throws JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}

	public static <T> T deserialize(String str, Class<T> clazz)
	{
		ObjectMapper mapper = new ObjectMapper();
		if (str == null)
		{
			return null;
		}
		try
		{
			return mapper.readValue(str, clazz);
		}
		catch (JsonParseException e)
		{
			logger.info("Exception while trying to deserialize, string=" + str + ", class=" + clazz, e);
		}
		catch (JsonMappingException e)
		{
			logger.info("Exception while trying to deserialize, string=" + str + ", class=" + clazz, e);
		}
		catch (IOException e)
		{
			logger.info("Exception while trying to deserialize, string=" + str + ", class=" + clazz, e);
		}
		return null;
	}
}