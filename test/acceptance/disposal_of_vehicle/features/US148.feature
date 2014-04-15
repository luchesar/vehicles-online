Feature: US148: Disposal to Trade - Date/time stamp of transaction
  As a DVLA Caseworker
  I want the date and time of the Disposal to Trade transaction to be generated
  So that it can be saved into the Source Record

  Scenario:
    Given that a trader has successfully completed a disposal submission
    Then a timestamp representing the current date and time is generated and retained