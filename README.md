# SecureBankAutomation

## Overview
SecureBankAutomation is a robust Selenium and Cucumber-based test automation framework for the SecureBank web application. It follows the Page Object Model (POM) design pattern for maintainability and scalability, and uses TestNG for assertions and reporting.

## Features
- Automated UI tests for login, dashboard, profile, transactions, and transfer functionalities
- Page Object Model for clean separation of concerns
- Cucumber BDD for readable, maintainable feature files
- Positive and negative scenario tagging for targeted test runs
- Parameterized tests using scenario outlines and examples
- Robust error handling and debug output for troubleshooting
- Database validation scenarios are included with proper setup and teardown hooks

## Project Structure
```
SecureBankAutomation/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   └── java/
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── securebank/
│       │           ├── pages/         # Page Object classes
│       │           ├── stepdefs/      # Step Definitions
│       │           ├── utils/         # Utilities (e.g., DriverManager, DatabaseHelper)
│       │           ├── hooks/         # Cucumber hooks
│       │           └── runner/        # Test runner
│       └── resources/
│           └── features/              # Cucumber feature files
├── target/                            # Build and report outputs
└── test-output/                       # TestNG and Cucumber HTML reports
```

## Getting Started
### Prerequisites
- Java 11 or higher
- Maven
- ChromeDriver (or update DriverManager for your browser)

## Prerequisite: Set Up the SecureBank Application

Before running the automation tests, you must set up both the frontend and backend of the SecureBank application on your local machine. Follow the instructions in the [bank-application-demo repository](https://github.com/sakib124/bank-application-demo) to:

1. Clone and set up the backend server.
2. Clone and set up the frontend client.
3. Start both the backend and frontend so the application is running and accessible at `http://localhost:8080`.

Once the application is running locally, you can proceed with the automation steps below.

### Setup
1. Clone the repository:
   ```
   git clone <your-repo-url>
   cd SecureBankAutomation
   ```
2. Install dependencies:
   ```
   mvn clean install
   ```
3. Update browser driver path in `DriverManager.java` if needed.

### Running Tests
- To run all tests:
  ```
  mvn test
  ```
- To run specific scenarios (e.g., only positive tests):
  ```
  mvn test -Dcucumber.filter.tags="@positive"
  ```
- To run only database scenarios:
  ```
  mvn test -Dcucumber.filter.tags="@database"
  ```
- Test reports will be generated in the `target/` and `test-output/` directories.

## Tagging and Hooks
- Database connection hooks (`@Before`/`@After`) are only run for scenarios tagged with `@database`
- To ensure database setup/teardown, tag your scenarios appropriately in the feature files.
- If you want hooks to run for all scenarios, remove the tag filter from the hook annotations in `DatabaseStepDefs.java`.

## Writing Tests
- Add new feature files in `src/test/resources/features/`
- Implement step definitions in `src/test/java/com/securebank/stepdefs/`
- Add new page objects in `src/test/java/com/securebank/pages/`

## Debugging & Troubleshooting
- Debug output is printed to the console for key actions and failures.
- Use scenario tags (`@positive`, `@negative`, `@database`) to filter and troubleshoot tests.
- If you see `NullPointerException` for WebDriver, ensure page objects are created inside steps, not as class fields.
- For pagination tests, ensure enough transactions exist to trigger multiple pages.
- If a locator is not found, verify the selector in the page object matches the current UI.

## Contributing
Pull requests are welcome! Please follow the existing code style and add tests for new features.

## License
This project is licensed under the MIT License.

## Contact
For questions or support, open an issue in the repository.