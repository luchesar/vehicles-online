Feature: US149: Disposal to Trade - The consent of the current keeper to dispose of this vehicle
  As a motor trader
  I want to confirm to the DVLA that I have the consent of the current keeper to dispose of this vehicle
  So that I can proceed with the next step of the submission for disposal transaction

  Scenario:
    Given the motor trader has confirmed the consent of the current keeper
    When they attempt to submit the consent in addition to other required information
    Then they are taken to the "Dispose a vehicle into the motor trade: summary" page

  Scenario:
    Given the motor trader has not confirmed the consent of the current keeper
    When they attempt to submit the consent in addition to other required information
    Then a single error message is displayed "Consent - You must have the consent of the registered keeper to notify the sale of this vehicle"
    And they remain on the "Complete & confirm" page