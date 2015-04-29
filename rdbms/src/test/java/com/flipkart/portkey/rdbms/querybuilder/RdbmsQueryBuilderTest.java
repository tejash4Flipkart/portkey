package com.flipkart.portkey.rdbms.querybuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.flipkart.portkey.rdbms.dao.Person;
import com.flipkart.portkey.rdbms.metadata.RdbmsMetaDataCache;
import com.flipkart.portkey.rdbms.metadata.RdbmsTableMetaData;

public class RdbmsQueryBuilderTest
{
	Logger logger = Logger.getLogger(RdbmsQueryBuilderTest.class);

	@Test
	public void testGetInsertQuery()
	{
		RdbmsTableMetaData metaData = RdbmsMetaDataCache.getInstance().getMetaData(Person.class);
		String actual = RdbmsQueryBuilder.getInstance().getInsertQuery(metaData);
		String expected =
		        "INSERT INTO person" + "\n (`id`,`first_name`,`last_name`,`age`,`mod_count`,`last_modified`)"
		                + "\nVALUES (:id,:first_name,:last_name,:age,mod_count+1,now())";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetUpsertQuery()
	{
		String expected =
		        "INSERT INTO person"
		                + "\n (`id`,`first_name`,`last_name`,`age`,`mod_count`,`last_modified`)"
		                + "\nVALUES (:id,:first_name,:last_name,:age,mod_count+1,now())"
		                + "\nON DUPLICATE KEY UPDATE `first_name`=:first_name,`last_name`=:last_name,`age`=:age,`mod_count`=mod_count+1,`last_modified`=now()";
		RdbmsTableMetaData metaData = RdbmsMetaDataCache.getInstance().getMetaData(Person.class);
		String actual = RdbmsQueryBuilder.getInstance().getUpsertQuery(metaData);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetUpsertQueryWithSpecificColumnsToBeUpdated()
	{
		RdbmsTableMetaData metaData = RdbmsMetaDataCache.getInstance().getMetaData(Person.class);
		List<String> fieldsToBeUpdatedOnDuplicate = new ArrayList<String>();
		fieldsToBeUpdatedOnDuplicate.add("firstName");
		fieldsToBeUpdatedOnDuplicate.add("lastName");
		fieldsToBeUpdatedOnDuplicate.add("modCount");
		fieldsToBeUpdatedOnDuplicate.add("lastModified");
		String expected =
		        "INSERT INTO person"
		                + "\n (`id`,`first_name`,`last_name`,`age`,`mod_count`,`last_modified`)"
		                + "\nVALUES (:id,:first_name,:last_name,:age,mod_count+1,now())"
		                + "\nON DUPLICATE KEY UPDATE `first_name`=:first_name,`last_name`=:last_name,`mod_count`=mod_count+1,`last_modified`=now()";
		String actual = RdbmsQueryBuilder.getInstance().getUpsertQuery(metaData, fieldsToBeUpdatedOnDuplicate);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetUpdateByPkQuery()
	{
		String expected =
		        "INSERT INTO person"
		                + "\n (`id`,`first_name`,`last_name`,`age`,`mod_count`,`last_modified`)"
		                + "\nVALUES (:id,:first_name,:last_name,:age,mod_count+1,now())"
		                + "\nON DUPLICATE KEY UPDATE `first_name`=:first_name,`last_name`=:last_name,`age`=:age,`mod_count`=mod_count+1,`last_modified`=now()";
		RdbmsTableMetaData metaData = RdbmsMetaDataCache.getInstance().getMetaData(Person.class);
		String actual = RdbmsQueryBuilder.getInstance().getUpsertQuery(metaData);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetUpdateByCriteriaQuery()
	{
		String expected =
		        "UPDATE person" + "\nSET `firstName`=:firstName, `lastName`=:lastName, `mod_count`=mod_count+1"
		                + "\nWHERE (`id`=:id)";
		String tableName = "person";
		List<String> columnsToBeUpdated = new ArrayList<String>();
		columnsToBeUpdated.add("firstName");
		columnsToBeUpdated.add("lastName");
		columnsToBeUpdated.add("mod_count");
		List<String> columnsInCriteria = new ArrayList<String>();
		columnsInCriteria.add("id");
		Map<String, Object> columnToValueMap = new HashMap<String, Object>();
		columnToValueMap.put("firstName", "someFirstName");
		columnToValueMap.put("lastName", "someLastName");
		RdbmsSpecialValue modCount = new RdbmsSpecialValue();
		modCount.setValue("mod_count+1");
		columnToValueMap.put("mod_count", modCount);
		columnToValueMap.put("id", "someId");
		String actual =
		        RdbmsQueryBuilder.getInstance().getUpdateByCriteriaQuery(tableName, columnsToBeUpdated,
		                columnsInCriteria, columnToValueMap);
		Assert.assertEquals(expected, actual);
	}

	@Test
	@Ignore
	public void testGetDeleteByCriteriaQuery()
	{

	}
}
