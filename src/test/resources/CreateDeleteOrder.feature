Feature: Create and delete orders
  As a customer, I want to create orders so that I can buy items.
  As a customer, I want to delete orders if I no longer intend to make the purchase.

  Background: 
    Given the following employees exist in the system
      | username | password | name           | phone          |
      | alice    | alice123 | Alice Allisson | (514) 555-1111 |
      | bob      | password | Bob Robertson  | (514) 555-2222 |
    And the following customers exist in the system
      | username  | password         | name             | phone          | address                | points |
      | obiwan212 | highground       | Obi-Wan Kenobi   | (438) 555-1234 | Jedi Temple, Coruscant |    212 |
      | anakin501 | i-dont-like-sand | Anakin Skywalker | (514) 555-9876 | Jedi Temple, Coruscant |    501 |
      | alice     | ---              | ---              | ---            | 123 Alice Avenue       |      2 |
    And the following items exist in the system
      | name                | price | perishableOrNot | quantity | points |
      | Eggs                |   549 | perishable      |       20 |      5 |
      | Chicken noodle soup |   179 | non-perishable  |        0 |      2 |
    And the following orders exist in the system
      # There's no way to set the autounique order number, so refer to orders here using a separate ID.
      # The controller should still identify orders by their order number.
      # You'll need to create a map from string IDs to order numbers.
      # Also, please convert the string "NULL" to null (here and in the tests below).
      | id | datePlaced | deadline    | customer  | assignee | state              |
      | a  | NULL       | SameDay     | alice     | NULL     | pending            |
      | b  | 2025-02-24 | InOneDay    | obiwan212 | NULL     | placed             |
      | c  | NULL       | InTwoDays   | anakin501 | NULL     | under construction |
      | d  | 2025-02-24 | InThreeDays | alice     | bob      | delivered          |
    And the following items are part of orders
      | order | item                | quantity |
      | a     | Eggs                |        2 |
      | b     | Eggs                |        1 |
      | b     | Chicken noodle soup |        3 |
      | d     | Chicken noodle soup |        5 |

  Scenario Outline: Successfully create an order
    When "<user>" attempts to create an order with deadline "<deadline>"
    Then the system shall not raise any errors
    And "<user>" shall have a new order
    And the newly-created order shall have deadline "<deadline>"
    And the newly-created order shall have 0 items
    And the newly-created order shall not have been placed
    And the total number of orders shall be 5

    Examples: 
      | user      | deadline    |
      | obiwan212 | SameDay     |
      | obiwan212 | InOneDay    |
      | anakin501 | InTwoDays   |
      | alice     | InThreeDays |

  Scenario Outline: Try to create an invalid order
    When "<user>" attempts to create an order with deadline "<deadline>"
    Then the system shall raise the error "<error>"
    And the total number of orders shall be 4

    Examples: 
      | user        | deadline | error                                            |
      | NULL        | SameDay  | customer is required                             |
      | nonexistent | SameDay  | there is no user with username \\"nonexistent\\" |
      | ghost       | SameDay  | there is no user with username \\"ghost\\"       |
      | bob         | SameDay  | \\"bob\\" is not a customer                      |
      | alice       | NULL     | delivery deadline is required                    |

  Scenario Outline: Successfully delete an order
    When the user attempts to delete the order with ID "<id>"
    Then the system shall not raise any errors
    And no order shall exist with ID "<id>"
    And the total number of orders shall be 3

    Examples: 
      | id |
      | a  |
      | c  |

  Scenario Outline: Try to delete an order that doesn't exist
    When the user attempts to delete the non-existent order with order number <orderNumber>
    Then the system shall raise the error "there is no order with number \"<orderNumber>\""
    And no order shall exist with order number <orderNumber>
    And the total number of orders shall be 4

    Examples: 
      | orderNumber |
      |     9999999 |
      |   123456789 |

  Scenario Outline: Try to delete an order that's already been placed
    When the user attempts to delete the order with ID "<id>"
    Then the system shall raise the error "cannot delete an order which has already been placed"
    And an order shall exist with ID "<id>"
    And the total number of orders shall be 4

    Examples: 
      | id |
      | b  |
      | d  |
