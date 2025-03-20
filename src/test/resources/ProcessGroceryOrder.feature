Feature: Process order
  As a customer, I want to place orders so that I can buy items.
  As a manager, I want to assign orders to employees so that the orders can be fulfilled.
  As an employee, I want to mark orders as completed once I have finished assembling them.

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
      | id    | datePlaced | deadline    | customer  | assignee | state              |
      | a     | NULL       | SameDay     | alice     | NULL     | under construction |
      | b1    | NULL       | InOneDay    | obiwan212 | NULL     | under construction |
      | b2    | NULL       | InOneDay    | obiwan212 | NULL     | under construction |
      | b3    | NULL       | InOneDay    | obiwan212 | NULL     | under construction |
      | b9    | NULL       | InOneDay    | obiwan212 | NULL     | under construction |
      | b10   | NULL       | InOneDay    | obiwan212 | NULL     | under construction |
      | b11   | NULL       | InOneDay    | obiwan212 | NULL     | under construction |
      | b12   | NULL       | InOneDay    | obiwan212 | NULL     | pending            |
      | empty | NULL       | InTwoDays   | anakin501 | NULL     | under construction |
      | d     | NULL       | InThreeDays | alice     | NULL     | under construction |
      | e     | NULL       | SameDay     | alice     | NULL     | pending            |
      | f     | today      | InOneDay    | obiwan212 | NULL     | placed             |
      | g     | yesterday  | InTwoDays   | anakin501 | bob      | in preparation     |
      | g1    | yesterday  | InOneDay    | anakin501 | bob      | in preparation     |
      | g2    | today      | InThreeDays | alice     | bob      | in preparation     |
      | g3    | today      | InThreeDays | alice     | bob      | in preparation     |
      | g4    | today      | SameDay     | alice     | bob      | in preparation     |
      | h     | yesterday  | InOneDay    | anakin501 | claire   | ready for delivery |
      | i     | yesterday  | SameDay     | alice     | alice    | delivered          |
      | j     | today      | InOneDay    | alice     | bob      | cancelled          |
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
      | b12   | Banana              |       12 |
      | b12   | Eggs                |        1 |
      | d     | Eggs                |        1 |
      | d     | Chicken noodle soup |        3 |
      | d     | Banana              |        1 |
      | e     | Grain of rice       |        1 |
      | f     | Banana              |        3 |
      | g     | Eggs                |        1 |
      | g     | Chicken noodle soup |        3 |
      | g1    | Eggs                |        1 |
      | g1    | Chicken noodle soup |        1 |
      | g2    | Eggs                |        1 |
      | g3    | Chicken noodle soup |        1 |
      | g3    | Chicken noodle soup |        2 |
      | g4    | Chicken noodle soup |        1 |
      | h     | Chicken noodle soup |        2 |
      | i     | Eggs                |        3 |
      | j     | Eggs                |        3 |

  Scenario Outline: Successfully check out
    When the user attempts to check out the order with ID "<id>"
    Then the system shall not raise any errors
    And the total cost of the order shall be <cost> cents
    And the order shall be "pending"
    And the order's assignee shall be "NULL"

    Examples: 
      | id  | cost |
      #  Eggs: $5.49
      #  Soup: $1.79
      # Total: $7.28
      | a   |  728 |
      | b1  |  100 |
      # (2 bananas)($0.95/banana) = $1.90
      | b2  |  190 |
      # (3 bananas)($0.90/banana) = $2.70
      | b3  |  270 |
      # (9 bananas)($0.60/banana) = $5.40
      | b9  |  540 |
      # (10 bananas)($0.55/banana) = $5.50
      | b10 |  550 |
      # (11 bananas)($0.55/banana) = $6.05
      | b11 |  605 |
      #   Eggs: $5.49
      #   Soup: (3 cans)(90%)($1.79/can) = $4.833
      # Banana: $1.00
      #  Total: $11.323 --> $11.32
      | d   | 1132 |

  Scenario Outline: Unsuccessfully check out
    When the user attempts to check out the order with ID "<id>"
    Then the system shall raise the error "<error>"
    And the order shall be "<state>"
    And the order's assignee shall be "<assignee>"

    Examples: 
      | id    | state              | assignee | error                                        |
      | empty | under construction | NULL     | cannot check out an empty order              |
      | b11   | under construction | NULL     | insufficient inventory for item \\"Banana\\" |
      | e     | pending            | NULL     | order has already been checked out           |
      | f     | placed             | NULL     | order has already been checked out           |
      | g     | in preparation     | bob      | order has already been checked out           |
      | h     | ready for delivery | claire   | order has already been checked out           |
      | i     | delivered          | alice    | order has already been checked out           |
      | j     | cancelled          | bob      | order has already been checked out           |

  Scenario Outline: Successfully pay for order
    When the user attempts to pay for the order with ID "<orderId>" <usingOrNotUsing> their points
    Then the system shall not raise any errors
    And the final cost of the order, after considering points, shall be <cost> cents
    And the order shall be "placed"
    And the order's date placed shall be today
    And the order's assignee shall be "NULL"
    And "<username>" shall have <points> points

    # TODO: How are points calculated?
    # TODO: Watch out for id vs orderId! Check column names
    Examples: 
      | orderId | usingOrNotUsing | cost | username | points |
      # Rice: (1 grain)($0.01/grain) = $0.01
      # Can use one point to bring order down to $0
      | e       | without using   |    1 | alice    |        |
      | e       | using           |    0 | alice    |        |

  Scenario Outline: Successfully check out and pay for order
    When the user attempts to check out the order with ID "<orderId>"
    And the user attempts to pay for the order with ID "<orderId>" <usingOrNotUsing> their points
    And the final cost of the order, after considering points, shall be <cost> cents
    And the order shall be "placed"
    And the order's assignee shall be "NULL"
    And the order's date placed shall be today
    And "<username>" shall have <points> points

    # See above for costs before considering points
    Examples: 
      | orderId | usingOrNotUsing | cost | username  | points |
      | a       | without using   |  728 | alice     |        |
      | a       | using           |  726 | alice     |        |
      | b1      | without using   |  100 | obiwan212 |        |
      | b1      | using           |    0 | obiwan212 |        |
      | b2      | without using   |  190 | obiwan212 |        |
      | b2      | using           |    0 | obiwan212 |        |
      | b3      | without using   |  270 | obiwan212 |        |
      | b3      | using           |   58 | obiwan212 |        |
      | b9      | without using   |  540 | obiwan212 |        |
      | b9      | using           |  328 | obiwan212 |        |
      | b10     | without using   |  550 | obiwan212 |        |
      | b10     | using           |  338 | obiwan212 |        |
      | b11     | without using   |  605 | obiwan212 |        |
      | b11     | using           |  605 | obiwan212 |        |
      | d       | without using   | 1132 | alice     |        |
      | d       | using           | 1130 | alice     |        |

  Scenario Outline: Unsuccessfully pay for order
    When the user attempts to pay for the order with ID "<orderId>" <usingOrNotUsing> their points
    Then the system shall raise the error "<error>"
    And the order shall be "<state>"
    And the order's assignee shall be "<assignee>"
    And "<customer>" shall have <points> points

    Examples: 
      | orderId | usingOrNotUsing | state              | customer  | points | assignee | error                                                  |
      | a       | using           | under construction | alice     |      2 | NULL     | cannot pay for an order which has not been checked out |
      | a       | without using   | under construction | alice     |      2 | NULL     | cannot pay for an order which has not been checked out |
      | f       | using           | placed             | obiwan212 |    212 | NULL     | cannot pay for order that has already been paid for    |
      | f       | without using   | placed             | obiwan212 |    212 | NULL     | cannot pay for order that has already been paid for    |
      | g       | using           | in preparation     | anakin501 |    501 | bob      | cannot pay for order that has already been paid for    |
      | g       | without using   | in preparation     | anakin501 |    501 | bob      | cannot pay for order that has already been paid for    |
      | h       | using           | ready for delivery | anakin501 |    501 | claire   | cannot pay for order that has already been paid for    |
      | h       | without using   | ready for delivery | anakin501 |    501 | claire   | cannot pay for order that has already been paid for    |
      | i       | using           | delivered          | alice     |      2 | alice    | cannot pay for order that has already been paid for    |
      | i       | without using   | delivered          | alice     |      2 | alice    | cannot pay for order that has already been paid for    |
      | j       | using           | cancelled          | alice     |      2 | bob      | cannot pay for order that has already been paid for    |
      | j       | without using   | cancelled          | alice     |      2 | bob      | cannot pay for order that has already been paid for    |

  Scenario Outline: Successfully assign order to employee
    When the manager attempts to assign the order with ID "<orderId>" to "<employee>"
    Then the system shall not raise any errors
    And the order shall be "in preparation"
    And the order's assignee shall be "<employee>"

    Examples: 
      | orderId | employee |
      | f       | alice    |
      | f       | bob      |
      | f       | claire   |
      # Change assignee
      | g       | alice    |
      | g       | bob      |
      | g       | claire   |

  Scenario Outline: Unsuccessfully assign order to employee
    When the manager attempts to assign the order with ID "<orderId>" to "<newAssignee>"
    Then the system shall raise the error "<error>"
    And the order shall be "<oldState>"
    And the order's assignee shall be "<oldAssignee>"

    Examples: 
      | orderId | newAssignee | oldAssignee | oldState           | error                                                           |
      | a       | alice       | NULL        | under construction | cannot assign employee to order that has not been placed        |
      | b1      | bob         | NULL        | under construction | cannot assign employee to order that has not been placed        |
      | e       | claire      | NULL        | pending            | cannot assign employee to order that has not been placed        |
      | h       | alice       | claire      | ready for delivery | cannot assign employee to an order that has already been placed |
      | i       | bob         | alice       | delivered          | cannot assign employee to an order that has already been placed |
      | j       | claire      | bob         | cancelled          | cannot assign employee to an order that has been cancelled      |
      | f       | nonexistent | NULL        | placed             | there is no user with username \\"nonexistent\\"                |
      | f       | ghost       | NULL        | placed             | there is no user with username \\"ghost\\"                      |
      | f       | obiwan212   | NULL        | placed             | \\"obiwan212\\" is not an employee                              |
      | f       | anakin501   | NULL        | placed             | \\"anakin501\\" is not an employee                              |

  Scenario Outline: Successfully finish order assembly
    When the user attempts to indicate that assembly of the order with ID "<orderId>" is finished
    Then the system shall not raise any errors
    And the order shall be "ready for delivery"

    Examples: 
      | orderId |
      | g1      |
      | g3      |
      | g4      |

  Scenario Outline: Unsuccessfully finish order assembly
    When the user attempts to indicate that assembly of the order with ID "<orderId>" is finished
    Then the system shall raise the error "<error>"
    And the order shall be "<oldState>"

    Examples: 
      | orderId | oldState           | error                                                                                             |
      | g       | in preparation     | cannot finish assembling order because it contains perishable items and today is not the deadline |
      | g2      | in preparation     | cannot finish assembling order because it contains perishable items and today is not the deadline |
      | a       | under construction | cannot finish assembling order because it has not been assigned to an employee                    |
      | b2      | under construction | cannot finish assembling order because it has not been assigned to an employee                    |
      | b9      | under construction | cannot finish assembling order because it has not been assigned to an employee                    |
      | e       | pending            | cannot finish assembling order because it has not been assigned to an employee                    |
      | f       | placed             | cannot finish assembling order because it has not been assigned to an employee                    |
      | h       | ready for delivery | cannot finish assembling order that has already been assembled                                    |
      | i       | delivered          | cannot finish assembling order that has already been assembled                                    |
      | j       | cancelled          | cannot finish assembling order because it was cancelled                                           |
