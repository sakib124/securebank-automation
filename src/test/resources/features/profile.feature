@profile
Feature: View default profile information

  Scenario Outline: View default profile for <name>
    Given I am logged in as "<username>" with password "<password>"
    And I navigate to the profile page
    Then I should see first name "<firstName>"
    And I should see last name "<lastName>"
    And I should see email "<email>"
    And I should see phone "<phone>"
    And I should see address "<address>"

    Examples:
      | name       | username   | password    | firstName | lastName | email                | phone        | address                             |
      | John Doe   | john.doe   | welcome_123 | John      | Doe      | john.doe@email.com   | 555-123-4567 | 123 Main Street, Anytown, ST 12345  |
      | Jane Smith | jane.smith | welcome_456 | Jane      | Smith    | jane.smith@email.com | 555-987-6543 | 456 Oak Avenue, Somewhere, ST 67890 |
