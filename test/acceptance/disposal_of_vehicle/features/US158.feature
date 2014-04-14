Feature: Disposal to Trade: Business Name - Error Messages
  As a Motor Trader
  I want to receive an appropriate error message when I enter a disallowed Business Name
  so that I can move to the next step in the transaction

  Scenario Outline:
    Given the motor trader has <entered> a business name which conforms to business rules
    When they attempt to submit the business name in addition to other required information
    Then the business name is retained
    And they are taken to the "Select your trade address" page

  Examples:
    | entered       |
    | Widgets Ltd   |
    | ABC123 Ltd    |
    | Plus+         |
    | Hyphen-       |
    | OpenBracket(  |
    | CloseBracket) |
    | FullStop.     |
    | Ampersand&    |
    | Comma,        |
    | At@           |
    | Apostrophe'   |

  Scenario Outline:
    Given the motor trader has <entered> a business name which does not conform to business rules
    When they attempt to submit the business name in addition to other required information
    Then a single error message is displayed "Trader name - Must be between two and 30 characters and not contain invalid characters"
    And they remain on the "Provide your trader details" page

  Examples:
    | entered                         |
    |                                 |
    | a                               |
    | abcdefghijklmnopqrstuvwxyzabcde |
    | 1                               |
    | 1234567890123456789012345678901 |
    | ..                              |
    | Exclamation!                    |
    | Quote”                          |
    | Pound£                          |
    | Dollar$                         |
    | Percentage%                     |
    | Caret^                          |
    | Underscore_                     |
    | Equal=                          |
    | ClosingBracket]                 |
    | OpeningBracket[                 |
    | OpeneingBrace{                  |
    | ClosignBrace}                   |
    | Hash#                           |
    | Tilde~                          |
    | Semicolon;                      |
    | Colon:                          |
    | Forwardslash/                   |
    | Questionmark?                   |
    | GreaterThan>                    |
    | LessThan<                       |
    | Backslash\                      |
    | Backtick`                       |
    | Thing¬                          |