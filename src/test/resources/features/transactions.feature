@transactions
Feature: Filter transactions

  @positive
  Scenario Outline: Filter by Account
    Given I am logged in as "john.doe" with password "welcome_123"
    And I navigate to the transactions page
    When I select "<Account>" from the Account filter and apply filters
    Then only transactions for the "<Account>" account should be displayed

    Examples:
      | Account     |
      | Checking    |
      | Savings     |
      | Credit Card |

  @positive
  Scenario: Filter by Type
    Given I am logged in as "jane.smith" with password "welcome_456"
    And I navigate to the transactions page
    When I select "<Type>" from the Type filter and apply filters
    Then only "<Type>" transactions should be displayed

    Examples:
      | Type       |
      | Deposit    |
      | Withdrawal |
      | Transfer   |
      | Payment    |

  @positive
  Scenario: Filter by Date Range
    Given I am logged in as "john.doe" with password "welcome_123"
    And I navigate to the transactions page
    When I select a valid From Date and To Date and apply filters
    Then only transactions within the selected date range should be displayed

  @positive
  Scenario: Filter by Account and Type
    Given I am logged in as "jane.smith" with password "welcome_456"
    And I navigate to the transactions page
    When I select "Savings" from the Account filter and apply filters
    When I select "Deposit" from the Type filter and apply filters
    Then only deposit transactions for the Savings account should be displayed

  @negative
  Scenario: No Results Found
    Given I am logged in as "john.doe" with password "welcome_123"
    And I navigate to the transactions page
    When I select "Savings" from the Account filter and apply filters
    When I select "Withdrawal" from the Type filter and apply filters
    Then the transactions table should display no results
    And the Previous and Next page buttons should be disabled

  @positive
  Scenario: Reset Filters
    Given I am logged in as "john.doe" with password "welcome_123"
    And I navigate to the transactions page
    And I have applied some filters
    When I click the "Reset" button
    Then all filter fields should be reset to their default values

  @positive
  Scenario: Pagination of Transactions
    Given I am logged in as "jane.smith" with password "welcome_456"
    And I navigate to the transactions page
    And there are more transactions than fit on one page
    When I click the Next page button
    Then the next set of transactions should be displayed
    When I click the Previous page button
    Then the previous set of transactions should be displayed
