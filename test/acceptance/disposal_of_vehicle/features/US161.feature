Feature: US161: Disposal to Trade - Postcode Lookup Error Message
  As a motor trader
  I want to receive an appropriate error message when I enter a badly formatted postcode
  So that I can correct it and move to the next step in the transaction

  Scenario Outline:
    Given the motor trader has <entered> a postcode which conforms to business rules
    When they attempt to submit the postcode in addition to other required information
    Then the postcode is retained
    And they are taken to the "Select your trade address" page

  Examples:
    | entered  |
    | AA9A 9AA |
    | A9A 9AA  |
    | A9 9AA   |
    | A99 9AA  |
    | AA9 9AA  |
    | AA99 9AA |
    | AA9A9AA  |
    | A9A9AA   |
    | A99AA    |
    | A999AA   |
    | AA99AA   |
    | AA999AA  |

  Scenario Outline:
    Given the motor trader has <entered> a postcode which does not conform to business rules
    When they attempt to submit the postcode in addition to other required information
    Then a single error message is displayed "Postcode - Must be between 5 and 8 characters and in a valid format, eg. PR2 8AE or PR28AE"
    And they remain on the "Provide your trader details" page

  Examples:
    | entered   |
    |           |
    | AA9A  9AA |
    | AA9A9AAAA |
    | AA9A      |
    | 9AA99     |
    | AAAAAAAA  |
    | 99999999  |