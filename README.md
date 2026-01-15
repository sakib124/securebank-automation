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
│       │           ├── utils/         # Utilities (e.g., DriverManager)
│       │           ├── hooks/         # Cucumber hooks
│       │           └── runner/        # Test runner
│       └── resources/
│           └── features/              # Cucumber feature files
└── target/
    └── ...                            # Build and report outputs
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
- Test reports will be generated in the `target/` directory.

## Writing Tests
- Add new feature files in `src/test/resources/features/`
- Implement step definitions in `src/test/java/com/securebank/stepdefs/`
- Add new page objects in `src/test/java/com/securebank/pages/`

## Debugging
- Debug output is printed to the console for key actions and failures.
- Use scenario tags (`@positive`, `@negative`) to filter and troubleshoot tests.

## Contributing
Pull requests are welcome! Please follow the existing code style and add tests for new features.

## License
This project is licensed under the MIT License.

## Contact
For questions or support, open an issue in the repository.