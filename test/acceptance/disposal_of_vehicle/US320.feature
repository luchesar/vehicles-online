Feature: US320: Redirect Browser Back Button After Transaction is Complete or after Service Exit

  Scenario: Browser back button should redirect to Dispose Success while on Dispose Success
    Given that the user has completed the sale of a vehicle and received the transaction confirmation screen
    When the user tries to return to a previous screen without using the "New Disposal" button
    Then the user is returned to the "Dispose Success" page for the service
    And the Trader details are retained

  Scenario: Browser back button should redirect to Vehicle Lookup after pressing 'New Disposal' on Dispose Success
    Given that the user has selected the "New Disposal" button and navigated away from the service
    When the user tries to return to a previous screens in the service
    Then the user is returned to the "Vehicle Lookup" page for the service
    And the Trader details are retained
    And no vehicle details are available

  Scenario: Browser back button should redirect to Vehicle Lookup after pressing 'Exit' on Dispose Success
    Given that the user has selected the "Exit" button and navigated away from the service
    When the user tries to return to a previous screens in the service
    Then the user is returned to the "BeforeYouStart" page for the service
