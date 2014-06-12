Feature: US114: Disposal to Trade - transaction failure codes and messages
  As a motor trader
  I want to know if my on-line transaction is successful or if it will need further investigation
  So that I can inform the customer of when to expect confirmation of disposal

  Scenario:
    Given that entered details correspond to a valid clean record that has no markers or error codes
    When they attempt to dispose of the vehicle
    Then they are taken to the "Sell a vehicle into the motor trade: summary" page
    And a message is displayed "Your application is being processed and you should receive confirmation letter within four weeks"

  Scenario:
    Given that entered details correspond to a valid record which has markers or error codes
    When they attempt to dispose of the vehicle
    Then they are taken to the "Sell a vehicle into the motor trade: failure" page
    And a message is displayed "We are not able to process your application at this time. Please send the V5C/3 to the following address; DVLA, Longview Road, Morriston, Swansea, SA6 7JL"