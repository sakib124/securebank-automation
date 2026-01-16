@transfer
Feature: Transfer money between accounts

  @e2e @happy-path @positive
  Scenario Outline: Transfer money between accounts
    Given I am logged in as "<username>" with password "<password>"
    And I navigate to the transfer page
    When I select "<fromAccount>" as the from account
    And I select "<toAccount>" as the to account
    And I enter amount "<amount>"
    And I enter description "<description>"
    And I submit the transfer
    Then I should see a confirmation modal
    When I confirm the transfer
    Then I should see success message "<successMessage>"
    And the "<toAccount>" balance should increase by "<amount>"
    And the "<fromAccount>" balance should decrease by "<amount>"
    And I should see a recent transaction with description "<description>", amount "<amountDisplay>", and status "<status>"
    And I should see a transaction with description "<description>", amount "<amountDisplay>", from "<fromAccount>", to "<toAccount>", and status "<status>" in the transactions history

    Examples:
      | username   | password    | fromAccount | toAccount | amount | description     | successMessage                              | amountDisplay | status    |
      | john.doe   | welcome_123 | checking    | savings   | 200.00 | Monthly savings | Transfer of $200.00 completed successfully! | $200.00       | completed |
      | john.doe   | welcome_123 | savings     | checking  | 150.00 | Emergency funds | Transfer of $150.00 completed successfully! | $150.00       | completed |
      | jane.smith | welcome_456 | checking    | savings   | 300.00 | Monthly savings | Transfer of $300.00 completed successfully! | $300.00       | completed |
      | jane.smith | welcome_456 | savings     | checking  | 450.00 | Emergency funds | Transfer of $450.00 completed successfully! | $450.00       | completed |
      | john.doe   | welcome_123 | checking    | savings   | 100.00 |                 | Transfer of $100.00 completed successfully! | $100.00       | completed |

  @negative
  Scenario: No From Account selected
    Given I am logged in as "john.doe" with password "welcome_123"
    And I navigate to the transfer page
    When I leave the "From Account" field empty
    Then I should see a missing field prompt "Please select an item in the list."

  @negative
  Scenario: No To Account selected
    Given I am logged in as "jane.smith" with password "welcome_456"
    And I navigate to the transfer page
    When I leave the "To Account" field empty
    Then I should see a missing field prompt "Please select an item in the list"

  @negative
  Scenario Outline: Same account selected
    Given I am logged in as "john.doe" with password "welcome_123"
    And I navigate to the transfer page
    When I select "<fromAccount>" as the from account
    And I select "<toAccount>" as the to account
    And I enter amount "100"
    And I submit the transfer
    Then I should see error message "Cannot transfer to the same account"

    Examples:
      | fromAccount | toAccount |
      | checking    | checking  |
      | savings     | savings   |

  @negative
  Scenario: Amount not entered
    Given I am logged in as "jane.smith" with password "welcome_456"
    And I navigate to the transfer page
    When I select "checking" as the from account
    And I select "savings" as the to account
    When I leave the "Amount" field empty
    And I submit the transfer
    Then I should see a missing field prompt "Please fill out this field"

  @negative
  Scenario: Amount is zero or negative
    Given I am logged in as "john.doe" with password "welcome_123"
    And I navigate to the transfer page
    When I select "checking" as the from account
    And I select "savings" as the to account
    When I enter zero or a negative value in the "Amount" field
    And I submit the transfer
    Then I should see a missing field prompt "Value must be greater than or equal to 0.01"

  @negative
  Scenario: Amount exceeds available balance
    Given I am logged in as "jane.smith" with password "welcome_456"
    And I navigate to the transfer page
    When I select "checking" as the from account
    And I select "savings" as the to account
    When I enter an amount greater than my available balance
    And I submit the transfer
    Then I should see error message "Insufficient funds"

  @negative
  Scenario: Clear form
    Given I am logged in as "john.doe" with password "welcome_123"
    And I navigate to the transfer page
    When I select "checking" as the from account
    And I select "savings" as the to account
    And I enter amount "100.00"
    And I enter description "Monthly savings"
    When I click the clear button
    Then all fields in the transfer form should be reset to their default values