Feature: Register, update, and delete customers
  As a customer, I want to register so that I can shop online.
  As a customer, I want to update my account in case my personal information changes.
  As a customer, I want to delete my account in case I no longer want to shop online.

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

  Scenario Outline: Successfully register customer who does not already exist
    When a customer attempts to register with username "<username>", password "<password>", and address "<address>"
    Then the system shall not raise any errors
    And a customer shall exist with username "<username>"
    And the customer's password shall be "<password>"
    And the customer's address shall be "<address>"
    And the customer's name shall be ""
    And the customer's phone number shall be ""
    And the customer shall have 0 points
    And the total number of users shall be 7
    And the total number of customers shall be 4
    And the total number of employees shall be 3
    And there shall be one manager

    Examples: 
      | username | password      | address         |
      | David    | groC3ry5store | 123 Main St.    |
      | Eve987   | pumpkinsmiam  | 2781 Sherbrooke |

  Scenario Outline: Register customer unsuccessfully due to duplicate username
    When a customer attempts to register with username "<username>", password "<password>", and address "<address>"
    Then the system shall raise the error "<error>"
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 3
    And there shall be one manager

    Examples: 
      | username  | password | address      | error                      |
      | manager   | password | 124 Main St. | username is already in use |
      | alice     | password | 124 Main St. | username is already in use |
      | obiwan212 | password | 124 Main St. | username is already in use |

  Scenario Outline: Register customer unsuccessfully
    When a customer attempts to register with username "<username>", password "<password>", and address "<address>"
    Then the system shall raise the error "<error>"
    And no user shall exist with username "<username>"
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 3
    And there shall be one manager

    Examples: 
      | username    | password | address      | error                                      |
      |             | password | 124 Main St. | username is required                       |
      | david       |          | 124 Main St. | password is required                       |
      | david       | password |              | address is required                        |
      | David David | password | 124 Main St. | username contains an invalid character ' ' |
      | david!      | password | 124 Main St. | username contains an invalid character '!' |

  Scenario Outline: Successfully add a customer account for someone who is already an employee
    When "<username>" attempts to add a customer account with address "<address>"
    Then the system shall not raise any errors
    And a customer shall exist with username "<username>"
    And the customer's password shall be "<password>"
    And the customer's address shall be "<address>"
    And the customer's name shall be "<name>"
    And the customer's phone number shall be "<phone>"
    And the customer shall have 0 points
    And the total number of users shall be 6
    And the total number of customers shall be 4
    And the total number of employees shall be 3
    And there shall be one manager

    Examples: 
      | username | password | name          | phone          | address             |
      | bob      | password | Bob Robertson | (514) 555-2222 | 456 Bob Boulevard   |
      | claire   | password | Claire Clark  | (514) 555-3333 | 789 Clearview Drive |

  Scenario: Try to add a customer account when the employee already has one
    When "alice" attempts to add a customer account with address "222 Main St."
    Then the system shall raise the error "\"alice\" already has a customer account"
    And a customer shall exist with username "alice"
    And the customer's password shall be "alice123"
    And the customer's address shall be "123 Alice Avenue"
    And the customer's name shall be "Alice Allisson"
    And the customer's phone number shall be "(514) 555-1111"
    And the customer shall have 2 points
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 3
    And there shall be one manager

  Scenario Outline: Update account information successfully
    When "<username>" attempts to change their <field> to "<newValue>"
    Then the system shall not raise any errors
    And a customer shall exist with username "<username>"
    And the customer's password shall be "<password>"
    And the customer's address shall be "<address>"
    And the customer's name shall be "<name>"
    And the customer's phone number shall be "<phone>"
    And the customer shall have <points> points
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 3
    And there shall be one manager

    Examples: 
      | username  | field        | newValue       | password         | name             | phone          | address                | points |
      | alice     | password     | mypassw0rd     | mypassw0rd       | Alice Allisson   | (514) 555-1111 | 123 Alice Avenue       |      2 |
      | obiwan212 | password     | hello_there    | hello_there      | Obi-Wan Kenobi   | (438) 555-1234 | Jedi Temple, Coruscant |    212 |
      | anakin501 | password     | i<3padme       | i<3padme         | Anakin Skywalker | (514) 555-9876 | Jedi Temple, Coruscant |    501 |
      | alice     | name         | Alice Alice    | alice123         | Alice Alice      | (514) 555-1111 | 123 Alice Avenue       |      2 |
      | obiwan212 | name         | Ben Kenobi     | highground       | Ben Kenobi       | (438) 555-1234 | Jedi Temple, Coruscant |    212 |
      | obiwan212 | name         |                | highground       |                  | (438) 555-1234 | Jedi Temple, Coruscant |    212 |
      | anakin501 | name         | Darth Vader    | i-dont-like-sand | Darth Vader      | (514) 555-9876 | Jedi Temple, Coruscant |    501 |
      | anakin501 | name         |                | i-dont-like-sand |                  | (514) 555-9876 | Jedi Temple, Coruscant |    501 |
      | alice     | phone number | (438) 555-0000 | alice123         | Alice Allisson   | (438) 555-0000 | 123 Alice Avenue       |      2 |
      | obiwan212 | phone number | (514) 555-2345 | highground       | Obi-Wan Kenobi   | (514) 555-2345 | Jedi Temple, Coruscant |    212 |
      | obiwan212 | phone number |                | highground       | Obi-Wan Kenobi   |                | Jedi Temple, Coruscant |    212 |
      | anakin501 | phone number | (438) 555-8765 | i-dont-like-sand | Anakin Skywalker | (438) 555-8765 | Jedi Temple, Coruscant |    501 |
      | anakin501 | phone number |                | i-dont-like-sand | Anakin Skywalker |                | Jedi Temple, Coruscant |    501 |
      | alice     | address      | 2 Main Street  | alice123         | Alice Allisson   | (514) 555-1111 | 2 Main Street          |      2 |
      | obiwan212 | address      | Tatooine       | highground       | Obi-Wan Kenobi   | (438) 555-1234 | Tatooine               |    212 |
      | anakin501 | address      | Death Star     | i-dont-like-sand | Anakin Skywalker | (514) 555-9876 | Death Star             |    501 |

  Scenario Outline: Try to update customer with invalid value
    When "<username>" attempts to change their <field> to "<newValue>"
    Then the system shall raise the error "<error>"
    And a customer shall exist with username "<username>"
    And the customer's password shall be "<password>"
    And the customer's address shall be "<address>"
    And the customer's name shall be "<name>"
    And the customer's phone number shall be "<phone>"
    And the customer shall have <points> points
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 3
    And there shall be one manager

    Examples: 
      | username  | field        | newValue   | password         | name             | phone          | address                | points | error                                                            |
      | obiwan212 | password     |            | highground       | Obi-Wan Kenobi   | (438) 555-1234 | Jedi Temple, Coruscant |    212 | password is required                                             |
      | anakin501 | password     |            | i-dont-like-sand | Anakin Skywalker | (514) 555-9876 | Jedi Temple, Coruscant |    501 | password is required                                             |
      | obiwan212 | phone number | not num    | highground       | Obi-Wan Kenobi   | (438) 555-1234 | Jedi Temple, Coruscant |    212 | phone number does not match expected format \\"(xxx) xxx-xxxx\\" |
      | anakin501 | phone number | 5145559876 | i-dont-like-sand | Anakin Skywalker | (514) 555-9876 | Jedi Temple, Coruscant |    501 | phone number does not match expected format \\"(xxx) xxx-xxxx\\" |
      | obiwan212 | address      |            | highground       | Obi-Wan Kenobi   | (438) 555-1234 | Jedi Temple, Coruscant |    212 | address is required                                              |
      | anakin501 | address      |            | i-dont-like-sand | Anakin Skywalker | (514) 555-9876 | Jedi Temple, Coruscant |    501 | address is required                                              |

  Scenario Outline: Try to update non-existent user
    When "<username>" attempts to change their <field> to "<newValue>"
    Then the system shall raise the error "there is no user with username \"<username>\""
    And no user shall exist with username "<username>"
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 3
    And there shall be one manager

    Examples: 
      | username    | field        | newValue       |
      | nonexistent | password     | password       |
      | idontexist  | name         | Fred           |
      | ghost       | phone number | (438) 555-5555 |
      | nonexistent | address      | 1234 Main St.  |

  Scenario Outline: Successfully delete customer who is not an employee
    When "<username>" attempts to delete their customer account
    Then the system shall not raise any errors
    And no user shall exist with username "<username>"
    And the total number of users shall be 5
    And the total number of customers shall be 2
    And the total number of employees shall be 3
    And there shall be one manager

    Examples: 
      | username  |
      | obiwan212 |
      | anakin501 |

  Scenario Outline: Successfully delete customer account for user who is also an employee
    When "alice" attempts to delete their customer account
    Then the system shall not raise any errors
    And no customer shall exist with username "alice"
    But an employee shall exist with username "alice"
    And the employee's password shall be "alice123"
    And the employee's name shall be "Alice Allisson"
    And the employee's phone number shall be "(514) 555-1111"
    And the total number of users shall be 6
    And the total number of customers shall be 2
    And the total number of employees shall be 3
    And there shall be one manager

  Scenario Outline: Try to delete a customer that doesn't exist
    When "<username>" attempts to delete their customer account
    Then the system shall raise the error "<error>"
    And no customer shall exist with username "<username>"
    And the total number of users shall be 6
    And the total number of customers shall be 3
    And the total number of employees shall be 3
    And there shall be one manager

    Examples: 
      | username    | error                                            |
      | nonexistent | there is no user with username \\"nonexistent\\" |
      | ghost       | there is no user with username \\"ghost\\"       |
      | bob         | \\"bob\\" is not a customer                      |
      | claire      | \\"claire\\" is not a customer                   |
