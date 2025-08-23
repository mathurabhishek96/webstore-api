package APItest1;

import io.restassured.response.Response;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;              // for given()
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static org.testng.Assert.assertEquals;

import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class APICRUDTests {
	
	 static String url = "https://fakestoreapi.com/products";

	  public static void Get() {
		  
		  String url = "https://fakestoreapi.com/products";
		  
		  Response getResponse = RestAssured.get(url);
		  
		  System.out.println("GET Status Code: " + getResponse.getStatusCode());
	        
		  
		     JsonPath jsonPath = getResponse.jsonPath();
		     
		    List<String> titles  = jsonPath.getList("title");
		    
		    for (String title : titles){
		    	System.out.println(title);
		    }
		    
		    System.out.println(getResponse.getBody().asPrettyString());
		     
		    
		  
		    
		   
	  }
	  
	  public static void Post()
	  {
	
	  
	  String payload = "{\n" +
              "  \"title\": \"Test Product\",\n" +
              "  \"price\": 29.99,\n" +
              "  \"description\": \"This is a test product\",\n" +
              "  \"image\": \"https://i.pravatar.cc\",\n" +
              "  \"category\": \"electronics\"\n" +
              "}";
	  
	  
	  Response postResponse  = RestAssured.given().contentType(ContentType.JSON).body(payload).post(url);
	  System.out.println("Status Code: " + postResponse.getStatusCode());
	  System.out.println("Status Code: " + postResponse.time());
	  System.out.println("Full Response:");
	  System.out.println(postResponse.getBody().asPrettyString());
			  }
	  
	  public static void Put() {
		    String url = "https://fakestoreapi.com/products/1"; 

		    String payload = "{\n" +
		            "  \"title\": \"Test Product\",\n" +
		            "  \"price\": 29.99\n" +
		            "}";

		    Response putResponse = RestAssured
		            .given()
		            .contentType(ContentType.JSON)
		            .body(payload)
		            .put(url);

		    System.out.println("Status Code: " + putResponse.getStatusCode());
		    System.out.println("Full Response:");
		    System.out.println(putResponse.getBody().asPrettyString());
		}
	  
	  
	  public static void Delete()
	  {
		  String url = "https://fakestoreapi.com/products/1"; 
		  

		    Response delete = RestAssured
		            .given()
		            .contentType(ContentType.JSON)
		            .delete(url);
		    
		    System.out.println("Status Code: " + delete.getStatusCode());
	  }
	  
	  public static void map()
	  {
		  String url = "https://fakestoreapi.com/products";

	        // JSON payloads as strings
		  String product1 = "{\n" +
				  "  \"title\": \"Product 1\",\n" +
				  "  \"price\": 199.99,\n" +
				  "  \"description\": \"First product\",\n" +
				  "  \"image\": \"https://i.pravatar.cc\",\n" +
				  "  \"category\": \"electronics\"\n" +
				  "}";

				  String product2 = "{\n" +
				  "  \"title\": \"Product 2\",\n" +
				  "  \"price\": 299.99,\n" +
				  "  \"description\": \"Second product\",\n" +
				  "  \"image\": \"https://i.pravatar.cc\",\n" +
				  "  \"category\": \"jewelery\"\n" +
				  "}";


	        // Array of payloads
	        String[] products = {product1, product2};

	        // Loop through each product and POST
	        for (String product : products) {
	            Response res = RestAssured.given()
	            		
	                    .contentType(ContentType.JSON)
	                    .body(product)
	                    .post(url);

	            System.out.println("POST Status Code: " + res.getStatusCode());
	            System.out.println("Response Body:");
	            System.out.println(res.getBody().asPrettyString());
	            System.out.println("-------------------------");
	        }
	    }
	  
	  
	  public static void schema()
	  {
		   String url = "https://fakestoreapi.com/products/1";

	        given()
	        .when()
	            .get(url)
	        .then()
	            .assertThat()
	            .statusCode(200)
	            .body(matchesJsonSchemaInClasspath("productSchema.json"));

	        System.out.println("Schema validation passed");
		  
		  
	  }
	  
	  
	  public static void Posttime()
	  {
	
	  
	  String payload = "{\n" +
              "  \"title\": \"Test Product\",\n" +
              "  \"price\": 29.99,\n" +
              "  \"description\": \"This is a test product\",\n" +
              "  \"image\": \"https://i.pravatar.cc\",\n" +
              "  \"category\": \"electronics\"\n" +
              "}";
	  
	  
	  Response postResponse  = RestAssured.given().contentType(ContentType.JSON).body(payload).post(url);
	  System.out.println("Status Code: " + postResponse.getStatusCode());
	  System.out.println("Status Code: " + postResponse.time());
	  System.out.println("Full Response:");
	  System.out.println(postResponse.getBody().asPrettyString());
			  }
	  
	  public static void PostTime() {
		    String url = "https://fakestoreapi.com/products/1"; 

		    String payload = "{\n" +
		            "  \"title\": \"Test Product\",\n" +
		            "  \"price\": 29.99\n" +
		            "}";

		    Response putResponse = RestAssured
		            .given()
		            .contentType(ContentType.JSON)
		            .body(payload)
		            .post(url);

		    System.out.println("Time Response: " + putResponse.time());
		    System.out.println("Full Response:");
		}
	  
	  public static void pagination()
	  {
		  int page = 1;
	        int limit = 20;
	        String url = "https://fakestoreapi.com/products?page=" + page + "&limit=" + limit;

	        Response response = given()
	                                .get(url)
	                                .then()
	                                .statusCode(200)
	                                .extract().response();

	        JsonPath js = response.jsonPath();

	        // Validate number of items
	        int itemsReturned = js.getList("$").size();  // assumes root is a list
	        
	        System.out.println("Items returned: " + itemsReturned);

	        if(itemsReturned <= limit) {
	            System.out.println("Pagination OK for page " + page);
	        } else {
	            System.out.println("Pagination issue detected!");
	        }

	        // Optional: print items
	        System.out.println("Items:");
	        System.out.println(response.asPrettyString());
	  }
	  
	  
	  public static void fileUploaddownload() {
	        File file = new File("C:\\Users\\mathu\\OneDrive - Griffith College\\Documents\\filetest.txt");
	        String url = "https://file.io";

	        Response upload = RestAssured
	                .given()
	                .redirects().follow(true)
	                .multiPart("file", file)
	                .post(url);

	        // Check status
	        if(upload.getStatusCode() == 200) {
	            System.out.println("Upload Successful!");
	        } else {
	            System.out.println("Upload Failed, Status: " + upload.getStatusCode());
	        }

	        // Print full response as string (safe)
	        String responseString = upload.getBody().asString();
	        System.out.println("Full Response: " + responseString);

	        // Only parse JSON if it looks like JSON
	        if(responseString.trim().startsWith("{")) {
	            try {
	                String downloadLink = upload.jsonPath().getString("link");
	                System.out.println("Download Link: " + downloadLink);
	            } catch (Exception e) {
	                System.out.println("Failed to parse JSON: " + e.getMessage());
	            }
	        } else {
	            System.out.println("Response is not valid JSON, cannot extract link.");
	        }
	    }
	  
	  
	  public static void downloadFile(String fileUrl, String savePath) {
		  try {
	            URL url = new URL(fileUrl);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");
	            connection.setInstanceFollowRedirects(true);
	            int responseCode = connection.getResponseCode();

	            if (responseCode == HttpURLConnection.HTTP_OK) {
	                try (InputStream in = connection.getInputStream();
	                     FileOutputStream out = new FileOutputStream(savePath)) {

	                    byte[] buffer = new byte[8192]; // 8 KB buffer
	                    int bytesRead;
	                    while ((bytesRead = in.read(buffer)) != -1) {
	                        out.write(buffer, 0, bytesRead);
	                    }
	                    System.out.println("File downloaded to: " + savePath);
	                }
	            } else {
	                System.out.println("Failed. Server returned HTTP " + responseCode);
	            }
	            connection.disconnect();
	        } catch (Exception e) {
	            System.out.println("Download failed: " + e.getMessage());
	        }
	    }

	  
	  public static void datadrivencsv() throws Exception {
	        // 1️⃣ Read CSV
	        CSVReader reader = new CSVReader(new FileReader(
	                "C:\\Users\\mathu\\OneDrive - Griffith College\\Documents\\products.csv"));
	        List<String[]> csvdata = reader.readAll();
	        reader.close();

	        // Remove header
	        csvdata.remove(0);

	        // 2️⃣ Call API
	        String url = "https://fakestoreapi.com/products";
	        Response getResponse = RestAssured.get(url);
	        System.out.println("GET Status Code: " + getResponse.getStatusCode());

	        JSONArray jsonarray = new JSONArray(getResponse.getBody().asString());

	        // 3️⃣ Compare CSV with API
	        for (int i = 0; i < csvdata.size(); i++) {
	            String[] data = csvdata.get(i);

	            // Convert CSV values to proper types
	            int expectedId = Integer.parseInt(data[0].trim());
	            String expectedTitle = data[1].trim();
	            double expectedPrice = Double.parseDouble(data[2].trim());

	            // JSON values
	            JSONObject jsondata = jsonarray.getJSONObject(i);
	            int actualId = jsondata.getInt("id");
	            String actualTitle = jsondata.getString("title");
	            double actualPrice = jsondata.getDouble("price");

	            // Print values
	            System.out.println("CSV -> ID: " + expectedId + ", Title: " + expectedTitle + ", Price: " + expectedPrice);
	            System.out.println("API -> ID: " + actualId + ", Title: " + actualTitle + ", Price: " + actualPrice);

	            // Assertions
	            assert expectedId == actualId : "ID mismatch at row " + (i + 1);
	            assert expectedTitle.equals(actualTitle) : "Title mismatch at row " + (i + 1);
	            assert expectedPrice == actualPrice : "Price mismatch at row " + (i + 1);

	            System.out.println("Row " + (i + 1) + " matched successfully \n");
	        }

	        System.out.println("All rows matched with API successfully!");
	    }

		     
		  
		  
	  }
	  

