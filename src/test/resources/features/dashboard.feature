@dashboard
Feature: Dashboard navigation

  Scenario: Navigate to dashboard and verify balances
    Given I am logged in as "john.doe" with password "welcome_123"
    When I navigate to the dashboard
    Then I should see welcome message "Welcome, John"
    And I should see checking, savings, and credit balances
