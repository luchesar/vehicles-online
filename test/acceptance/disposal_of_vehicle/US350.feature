Feature: US350: Create unique identifier for each request that can be logged and audited

  Scenario: Add the trackingId to all requests/responses in the application
    Given the web site is running
    When the motor trader goes to the website for the first time
    Then trackingId should be accessible with non empty value

  Scenario: Use the same tracking id if it is present
    Given the motor trader has already been in the website and has trackingId assigned
    When going to the website again
    Then the trackingId should stay the same