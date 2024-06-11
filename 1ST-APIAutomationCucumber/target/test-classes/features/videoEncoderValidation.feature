@tag
Feature: Validating the VideoEncoder API
  I want to use this template for my feature file

  @CreateEncoder
  Scenario: Verify whether Video Encoder is being successfully created
    Given Create VideoEncoder with "<Parent>", "<DisplayName>", "<EncoderIdentifier>", "<Vendor>"
    When User calls "AddEncoderAPI" with "POST" API request
    Then the API calls got success with 200
    Then "state" in response body as "STATE_ACTIVE"
    And verify name created maps to "name" using "GetEncoderAPI"
    And the API calls got success with 200
    And "state" in response body as "STATE_ACTIVE"
    And User calls "DisableEncoderAPI" with "POST" API request
    And User calls "ReenableEncoderAPI" with "POST" API request
    And User calls "UpdateEncoderAPI" with "PATCH" API request
    And the API calls got success with 200
    And "state" in response body as "STATE_ACTIVE"
    And User calls "ListEncoderAPI" with "POST" API request
    And User calls "DeleteEncoderAPI" with "DELETE" API request
    And the API calls got success with 200
    Then "state" in response body as "STATE_ACTIVE"
    

Examples:
  | Parent                       | DisplayName | EncoderIdentifier | Vendor |
  | org/Test_Org/orgUnits/Unit-01| Test-01     | Test-01       | LTN    |
	
#	@DeleteEncoder
#	Scenario: Verify whether Video Encoder is being deleted successfully
    #Given Delete the Encoder by its "name" using "DeleteEncoderAPI"
    #When User calls "DeleteEncoderAPI" with "DELETE" API request "name"
    #Then the API calls got success with 200
#		Then "state" in response body as "STATE_ACTIVE"

#	 
	 
#	@DeleteEncoder
#	Scenario:  Verify whether Video Encoder is being get successfully
#		Given DeleteEncoderAPI payload
#		When User calls "DeleteEncoderAPI" with "DELETE" API request
    #Then the API calls got success with 200
#		Then "state" in response body as "STATE_ACTIVE"