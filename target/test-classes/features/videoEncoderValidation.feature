@tag
Feature: Validating the swagger API
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
  
  @VideoDecoder
  Scenario: Verify whether Video Decoder API is working properly
    Given Create VideoDecoder with "<Parent>", "<DisplayName>", "<Vendor>", "<DecoderIdentifier>", "<SupportedChannelVendor>", "<VendorChannelIdentifier>", "<ProjectID>", "<Zone>", "<InstanceName>"
    When User calls "AddDecoderAPI" with "POST" API request
    Then "state" in response body as "STATE_IDLE"
    And verify name created maps to "name" using "GetDecoderAPI"
    And the API calls got success with 200
    And "state" in response body as "STATE_IDLE"
    And User calls "DisableDecoderAPI" with "POST" API request
    And User calls "ReenableDecoderAPI" with "POST" API request
    And User calls "UpdateDecoderAPI" with "PATCH" API request
    #And the API calls got success with 200
    And "state" in response body as "STATE_IDLE"
    And User calls "ListDecoderAPI" with "POST" API request
    When User calls "DeleteDecoderAPI" with "DELETE" API request
    Then the API calls got success with 200
    Then "state" in response body as "STATE_IDLE"

  Examples:
    | Parent                       | DisplayName | Vendor | DecoderIdentifier | SupportedChannelVendor | VendorChannelIdentifier | ProjectID | Zone | InstanceName |
    | org/Test_Org/orgUnits/Unit-01| Test-01     | LTN    | Test-01           | PHENIX                 | test                    | test      | test | InstanceName |

 