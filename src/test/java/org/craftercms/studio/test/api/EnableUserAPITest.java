package org.craftercms.studio.test.api;

import org.craftercms.studio.test.utils.APIConnectionManager;
import org.craftercms.studio.test.utils.JsonTester;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gustavo Ortiz Alfaro.
 */

public class EnableUserAPITest {

	private JsonTester api;
	private String headerLocationBase;
	private String username = "admin";
	private String password = "admin";
	private String newusername = "jane.doe";
	private String newpassword = "SuperSecretPassword123#";
	private String first_name = "Jane";
	private String last_name = "Doe";
	private String email = "jane@example.com";
	// private String externally_managed = "true";

	public EnableUserAPITest() {
		APIConnectionManager apiConnectionManager = new APIConnectionManager();
		api = new JsonTester(apiConnectionManager.getProtocol(), apiConnectionManager.getHost(),
				apiConnectionManager.getPort());
		headerLocationBase = apiConnectionManager.getHeaderLocationBase();
	}

	@BeforeTest
	public void login() {
		Map<String, Object> json = new HashMap<>();
		json.put("username", username);
		json.put("password", password);
		api.post("/studio/api/1/services/api/1/security/login.json")
		//.urlParam("username", username)
		//.urlParam("password", password)
		.json(json).execute().status(200);
	}

	@Test(priority = 1)
	public void testCreateUser() {
		Map<String, Object> json = new HashMap<>();
		json.put("username", newusername);
		json.put("password", newpassword);
		json.put("first_name", first_name);
		json.put("last_name", last_name);
		json.put("email", email);
		
		api.post("/studio/api/1/services/api/1/user/create.json")
//		.urlParam("username", newusername)
//		.urlParam("password", newpassword)
//		.urlParam("first_name", first_name)
//		.urlParam("last_name", last_name)
//		.urlParam("email", email)
		.json(json).execute().status(201)
				.header("Location", is(headerLocationBase + "/studio/api/1/services/api/1/user/get.json?user="+newusername))
				.json("$.message", is("OK")).debug();
	}

	@Test(priority = 2)
	public void testEnableUser() {
		Map<String, Object> json = new HashMap<>();
		json.put("username", newusername);
		
		api.post("/studio/api/1/services/api/1/user/enable.json")
		//.urlParam("username", newusername)
		.json(json).execute()
				.status(200)
				.header("Location",
						is(headerLocationBase + "/studio/api/1/services/api/1/user/get.json?username="+newusername))
				.json("$.message", is("OK")).debug();

	}

	@Test(priority = 3)
	public void testInvalidParameters() {
		Map<String, Object> json = new HashMap<>();
		json.put("usernamenonvalid", newusername);
		
		api.post("/studio/api/1/services/api/1/user/enable.json")
		//.urlParam("usernamenonvalid", newusername)
		.json(json).execute()
				.status(400).json("$.message", is("Invalid parameter: username")).debug();

	}

	// @Test(priority=4)
	// public void testUnauthorized() {
	// Map<String, Object> json = new HashMap<>();
	// json.put("username", "jane.doeNotFound");
	// api.post("/studio/api/1/services/api/1/user/enable.json").json(json).execute().status(401)
	// .json("$.message", is("Unauthorized")).debug();
	//
	//
	// }

	// @Test(priority=5)
	// public void testExternallyManagedUser() {
	// Map<String, Object> json = new HashMap<>();
	// json.put("username", "jane.doeNotFound");
	// api.post("/studio/api/1/services/api/1/user/enable.json").json(json).execute().status(403)
	// .json("$.message", is("Externally managed user")).debug();
	//
	//
	//
	// }

	@Test(priority = 6)
	public void testUserNotFound() {
		Map<String, Object> json = new HashMap<>();
		json.put("username", newusername + "nonvalid");
		
		api.post("/studio/api/1/services/api/1/user/enable.json")
		//.urlParam("username", newusername + "nonvalid")
		.json(json).execute().status(404).json("$.message", is("User not found")).debug();

	}

	// @Test(priority=7)
	// public void testInternalServerError() {
	// Map<String, Object> json = new HashMap<>();
	// json.put("", "jane.doe");
	// api.post("/studio/api/1/services/api/1/user/enable.json").json(json).execute().status(500)
	// .json("$.message", is("Internal server error"));
	//
	//
	// }

}
