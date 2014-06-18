Feature: US432: Option to Auto Populate Date of Sale Field with Todays Date

  Scenario: On Dispose page use a manually entered date of disposal with javascript disabled
  Given the Trader is on the Complete & Confirm page and javascript is not enabled for the browser
  When the user manually selects a date using the  Date of Sale date drop downs
  Then the user can select "Confirm sale" without error on the date of sale field



