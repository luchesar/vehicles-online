Feature: US74: Disposal to Trade: Remember my trader details when I make a new disposal
  As a motor trader
  I want the system to retain business name and address
  So that it can be reused in subsequent transactions

  Scenario:
    Given that a trader has successfully completed a disposal submission
    When they wish to start a new disposal transaction
    Then the trader name and address details are pre populated