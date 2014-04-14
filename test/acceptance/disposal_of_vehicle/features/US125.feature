Feature: US125: Date of Disposal - Business Rules
  As a motor trader
  I want to enter the date of disposal of a vehicle
  So that I can complete the transaction

  Scenario:
    Given the motor trader has entered a valid calendar date which conforms to the business rules
    When they attempt to submit the date in addition to other required information
    Then they are taken to the "Dispose a vehicle into the motor trade: summary" page

  Scenario:
    Given the motor trader has entered a valid calendar date which does not conform to the business rules
    When they attempt to submit the date in addition to other required information
    Then a single error message is displayed "Date of disposal - Must be between today and two years ago"
    And they remain on the "Complete & confirm" page