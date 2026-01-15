@login
Feature: Login functionality

  @positive
  Scenario Outline: Valid login
    Given I am on the login page
    When I login with username "<username>" and password "<password>"
    Then I should see the dashboard with welcome message "<name>"

    Examples:
      | username   | password    | name |
      | john.doe   | welcome_123 | John |
      | jane.smith | welcome_456 | Jane |

  @negative
  Scenario: Invalid login
    Given I am on the login page
    When I login with username "invalid" and password "wrongpass"
    Then I should see error message "Invalid username or password"

  @negative
  Scenario: Empty username
    Given I am on the login page
    When I login with username "" and password "welcome_123"
    Then the "username" field should be required and login should not proceed

  @negative
  Scenario: Empty password
    Given I am on the login page
    When I login with username "john.doe" and password ""
    Then the "password" field should be required and login should not proceed

  @negative
  Scenario: Empty username and password
    Given I am on the login page
    When I login with username "" and password ""
    Then the "username" field should be required and login should not proceed

  @logout
  Scenario: Logout
    Given I am on the login page
    When I login with username "john.doe" and password "welcome_123"
    And I logout
    Then I am on the login page
