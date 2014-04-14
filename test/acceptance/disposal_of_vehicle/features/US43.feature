Feature: US43: Validate document reference entry format - Error Messages
  As a Motor Trader
  I want to receive an appropriate error message when I enter an invalid document reference number
  so that I can correct it and move to the next step in the transaction

  Scenario: Valid format document reference number entered
    Given a motor trader has entered a doc ref number in a valid format
    When they attempt to submit the information
    Then the doc ref number is retained
    And they move to the next step in the transaction

  Scenario Outline: Invalid format document reference number entered
    Given a motor trader has <entered> a doc ref number in an invalid format
    When they attempt to submit the information
    Then a single error message is displayed "Document reference number - Must be an 11-digit number"
    And they remain at the current stage in the transaction

  Examples:
    | entered      |
    |              |
    | 123          |
    | 123456789012 |
    | abcdefghijk  |
    | abc123abc12  |
