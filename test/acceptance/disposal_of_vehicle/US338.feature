Feature: US338: Ability to switch between languages
  As a motor trader
  I want to be able to choose to view the service in either English or Welsh
  So that I can view the site in my preferred language.

  Scenario: Change language from English to Welsh
    Given the site is being viewed in the English Language
    When I select the 'Cymraeg' button in the footer
    Then the site is displayed in Welsh
    And I can continue my transaction without the need to re-enter previously submitted data
    And the language button in the footer will change to 'English'

  Scenario: Change language from Welsh to English
    Given the site is being viewed in the Welsh Language
    When I select the 'English' button in the footer
    Then the site is displayed in English
    And I can continue my transaction without the need to re-enter previously submitted data
    And the language button in the footer will change to 'Cymraeg'