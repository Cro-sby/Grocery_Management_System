Feature: Register, update, and delete employees
  As a manager, I want to create new employees or delete existing ones.
  As an employee, I want to update my account information so that I can keep it up to date.

  Background: 
    Given the following employees exist in the system
      | username | password | name           | phone          |
      | alice    | alice123 | Alice Allisson | (514) 555-1111 |
      | bob      | password | Bob Robertson  | (514) 555-2222 |
      | claire   | password | Claire Clark   | (514) 555-3333 |
    And the following customers exist in the system
      | username  | password         | name             | phone          | address                | points |
      | obiwan212 | highground       | Obi-Wan Kenobi   | (438) 555-1234 | Jedi Temple, Coruscant |    212 |
      | anakin501 | i-dont-like-sand | Anakin Skywalker | (514) 555-9876 | Jedi Temple, Coruscant |    501 |
      | alice     | ---              | ---              | ---            | 123 Alice Avenue       |      2 |

  Scenario Outline: Successfully register employee who does not already exist
    When the manager attempts to add a new employee with username "<username>"
    Then the system shall not raise any errors
    And an employee shall exist with username "<username>"
    And the employee's password shall be "employee"
    And the employee's name shall be ""
    And the employee's phone number shall be ""
    And the total number of users shall be 7
    And the total number of customers shall be 3
    And the total number of employees shall be 4
    And there shall be one manager

    Examples: 
      | username |
      | doug     |
      | Eve000   |
      | 11FRED   |

  Scenario Outline: Try to register new employee with duplicate username
    When the manager attempts to add a new employee with username "<username>"
    Then the system shall raise the error "username is already in use"
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 3
    And there shall be one manager

    Examples: 
      | username  |
      | alice     |
      | bob       |
      | claire    |
      | obiwan212 |
      | anakin501 |

  Scenario: Try to register new employee with blank username
    When the manager attempts to add a new employee with username ""
    Then the system shall raise the error "username is required"
    And no user shall exist with username ""
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 3
    And there shall be one manager

  Scenario Outline: Successfully add employee account for a customer
    When the manager attempts to create an employee account for "<username>"
    Then the system shall not raise any errors
    And an employee shall exist with username "<username>"
    And the employee's password shall be "<password>"
    And the employee's name shall be "<name>"
    And the employee's phone number shall be "<phone>"
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 4
    And there shall be one manager

    Examples: 
      | username  | password         | name             | phone          |
      | obiwan212 | highground       | Obi-Wan Kenobi   | (438) 555-1234 |
      | anakin501 | i-dont-like-sand | Anakin Skywalker | (514) 555-9876 |

  Scenario: Try to add employee account when the user already has one
    When the manager attempts to create an employee account for "alice"
    Then the system shall raise the error "\"alice\" already has an employee account"
    And an employee shall exist with username "alice"
    And the employee's password shall be "alice123"
    And the employee's name shall be "Alice Allisson"
    And the employee's phone number shall be "(514) 555-1111"
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 3
    And there shall be one manager

  Scenario Outline: Try to add employee account to user that doesn't exist
    When the manager attempts to create an employee account for "<username>"
    Then the system shall raise the error "there is no user with username \"<username>\""
    And no user shall exist with username "<username>"
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 3
    And there shall be one manager

    Examples: 
      | username    |
      | nonexistent |
      | idontexist  |
      | ghost       |

  Scenario Outline: Successfully update account info
    When "<username>" attempts to change their <field> to "<newValue>"
    Then the system shall not raise any errors
    And an employee shall exist with username "<username>"
    And the employee's password shall be "<password>"
    And the employee's name shall be "<name>"
    And the employee's phone number shall be "<phone>"
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 3
    And there shall be one manager

    Examples: 
      | username | field        | newValue       | password   | name           | phone          |
      | alice    | password     | mypassw0rd     | mypassw0rd | Alice Allisson | (514) 555-1111 |
      | bob      | password     | bobobobob      | bobobobob  | Bob Robertson  | (514) 555-2222 |
      | alice    | name         | Alice Alice    | alice123   | Alice Alice    | (514) 555-1111 |
      | claire   | name         |                | password   |                | (514) 555-3333 |
      | alice    | phone number | (438) 555-0000 | alice123   | Alice Allisson | (438) 555-0000 |
      | bob      | phone number | (438) 555-9876 | password   | Bob Robertson  | (438) 555-9876 |

  Scenario Outline: Try to update employee with invalid data
    When "<username>" attempts to change their <field> to "<newValue>"
    Then the system shall raise the error "<error>"
    And an employee shall exist with username "<username>"
    And the employee's password shall be "<password>"
    And the employee's name shall be "<name>"
    And the employee's phone number shall be "<phone>"
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 3
    And there shall be one manager

    Examples: 
      | username | field        | newValue     | password | name           | phone          | error                                                            |
      | alice    | password     |              | alice123 | Alice Allisson | (514) 555-1111 | password is required                                             |
      | alice    | phone number | 438-555-0000 | alice123 | Alice Allisson | (514) 555-1111 | phone number does not match expected format \\"(xxx) xxx-xxxx\\" |

  Scenario Outline: Successfully delete employee who is not a customer
    When the manager attempts to delete employee "<username>"
    Then the system shall not raise any errors
    And no user shall exist with username "<username>"
    And the total number of users shall be 5
    And the total number of customers shall be 3
    And the total number of employees shall be 2
    And there shall be one manager

    Examples: 
      | username |
      | bob      |
      | claire   |

  Scenario: Successfully delete employee who is also a customer
    When the manager attempts to delete employee "alice"
    Then the system shall not raise any errors
    And no employee shall exist with username "alice"
    But a customer shall exist with username "alice"
    And the customer's password shall be "alice123"
    And the customer's address shall be "123 Alice Avenue"
    And the customer's name shall be "Alice Allisson"
    And the customer's phone number shall be "(514) 555-1111"
    And the customer shall have 2 points
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 2
    And there shall be one manager

  Scenario Outline: Try to delete an employee that doesn't exist
    When the manager attempts to delete employee "<username>"
    Then the system shall raise the error "<error>"
    And no employee shall exist with username "<username>"
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 3
    And there shall be one manager

    Examples: 
      | username    | error                                            |
      | nonexistent | there is no user with username \\"nonexistent\\" |
      | ghost       | there is no user with username \\"ghost\\"       |
      | obiwan212   | \\"obiwan212\\" is not an employee               |
      | anakin501   | \\"anakin501\\" is not an employee               |
