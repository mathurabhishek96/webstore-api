package APItest1;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.RestAssured.given;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import com.opencsv.CSVReader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class APICRUDTests {

    static String url = "https://fakestoreapi.com/products";

    // ------------------- GET -------------------
    public static void Get() {
        Response getResponse = RestAssured.get(url);
        System.out.println("GET Status Code: " + getResponse.getStatusCode());

        String responseBody = getResponse.getBody().asString();
        if (!responseBody.trim().startsWith("{") && !responseBody.trim().startsWith("[")) {
            throw new RuntimeException("Response is not valid JSON: " + responseBody);
        }

        JsonPath jsonPath = getResponse.jsonPath();
        List<String> titles = jsonPath.getList("title");

        for (String title : titles) {
            System.out.println(title);
        }

        System.out.println(getResponse.getBody().asPrettyString());
    }

    // ------------------- POST -------------------
    public static void Post() {
        String payload = "{\n" +
                "  \"title\": \"Test Product\",\n" +
                "  \"price\": 29.99,\n" +
                "  \"description\": \"This is a test product\",\n" +
                "  \"image\": \"https://i.pravatar.cc\",\n" +
                "  \"category\": \"electronics\"\n" +
                "}";

        Response postResponse = RestAssured.given().contentType(ContentType.JSON).body(payload).post(url);
        System.out.println("Status Code: " + postResponse.getStatusCode());
        System.out.println("Time: " + postResponse.time());
        System.out.println(postResponse.getBody().asPrettyString());
    }

    // ------------------- PUT -------------------
    public static void Put() {
        String putUrl = url + "/1";
        String payload = "{\n  \"title\": \"Updated Product\",\n  \"price\": 39.99\n}";

        Response putResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payload)
                .put(putUrl);

        System.out.println("Status Code: " + putResponse.getStatusCode());
        System.out.println(putResponse.getBody().asPrettyString());
    }

    // ------------------- DELETE -------------------
    public static void Delete() {
        String deleteUrl = url + "/1";

        Response deleteResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .delete(deleteUrl);

        System.out.println("Status Code: " + deleteResponse.getStatusCode());
    }

    // ------------------- Schema Validation -------------------
    public static void schema() {
        String schemaUrl = url + "/1";

        given()
                .when()
                .get(schemaUrl)
                .then()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("productSchema.json"));

        System.out.println("Schema validation passed");
    }

    // ------------------- Pagination -------------------
    public static void pagination() {
        Response response = given()
                .get(url)
                .then()
                .statusCode(200)
                .extract().response();

        JsonPath js = response.jsonPath();
        int itemsReturned = js.getList("$").size();
        System.out.println("Items returned: " + itemsReturned);

        Assert.assertTrue(itemsReturned <= 20, "Pagination issue: more than 20 items");

        System.out.println(response.asPrettyString());
    }

    // ------------------- File Upload / Download -------------------
    public static void fileUploaddownload() {
        File file = new File("src/test/resources/filetest.txt"); // relative path
        String uploadUrl = "https://file.io";

        Response upload = RestAssured.given()
                .redirects().follow(true)
                .multiPart("file", file)
                .post(uploadUrl);

        if (upload.getStatusCode() == 200) {
            System.out.println("Upload Successful!");
        } else {
            System.out.println("Upload Failed, Status: " + upload.getStatusCode());
        }

        String responseString = upload.getBody().asString();
        System.out.println("Full Response: " + responseString);

        if (responseString.trim().startsWith("{")) {
            try {
                String downloadLink = upload.jsonPath().getString("link");
                System.out.println("Download Link: " + downloadLink);
            } catch (Exception e) {
                System.out.println("Failed to parse JSON: " + e.getMessage());
            }
        }
    }

    public static void downloadFile(String fileUrl, String savePath) {
        try {
            URL urlObj = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
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

    // ------------------- Data-Driven CSV -------------------
    public static void datadrivencsv() throws Exception {
        InputStream csvStream = APICRUDTests.class.getClassLoader().getResourceAsStream("products.csv");
        if (csvStream == null) {
            throw new FileNotFoundException("products.csv not found in resources folder");
        }

        CSVReader reader = new CSVReader(new InputStreamReader(csvStream));
        List<String[]> csvdata = reader.readAll();
        reader.close();

        csvdata.remove(0); // remove header

        Response getResponse = RestAssured.get(url);
        System.out.println("GET Status Code: " + getResponse.getStatusCode());

        JSONArray jsonarray = new JSONArray(getResponse.getBody().asString());

        for (int i = 0; i < csvdata.size(); i++) {
            String[] data = csvdata.get(i);

            int expectedId = Integer.parseInt(data[0].trim());
            String expectedTitle = data[1].trim();
            double expectedPrice = Double.parseDouble(data[2].trim());

            JSONObject jsondata = jsonarray.getJSONObject(i);
            int actualId = jsondata.getInt("id");
            String actualTitle = jsondata.getString("title");
            double actualPrice = jsondata.getDouble("price");

            System.out.println("CSV -> ID: " + expectedId + ", Title: " + expectedTitle + ", Price: " + expectedPrice);
            System.out.println("API -> ID: " + actualId + ", Title: " + actualTitle + ", Price: " + actualPrice);

            Assert.assertEquals(actualId, expectedId, "ID mismatch at row " + (i + 1));
            Assert.assertEquals(actualTitle, expectedTitle, "Title mismatch at row " + (i + 1));
            Assert.assertEquals(actualPrice, expectedPrice, "Price mismatch at row " + (i + 1));

            System.out.println("Row " + (i + 1) + " matched successfully \n");
        }

        System.out.println("All rows matched with API successfully!");
    }

}
