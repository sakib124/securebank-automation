@database
Feature: Database validation for transactions table

  @positive
  Scenario Outline: End-to-end transfer: UI and database verification
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
    And I query the transactions table for the latest transaction for "<fromAccount>" account and user "<username>"
    Then the transaction should exist in the database with description "<description>", amount "<amount>", from "<fromAccount>", to "<toAccount>", and status "<status>"
    When I navigate to the dashboard
    And the "<fromAccount>" account balance in the dashboard should match the database value

    Examples:
      | username   | password    | fromAccount | toAccount | amount | description     | successMessage                              | amountDisplay | status    |
      | john.doe   | welcome_123 | checking    | savings   | 100.00 | Emergency funds | Transfer of $100.00 completed successfully! | $100.00       | completed |
      | jane.smith | welcome_456 | savings     | checking  | 350.00 | Monthly savings | Transfer of $350.00 completed successfully! | $350.00       | completed |
      | john.doe   | welcome_123 | checking    | savings   | 150.00 |                 | Transfer of $150.00 completed successfully! | $150.00       | completed |

  @default @positive
  Scenario: Verify seeded deposit transaction for john.doe's checking account
    When I query the transactions table for transaction_id "TXN001"
    Then the transaction should exist with account_id 1, transaction_type "deposit", amount 3500.00, and status "completed"

  @default @negative
  Scenario: Verify no withdrawal transactions for jane.smith's savings account
    When I query the transactions table for withdrawals for account_id 5
    Then no transactions should be found

  @default @positive
  Scenario: Verify transaction date range for jane.smith's checking account
    When I query the transactions table for account_id 4 between "2026-01-07" and "2026-01-11"
    Then the following transaction_ids should be returned:
      | TXN101 |
      | TXN102 |
      | TXN103 |
      | TXN104 |

  @default @positive
  Scenario: Verify payment transaction for john.doe's credit account
    When I query the transactions table for transaction_id "TXN006"
    Then the transaction should exist with account_id 3, transaction_type "payment", amount -89.99, and status "completed"
