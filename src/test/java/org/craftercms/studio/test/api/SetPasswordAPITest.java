package org.craftercms.studio.test.api;

import org.craftercms.studio.test.utils.APIConnectionManager;
import org.craftercms.studio.test.utils.JsonTester;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Gustavo Ortiz Alfaro
 */

public class SetPasswordAPITest {

    private JsonTester api;
	private String headerLocationBase;
	private String username = "admin";
   	private String password = "admin";
   	private String siteId = "mysite";
	private String description = "Description!";
	private String blueprint = "empty";

    public SetPasswordAPITest(){
    	APIConnectionManager apiConnectionManager = new APIConnectionManager();
		api = new JsonTester(apiConnectionManager.getProtocol()
				, apiConnectionManager.getHost(),apiConnectionManager.getPort());
		headerLocationBase=apiConnectionManager.getHeaderLocationBase();
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


    @Test(priority=1)
	public void testCreateSite() {
    	Map<String, Object> json = new HashMap<>();
		json.put("site_id", siteId);
		json.put("description", description);
		json.put("blueprint", blueprint);
		
		api.post("/studio/api/1/services/api/1/site/create.json")
		//.urlParam("site_id", siteId)
	    //.urlParam("description", description)
		//.urlParam("blueprint", blueprint)
		.json(json).execute().status(201)
				.header("Location",
						is(headerLocationBase + "/studio/api/1/services/api/1/site/get.json?site_id="+siteId))
				.json("$.message", is("OK")).debug();
	}
    
//    @Test(priority=2)
//   	public void testInvalidParameters() {
//   		api.post("/studio/api/1/services/api/1/site/create.json").urlParam("site_idnonvalid", siteId)
//		.urlParam("description", description).urlParam("blueprint", blueprint).execute().status(400)
//   				.json("$.message", is("Invalid parameter(s): [site_id")).debug();
//
//   	}
    
    
    
    
//    @Test(priority=3)
//	public void testUnauthorized() {
//		Map<String, Object> json = new HashMap<>();
//		json.put("site_id", "mySite");
//		json.put("description", "My very first site!");
//		json.put("blueprint", "Empty");
//		api.post("/studio/api/1/services/api/1/site/create.json").json(json).execute().status(401)
//				.json("$.message", is("Unauthorized")).debug();
//
//	}
    
//    @Test(priority=4)
//   	public void testSiteAlreadyExists() {
//   		api.post("/studio/api/1/services/api/1/site/create.json").urlParam("site_id", siteId)
//		.urlParam("description", description).urlParam("blueprint", blueprint).execute().status(409)
//		        .header("Location", is(headerLocationBase+"/studio/api/1/services/api/1/site/get.json?site_id=mySite"))
//   				.json("$.message", is("Site already exists")).debug();
//
//    }
    
//    @Test(priority=5)
//   	public void testInternalServerError() {
//   		Map<String, Object> json = new HashMap<>();
//   		json.put("site_idFAIL", "mySite");
//   		json.put("description", "My very first site!");
//   		json.put("blueprint", "Empty");
//   		api.post("/studio/api/1/services/api/1/site/create.json").json(json).execute().status(500)
//   				.json("$.message", is("Internal server error")).debug();
//
//    }
    

    
}
