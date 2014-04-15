Feature: US140: Disposal to Trade - Consent for Disposal with Personalised Registration
  As a motor trader
  I want to confirm to the DVLA that the current keeper is aware that if the vehicle they are disposing of has a personalised registration mark then the mark will be disposed of along with the vehicle
  So that I can proceed to the next step of the submission for disposal transaction

  Scenario:
    Given the motor trader has confirmed the acknowledgement of the current keeper
    When they attempt to submit the acknowledgement in addition to other required information
    Then they are taken to the "Dispose a vehicle into the motor trade: summary" page

  Scenario:
    Given the motor trader has not confirmed the acknowledgement of the current keeper
    When they attempt to submit the acknowledgement in addition to other required information
    Then a single error message is displayed "Loss of registration consent - You must confirm that you have made the keeper aware that they will lose the registration mark associated with this vehicle"
    And they remain on the "Complete & confirm" page