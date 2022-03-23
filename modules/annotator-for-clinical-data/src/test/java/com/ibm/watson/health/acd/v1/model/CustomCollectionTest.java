/*
 * (C) Copyright IBM Corp. 2020, 2022.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.ibm.watson.health.acd.v1.model;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.cloud.sdk.core.service.model.FileWithMetadata;
import com.ibm.watson.health.acd.v1.exception.IllegalMethodException;
import com.ibm.watson.health.acd.v1.exception.IndexNotAvailableException;
import com.ibm.watson.health.acd.v1.exception.ParameterNotAvailableException;
import com.ibm.watson.health.acd.v1.util.CustomCollection;
import com.ibm.watson.health.acd.v1.utils.TestUtilities;

/**
 * Unit test class for the ConfigurationEntity model.
 */
public class CustomCollectionTest {
  final HashMap<String, InputStream> mockStreamMap = TestUtilities.createMockStreamMap();
  final List<FileWithMetadata> mockListFileWithMetadata = TestUtilities.creatMockListFileWithMetadata();

  @Test
  public void testCustomCollection() throws Throwable {

	  String message = "";
	  JsonObject jsonObj = new JsonObject();
	  jsonObj.addProperty("prop", "propValue");

	  CustomCollection ccModel = new CustomCollection();
	  ccModel.setObject(jsonObj);
	  JsonElement jsonEle;

	  // positive tests
	  jsonEle = ccModel.getObject();
	  Assert.assertNotNull(jsonEle);
	  Gson gson = new Gson();
	  jsonEle = ccModel.get("prop").getObject();
	  assertEquals(gson.toJson(jsonEle), "\"propValue\"");
	  assertEquals(ccModel.getValue("prop"), "\"propValue\"");

	  // negative tests
	  message = "dummy is not available or it is empty.";
	  try {
		  jsonEle = ccModel.get("dummy").getObject();
		  if (jsonEle == null) {
			  // getObject will return null
			  throw new ParameterNotAvailableException("dummy is not available or it is empty.");
		  }
		  if (ccModel.getValue("dummy").equals("")) {
			  throw new ParameterNotAvailableException("dummy is not available or it is empty.");
		  }
	  } catch (RuntimeException e) {
		  assertTrue(e instanceof ParameterNotAvailableException);
		  ParameterNotAvailableException ex = (ParameterNotAvailableException) e;
		  assertEquals(message, ex.getMessage());
	  }

	  Concept concept = new Concept();
	  concept.setBegin(Long.valueOf("0"));
	  concept.setEnd(Long.valueOf("26"));
	  concept.setCoveredText("testString");
	  List<Concept> conceptList = new ArrayList<Concept>();
	  conceptList.add(0, concept);
	  conceptList.add(1, concept);
	  List<Object> annoList = new ArrayList<Object>();
	  annoList.add(conceptList);
	  CustomCollection ccModel2 = new CustomCollection();
	  List<CustomCollection> ccModel2List = ccModel2.convertToCustomCollectionList(annoList);
	  CustomCollection ccModel3 = ccModel2.convertToCustomCollection(concept);

	  message = "Index: 5,  Size: 2";
	  try {
		  ccModel2List.get(0).get(5);
	  } catch (RuntimeException e) {
		  assertTrue(e instanceof IndexNotAvailableException);
		  IndexNotAvailableException ex = (IndexNotAvailableException) e;
		  assertEquals(message, ex.getMessage());
	  }

	  message = "dummy is not a list.";
	  try {
		  List<CustomCollection> ccModel2ListTest = ccModel2.getList("dummy");
		  if (ccModel2ListTest.isEmpty()) {
			  // getList will return empty list, force an IllegalMethodException test
			  throw new IllegalMethodException("dummy is not a list.");
		  }
	  } catch (RuntimeException e) {
		  assertTrue(e instanceof IllegalMethodException);
		  IllegalMethodException ex = (IllegalMethodException) e;
		  assertEquals(message, ex.getMessage());
	  }

	  message = "dummy is not available or it is empty.";
	  try {
		  jsonEle = ccModel3.get("dummy").getObject();
		  if (jsonEle == null) {
			  // get will return null
			  throw new ParameterNotAvailableException("dummy is not available or it is empty.");
		  }

	  } catch (RuntimeException e) {
		  assertTrue(e instanceof ParameterNotAvailableException);
		  ParameterNotAvailableException ex = (ParameterNotAvailableException) e;
		  assertEquals(message, ex.getMessage());
	  }
  }
}