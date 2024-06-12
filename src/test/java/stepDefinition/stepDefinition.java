package stepDefinition;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.io.IOException;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.APIResources;
import resources.Utils;
import resources.VideoDecoder;
import resources.VideoEncoder;

public class stepDefinition extends Utils {
    RequestSpecification res, request;
    ResponseSpecification resspec;
    Response response;
    
    String name;
    String updateURL,requestBody;
    String token;
    VideoEncoder encoder = new VideoEncoder();
    VideoDecoder decoder = new VideoDecoder();

    public stepDefinition() throws IOException {
        token = getGlobalValue("token");
    }

    @Given("Create VideoEncoder with {string}, {string}, {string}, {string}")
    public void create_video_encoder_with(String Parent, String DisplayName, String EncoderIdentifier, String Vendor) throws IOException {
        res = given().spec(requestSpecification()).header("accept", "application/json")
                .header("Authorization", token)
                .body(encoder.createEncoderPayload(Parent, DisplayName, EncoderIdentifier, Vendor));
    }

    @Given("Create VideoDecoder with {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}")
    public void create_video_decoder_with(String Parent, String DisplayName, String Vendor, String DecoderIdentifier, String SupportedChannelVendor, String VendorChannelIdentifier, String ProjectID, String Zone, String InstanceName) throws IOException {
        res = given().spec(requestSpecification()).header("accept", "application/json")
                .header("Authorization", token)
                .body(decoder.createDecoderPayload(Parent, DisplayName, Vendor, DecoderIdentifier, SupportedChannelVendor, VendorChannelIdentifier, ProjectID, Zone, InstanceName));
    }

    @When("User calls {string} with {string} API request")
    public void user_calls_with_api_request(String resource, String method) throws IOException {
        APIResources resourceAPI = APIResources.valueOf(resource);
        System.out.println("Calling API: " + resourceAPI.getResource());
        resspec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

        switch (method.toUpperCase()) {
            case "GET":
                response = given().spec(requestSpecification()).header("accept", "application/json")
                        .header("Authorization", token).pathParam("name", name).when().get(resourceAPI.getResource());
                System.out.println("Get Request Extracted name: " + name);
                break;
            case "DELETE":
                response = given().spec(requestSpecification()).header("accept", "application/json")
                        .header("Authorization", token).pathParam("name", name).when().delete(resourceAPI.getResource());
                System.out.println("Delete Request Extracted name: " + name);
                break;
            case "POST":
                handlePostRequest(resourceAPI);
                break;
            case "PATCH":
                handlePatchRequest(resourceAPI);
                break;
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }
    }

    private void handlePostRequest(APIResources resourceAPI) throws IOException {
        switch (resourceAPI) {
            case AddEncoderAPI:
            case AddDecoderAPI:
                response = res.when().post(resourceAPI.getResource());
                name = response.jsonPath().getString("name");
                System.out.println("Extracted name: " + name);
                break;

            case DisableEncoderAPI:
            case ReenableEncoderAPI:
            case DisableDecoderAPI:
            case ReenableDecoderAPI:
                String action;
                if (resourceAPI == APIResources.DisableEncoderAPI || resourceAPI == APIResources.DisableDecoderAPI) {
                    action = ":disable";
                } else {
                    action = ":reenable";
                }
                performActionPostRequest(name + action);
                break;

            case ListEncoderAPI:
            case ListDecoderAPI: // Assuming there's a similar listing API for decoders
                response = given().spec(requestSpecification()).header("accept", "application/json")
                        .header("Authorization", token).body("{}").when().post(resourceAPI.getResource());
                break;

            default:
                throw new IllegalArgumentException("Invalid resource API: " + resourceAPI);
        }
    }

    private void handlePatchRequest(APIResources resourceAPI) throws IOException {
        String updateURL;
        if (resourceAPI == APIResources.UpdateEncoderAPI) {
            updateURL = resourceAPI.getResource() + "/" + name + "?field_mask=display_name,vendor_encoder_identifier";
            response = given().spec(requestSpecification())
                    .header("accept", "application/json")
                    .header("Authorization", token)
                    .body(encoder.updateEncoderPayload("Updated DisplayName", "Updated EncoderIdentifier"))
                    .when()
                    .patch(updateURL);
        } else if (resourceAPI == APIResources.UpdateDecoderAPI) {
            updateURL = String.format("%s/%s?field_mask=display_name,vendor_decoder_identifier,project_id,zone,instance_name,vendor_channel_identifier", resourceAPI.getResource(), name);
            requestBody = decoder.updateDecoderPayload("Updated DisplayName", "Updated DecoderIdentifier", "Updated ProjectID", "Updated Zone", "Updated InstanceName", "Updated VendorChannelIdentifier");
            response = given().spec(requestSpecification())
                    .header("accept", "application/json")
                    .header("Authorization", token)
                    .body(requestBody)
                    .when()
                    .patch(updateURL);
        } else {
            throw new IllegalArgumentException("Invalid resource API for PATCH request: " + resourceAPI);
        }

        // Print out relevant information for debugging
        System.out.println("Patch Request for: " + name);
        System.out.println("Update API URL: " + updateURL);
        System.out.println(response.asString());
        
        // Assert the response status code
        assertEquals(response.getStatusCode(), 200, "Expected status code 200, but found " + response.getStatusCode());
    }



    private void performActionPostRequest(String resourceWithAction) throws IOException {
        response = given().spec(requestSpecification()).header("accept", "application/json")
                .header("Authorization", token).body("{}").when().post(resourceWithAction);
    }

    @Then("the API calls got success with {int}")
    public void the_api_calls_got_success_with(Integer statusCode) {
        assertEquals(response.getStatusCode(), statusCode.intValue());
    }

    @Then("{string} in response body as {string}")
    public void in_response_body_as(String key, String expectedValue) {
        String actualValue = getJsonPath(response, key);
        assertEquals(actualValue, expectedValue);
        System.out.println(actualValue);
    }

    @And("verify name created maps to {string} using {string}")
    public void verify_name_created_maps_to_using(String expectedName, String resource) throws IOException {
        String createdName = getJsonPath(response, "name");
        name = createdName; // Save the created name for later use

        request = given().spec(requestSpecification()).header("accept", "application/json").header("Authorization", token);
        user_calls_with_api_request(resource, "GET");

        String actualName = getJsonPath(response, "name");
        System.out.println("Verified name: " + actualName);
        assertEquals(actualName, createdName);
        System.out.println("Passed");
    }

    @Given("Delete the Encoder by its {string} using {string}")
    public void delete_the_encoder_by_its_using(String encoderName, String resource) throws IOException {
        name = encoderName; // Set the name to the provided encoderName

        request = given().spec(requestSpecification()).header("accept", "application/json").header("Authorization", token);
        user_calls_with_api_request(resource, "DELETE");
    }
}
