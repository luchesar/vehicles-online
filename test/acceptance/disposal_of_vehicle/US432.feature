Feature: US432: Option to Auto Populate Date of Sale Field with Todays Date

  Scenario: On Dispose page use todays date populates date of disposal field with todays date with javaScript enabled
  Given the Trader is on the Complete & Confirm page and javascript is enabled for the browser
  When the user checks the 'Use today's date' checkbox for Date of Sale
  Then the data of sale will be set to today's date
  And the user can select 'Confirm sale' without error on the date of sale field
