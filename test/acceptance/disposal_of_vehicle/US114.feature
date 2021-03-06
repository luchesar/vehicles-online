Feature: US114: Disposal to Trade - transaction failure codes and messages
  As a motor trader
  I want to know if my on-line transaction is successful or if it will need further investigation
  So that I can inform the customer of when to expect confirmation of disposal

  Scenario:
    Given details are entered that correspond to a vehicle that has a valid clean record and has no markers or error codes
    When this is submitted along with any other mandatory information
    Then the next step in the dispose transaction "Sell a vehicle into the motor trade: summary" is shown
    And a message is displayed "A letter will be sent to the name and address on the V5C within 4 weeks. If they do not receive a letter in this time they must contact DVLA Customer Enquiries on 0300 790 6802 as they could still be liable for this vehicle. Please print this page and pass to the customer."

  Scenario:
    Given details are entered that correspond to a vehicle that has a valid record but does have markers or error codes
    When this is submitted along with any other mandatory information
    Then the next step in the dispose transaction "Sell a vehicle into the motor trade: failure" is shown
    And a message is displayed "We are not able to process the application at this time. Please send the V5C/3 to the following address;"