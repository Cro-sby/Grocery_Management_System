Feature: Process order
  As a customer, I want to place orders so that I can buy items.
  As a manager, I want to assign orders to employees so that the orders can be fulfilled.
  As an employee, I want to mark orders as completed once I have finished assembling them.

  Background: 
    Given the following customers exist in the system
      | username  | password         | name             | phone          | address                | points |
      | obiwan212 | highground       | Obi-Wan Kenobi   | (438) 555-1234 | Jedi Temple, Coruscant |    212 |
      | anakin501 | i-dont-like-sand | Anakin Skywalker | (514) 555-9876 | Jedi Temple, Coruscant |    501 |
      | alice     | alice123         | Alice Allisson   | (514) 555-1111 | 123 Alice Avenue       |      2 |
    And the following items exist in the system
      | name                | price | perishableOrNot | quantity | points |
      | Eggs                |   549 | perishable      |       20 |      5 |
      | Chicken noodle soup |   179 | non-perishable  |        0 |      2 |
      | Banana              |   100 | perishable      |       10 |      1 |
      | Grain of rice       |     1 | perishable      |      100 |      1 |
    And the following orders exist in the system
      # There's no way to set the autounique order number, so refer to orders here using a separate ID.
      # The controller should still identify orders by their order number.
      # You'll need to create a map from string IDs to order numbers.
      # Also, please convert the string "NULL" to null, "today" to the current date, "yesterday" to yesterday's date, etc.
      | id    | datePlaced | deadline    | customer  | state              |
      | a     | NULL       | SameDay     | alice     | under construction |
      | b1    | NULL       | InOneDay    | obiwan212 | under construction |
      | b2    | NULL       | InOneDay    | obiwan212 | under construction |
      | b3    | NULL       | InOneDay    | obiwan212 | under construction |
      | b9    | NULL       | InOneDay    | obiwan212 | under construction |
      | b10   | NULL       | InOneDay    | obiwan212 | under construction |
      | b11   | NULL       | InOneDay    | obiwan212 | under construction |
      | empty | NULL       | InTwoDays   | anakin501 | under construction |
      | d     | NULL       | InThreeDays | alice     | under construction |
      | e     | NULL       | SameDay     | alice     | pending            |
      | f     | today      | InOneDay    | obiwan212 | placed             |
      | g     | yesterday  | InTwoDays   | anakin501 | in preparation     |
      | h     | yesterday  | InOneDay    | anakin501 | ready for delivery |
      | i     | yesterday  | SameDay     | alice     | delivered          |
      | j     | today      | InOneDay    | alice     | cancelled          |
    And the following items are part of orders
      | order | item                | quantity |
      | a     | Eggs                |        1 |
      | a     | Chicken noodle soup |        1 |
      | b1    | Banana              |        1 |
      | b2    | Banana              |        2 |
      | b3    | Banana              |        3 |
      | b9    | Banana              |        9 |
      | b10   | Banana              |       10 |
      | b11   | Banana              |       11 |
      | d     | Eggs                |        1 |
      | d     | Chicken noodle soup |        3 |
      | d     | Banana              |        1 |
      | e     | Grain of rice       |        1 |
      | f     | Banana              |        3 |
      | g     | Eggs                |        1 |
      | h     | Chicken noodle soup |        2 |
      | i     | Eggs                |        3 |
      | j     | Eggs                |        3 |

  Scenario Outline: Successfully check out
    When the user attempts to check out the order with ID "<id>"
    Then the system shall not raise any errors
    And the total cost of the order shall be <cost> cents
    And the order shall be "pending"

    Examples: 
      | id    | cost |
      #  Eggs: $5.49
      #  Soup: $1.79
      # Total: $7.28
      | a     |  728 |
      | b1    |  100 |
      # (2 bananas)($0.95/banana) = $1.90
      | b2    |  190 |
      # (3 bananas)($0.90/banana) = $2.70
      | b3    |  270 |
      # (9 bananas)($0.60/banana) = $5.40
      | b9    |  540 |
      # (10 bananas)($0.55/banana) = $5.50
      | b10   |  550 |
      # (11 bananas)($0.55/banana) = $6.05
      | b11   |  605 |
      | empty |    0 |
      #   Eggs: $5.49
      #   Soup: (3 cans)(90%)($1.79/can) = $4.833
      # Banana: $1.00
      #  Total: $11.323 --> $11.32
      | d     | 1132 |

  Scenario Outline: Unsuccessfully check out
    When the user attempts to check out the order with ID "<id>"
    Then the system shall raise the error "<error>"
    And the order shall be "<state>"

    Examples: 
      | id    | state              | error                                        |
      | empty | under construction | cannot check out an empty order              |
      | b11   | under construction | insufficient inventory for item \\"Banana\\" |
      | e     | pending            | order has already been checked out           |
      | f     | placed             | order has already been checked out           |
      | g     | in preparation     | order has already been checked out           |
      | h     | ready for delivery | order has already been checked out           |
      | i     | delivered          | order has already been checked out           |
      | j     | cancelled          | order has already been checked out           |
