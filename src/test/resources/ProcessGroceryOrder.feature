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
      | Chicken noodle soup |   179 | non-perishable  |        2 |      2 |
      | Banana              |   100 | perishable      |        8 |      1 |
      | Grain of rice       |     1 | perishable      |      100 |      1 |

  Scenario Outline: Successfully check out a bulk order of bananas
    Given the following orders exist in the system
      | id   | datePlaced | deadline   | customer   | assignee | state              |
      | <id> | NULL       | <deadline> | <customer> | NULL     | under construction |
    And the following items are part of orders
      | order | item   | quantity     |
      | <id>  | Banana | <numBananas> |
    When the user attempts to check out the order with ID "<id>"
    Then the system shall not raise any errors
    And the total cost of the order shall be <cost> cents
    And the order shall be "pending"
    And the order's placer shall be "<customer>"
    # No change yet in points, inventory, assignee, etc.
    And the order's assignee shall be "NULL"
    And "<customer>" shall have <points> points
    And the quantity of item "Banana" shall be 8

    Examples: 
      | id | deadline    | customer  | points | numBananas | cost |
      | a  | InOneDay    | alice     |      2 |          1 |  100 |
      # (2 bananas)($0.95/banana) = $1.90
      | b  | InOneDay    | anakin501 |    501 |          2 |  190 |
      # (3 bananas)($0.90/banana) = $2.70
      | c  | InTwoDays   | obiwan212 |    212 |          3 |  270 |
      # (8 bananas)($0.65/banana) = $5.20
      | d  | InThreeDays | obiwan212 |    212 |          8 |  520 |
      # (9 bananas)($0.60/banana) = $5.40
      | e  | InTwoDays   | obiwan212 |    212 |          9 |  540 |
      # (10 bananas)($0.55/banana) = $5.50
      | f  | InOneDay    | obiwan212 |    212 |         10 |  550 |

  Scenario Outline: Successfully check out an order with multiple items
    Given the following orders exist in the system
      | id | datePlaced | deadline   | customer | assignee | state              |
      | g  | NULL       | <deadline> | alice    | NULL     | under construction |
    And the following items are part of orders
      | order | item                | quantity         |
      | g     | Eggs                | <qtyEggsInOrder> |
      | g     | Chicken noodle soup | <qtySoupInOrder> |
    When the user attempts to check out the order with ID "g"
    Then the system shall not raise any errors
    And the total cost of the order shall be <cost> cents
    And the order shall be "pending"
    And the order's placer shall be "alice"
    # No change yet in points, inventory, assignee, etc.
    And the order's assignee shall be "NULL"
    And "alice" shall have 2 points
    And the quantity of item "Eggs" shall be 20
    And the quantity of item "Chicken noodle soup" shall be 2

    Examples: 
      | deadline    | qtyEggsInOrder | qtySoupInOrder | cost |
      | InOneDay    |              1 |              1 |  728 |
      #  Eggs: (5 units)(0.8)($5.49/unit) = $21.96
      #  Soup: (3 units)(0.9)($1.79/unit) = $4.833
      # Total: $26.793 --> $26.79
      | InTwoDays   |              5 |              3 | 2679 |
      | InThreeDays |              5 |              3 | 2679 |
      # Extra fee for same-day delivery
      | SameDay     |              1 |              1 | 1228 |
      | SameDay     |              5 |              3 | 3179 |

  Scenario: Try to check out an empty order
    Given the following orders exist in the system
      | id    | datePlaced | deadline  | customer | assignee | state              |
      | empty | NULL       | InTwoDays | alice    | NULL     | under construction |
    When the user attempts to check out the order with ID "empty"
    Then the system shall raise the error "cannot check out an empty order"
    And the order shall be "under construction"
    And the order's assignee shall be "NULL"

  Scenario Outline: Try to check out an order in the wrong state
    Given the following orders exist in the system
      | id       | datePlaced   | deadline  | customer  | assignee   | state   |
      | badstate | <datePlaced> | InTwoDays | anakin501 | <assignee> | <state> |
    And the following items are part of orders
      | order    | item                | quantity |
      | badstate | Eggs                |        5 |
      | badstate | Chicken noodle soup |        2 |
    When the user attempts to check out the order with ID "badstate"
    Then the system shall raise the error "order has already been checked out"
    And the order shall be "<state>"
    And the order's assignee shall be "<assignee>"
    And "anakin501" shall have 501 points
    And the quantity of item "Eggs" shall be 20
    And the quantity of item "Chicken noodle soup" shall be 2

    Examples: 
      | state              | datePlaced | assignee |
      | pending            | NULL       | NULL     |
      | placed             | today      | NULL     |
      | in preparation     | yesterday  | bob      |
      | ready for delivery | yesterday  | claire   |
      | delivered          | yesterday  | alice    |
      | cancelled          | yesterday  | bob      |

  Scenario Outline: Successfully pay for an order
    Given the following orders exist in the system
      | id      | datePlaced | deadline   | customer   | assignee | state   |
      | eggsoup | NULL       | <deadline> | <customer> | NULL     | pending |
      | bananas | NULL       | <deadline> | <customer> | NULL     | pending |
      | smol    | NULL       | <deadline> | <customer> | NULL     | pending |
    And the following items are part of orders
      | order   | item                | quantity |
      | eggsoup | Eggs                |        1 |
      | eggsoup | Chicken noodle soup |        1 |
      | bananas | Banana              |        8 |
      | smol    | Grain of rice       |        1 |
    When the user attempts to pay for the order with ID "<orderId>" "<usingOrWithoutUsing>" their points
    Then the system shall not raise any errors
    And the final cost of the order, after considering points, shall be <cost> cents
    And the order shall be "placed"
    And the order's date placed shall be today
    And the order's assignee shall be "NULL"
    And "<customer>" shall have <points> points
    And the quantity of item "Eggs" shall be <newQtyEggs>
    And the quantity of item "Chicken noodle soup" shall be <newQtySoup>
    And the quantity of item "Banana" shall be <newQtyBananas>
    And the quantity of item "Grain of rice" shall be <newQtyRice>

    Examples: 
      | orderId | deadline    | customer  | usingOrWithoutUsing | cost | points | newQtyEggs | newQtySoup | newQtyBananas | newQtyRice |
      # 1 carton of eggs + 1 can of soup = 7 points
      | eggsoup | InOneDay    | alice     | without using       |  728 |      9 |         19 |          1 |             8 |        100 |
      | eggsoup | InTwoDays   | alice     | using               |  726 |      7 |         19 |          1 |             8 |        100 |
      | eggsoup | InThreeDays | anakin501 | without using       |  728 |    508 |         19 |          1 |             8 |        100 |
      | eggsoup | InOneDay    | anakin501 | using               |  227 |      7 |         19 |          1 |             8 |        100 |
      # 8 bananas = 8 points
      | bananas | InTwoDays   | obiwan212 | without using       |  520 |    220 |         20 |          2 |             0 |        100 |
      | bananas | InThreeDays | obiwan212 | using               |  308 |      8 |         20 |          2 |             0 |        100 |
      # 1 grain of rice = 1 point
      | smol    | InOneDay    | alice     | without using       |    1 |      3 |         20 |          2 |             8 |         99 |
      | smol    | InTwoDays   | alice     | using               |    0 |      2 |         20 |          2 |             8 |         99 |
      # Extra fee for same-day delivery
      | eggsoup | SameDay     | alice     | without using       | 1228 |      9 |         19 |          1 |             8 |        100 |
      | eggsoup | SameDay     | alice     | using               | 1226 |      7 |         19 |          1 |             8 |        100 |
      | bananas | SameDay     | obiwan212 | without using       | 1020 |    220 |         20 |          2 |             0 |        100 |
      | bananas | SameDay     | obiwan212 | using               |  808 |      8 |         20 |          2 |             0 |        100 |
      | smol    | SameDay     | alice     | without using       |  501 |      3 |         20 |          2 |             8 |         99 |
      | smol    | SameDay     | alice     | using               |  499 |      1 |         20 |          2 |             8 |         99 |
      | smol    | SameDay     | anakin501 | without using       |  501 |    502 |         20 |          2 |             8 |         99 |
      | smol    | SameDay     | anakin501 | using               |    0 |      1 |         20 |          2 |             8 |         99 |

  Scenario Outline: Unsuccessfully pay for order due to insufficient stock
    Given the following orders exist in the system
      | id      | datePlaced | deadline    | customer  | assignee | state   |
      | eggsoup | NULL       | InThreeDays | obiwan212 | NULL     | pending |
      | bananas | NULL       | SameDay     | obiwan212 | NULL     | pending |
    And the following items are part of orders
      | order   | item                | quantity |
      | eggsoup | Eggs                |        1 |
      | eggsoup | Chicken noodle soup |        5 |
      | bananas | Banana              |        9 |
    When the user attempts to pay for the order with ID "<orderId>" "<usingOrWithoutUsing>" their points
    Then the system shall raise the error "<error>"
    And the order shall be "pending"
    And the order's assignee shall be "NULL"
    And the customer shall have 212 points
    And the quantity of item "Eggs" shall be 20
    And the quantity of item "Chicken noodle soup" shall be 2
    And the quantity of item "Banana" shall be 8
    And the quantity of item "Grain of rice" shall be 100

    Examples: 
      | orderId | usingOrWithoutUsing | error                                                |
      | eggsoup | using               | insufficient stock of item \\"Chicken noodle soup\\" |
      | eggsoup | without using       | insufficient stock of item \\"Chicken noodle soup\\" |
      | bananas | using               | insufficient stock of item \\"Banana\\"              |
      | bananas | without using       | insufficient stock of item \\"Banana\\"              |

  Scenario Outline: Unsuccessfully pay for order due to wrong state
    Given the following orders exist in the system
      | id       | datePlaced | deadline    | customer | assignee   | state   |
      | badstate | NULL       | InThreeDays | alice    | <assignee> | <state> |
    And the following items are part of orders
      | order    | item | quantity |
      | badstate | Eggs |        1 |
    When the user attempts to pay for the order with ID "badstate" "<usingOrWithoutUsing>" their points
    Then the system shall raise the error "<error>"
    And the order shall be "<state>"
    And the order's assignee shall be "<assignee>"
    And "alice" shall have 2 points
    And the quantity of item "Eggs" shall be 20
    And the quantity of item "Chicken noodle soup" shall be 2
    And the quantity of item "Banana" shall be 8
    And the quantity of item "Grain of rice" shall be 100

    Examples: 
      | state              | assignee | usingOrWithoutUsing | error                                                   |
      | under construction | NULL     | without using       | cannot pay for an order which has not been checked out  |
      | under construction | NULL     | using               | cannot pay for an order which has not been checked out  |
      | placed             | NULL     | without using       | cannot pay for an order which has already been paid for |
      | placed             | NULL     | using               | cannot pay for an order which has already been paid for |
      | in preparation     | bob      | without using       | cannot pay for an order which has already been paid for |
      | in preparation     | bob      | using               | cannot pay for an order which has already been paid for |
      | ready for delivery | claire   | without using       | cannot pay for an order which has already been paid for |
      | ready for delivery | claire   | using               | cannot pay for an order which has already been paid for |
      | delivered          | alice    | without using       | cannot pay for an order which has already been paid for |
      | delivered          | alice    | using               | cannot pay for an order which has already been paid for |
      | cancelled          | alice    | without using       | cannot pay for an order which has been cancelled        |
      | cancelled          | alice    | using               | cannot pay for an order which has been cancelled        |

  Scenario Outline: Successfully assign order to employee
    Given the following orders exist in the system
      | id         | datePlaced | deadline    | customer  | assignee | state          |
      | unassigned | today      | InThreeDays | alice     | NULL     | placed         |
      | assigned   | yesterday  | InTwoDays   | anakin501 | bob      | in preparation |
    And the following items are part of orders
      | order      | item                | quantity |
      | unassigned | Eggs                |        1 |
      | assigned   | Chicken noodle soup |        1 |
    When the manager attempts to assign the order with ID "<orderId>" to "<employee>"
    Then the system shall not raise any errors
    And the order shall be "in preparation"
    And the order's assignee shall be "<employee>"

    Examples: 
      | orderId    | employee |
      | unassigned | alice    |
      | unassigned | bob      |
      | unassigned | claire   |
      # Change assignee of already-assigned order.
      | assigned   | alice    |
      | assigned   | claire   |

  Scenario Outline: Unsuccessfully assign order to employee
    Given the following orders exist in the system
      | id | datePlaced | deadline    | customer | assignee      | state      |
      | m  | today      | InThreeDays | alice    | <oldAssignee> | <oldState> |
    And the following items are part of orders
      | order | item | quantity |
      | m     | Eggs |        1 |
    When the manager attempts to assign the order with ID "m" to "<newAssignee>"
    Then the system shall raise the error "<error>"
    And the order shall be "<oldState>"
    And the order's assignee shall be "<oldAssignee>"

    Examples: 
      | newAssignee | oldAssignee | oldState           | error                                                             |
      | alice       | NULL        | under construction | cannot assign employee to order that has not been placed          |
      | bob         | NULL        | under construction | cannot assign employee to order that has not been placed          |
      | claire      | NULL        | pending            | cannot assign employee to order that has not been placed          |
      | alice       | claire      | ready for delivery | cannot assign employee to an order that has already been prepared |
      | bob         | alice       | delivered          | cannot assign employee to an order that has already been prepared |
      | claire      | bob         | cancelled          | cannot assign employee to an order that has been cancelled        |
      | nonexistent | NULL        | placed             | there is no user with username \\"nonexistent\\"                  |
      | ghost       | NULL        | placed             | there is no user with username \\"ghost\\"                        |
      | obiwan212   | NULL        | placed             | \\"obiwan212\\" is not an employee                                |
      | anakin501   | NULL        | placed             | \\"anakin501\\" is not an employee                                |

  Scenario Outline: Successfully finish order assembly
    Given the following orders exist in the system
      | id                 | datePlaced   | deadline   | customer  | assignee | state          |
      | with_perishable    | <datePlaced> | <deadline> | obiwan212 | alice    | in preparation |
      | without_perishable | <datePlaced> | <deadline> | anakin501 | bob      | in preparation |
    And the following items are part of orders
      | order              | item                | quantity |
      | with_perishable    | Chicken noodle soup |        1 |
      | with_perishable    | Eggs                |        1 |
      | without_perishable | Chicken noodle soup |        1 |
    When the user attempts to indicate that assembly of the order with ID "<orderId>" is finished
    Then the system shall not raise any errors
    And the order shall be "ready for delivery"

    Examples: 
      | orderId            | datePlaced | deadline    |
      # Can always assemble order on the day it is due
      | with_perishable    | yesterday  | InOneDay    |
      | with_perishable    | today      | SameDay     |
      | without_perishable | yesterday  | InOneDay    |
      | without_perishable | today      | SameDay     |
      # No perishable items: can assemble early
      | without_perishable | yesterday  | InTwoDays   |
      | without_perishable | yesterday  | InThreeDays |
      | without_perishable | today      | InOneDay    |
      | without_perishable | today      | InTwoDays   |
      | without_perishable | today      | InThreeDays |
      # Better late than never
      | with_perishable    | yesterday  | SameDay     |
      | without_perishable | yesterday  | SameDay     |

  Scenario Outline: Try to assemble perishable order before deadline
    Given the following orders exist in the system
      | id         | datePlaced   | deadline   | customer  | assignee | state          |
      | perishable | <datePlaced> | <deadline> | obiwan212 | alice    | in preparation |
    And the following items are part of orders
      | order      | item                | quantity |
      | perishable | Chicken noodle soup |        2 |
      | perishable | Eggs                |        1 |
    When the user attempts to indicate that assembly of the order with ID "perishable" is finished
    Then the system shall raise the error "cannot finish assembling an order with perishable items before the deadline"
    And the order shall be "in preparation"

    Examples: 
      | datePlaced | deadline    |
      | yesterday  | InTwoDays   |
      | yesterday  | InThreeDays |
      | today      | InOneDay    |
      | today      | InTwoDays   |
      | today      | InThreeDays |

  Scenario Outline: Unsuccessfully finish order assembly
    Given the following orders exist in the system
      | id        | datePlaced | deadline | customer | assignee      | state      |
      | bad_state | yesterday  | InOneDay | alice    | <oldAssignee> | <oldState> |
    And the following items are part of orders
      | order     | item                | quantity |
      | bad_state | Chicken noodle soup |        1 |
    When the user attempts to indicate that assembly of the order with ID "bad_state" is finished
    Then the system shall raise the error "<error>"
    And the order shall be "<oldState>"

    Examples: 
      | oldState           | oldAssignee | error                                                                          |
      | under construction | NULL        | cannot finish assembling order because it has not been assigned to an employee |
      | pending            | NULL        | cannot finish assembling order because it has not been assigned to an employee |
      | placed             | NULL        | cannot finish assembling order because it has not been assigned to an employee |
      | ready for delivery | bob         | cannot finish assembling order that has already been assembled                 |
      | delivered          | bob         | cannot finish assembling order that has already been assembled                 |
      | cancelled          | bob         | cannot finish assembling order because it was cancelled                        |

  Scenario Outline: Successfully cancel order
    Given the following orders exist in the system
      | id   | datePlaced | deadline | customer   | assignee | state      |
      | all  | yesterday  | InOneDay | <customer> | NULL     | <oldState> |
      | some | today      | SameDay  | <customer> | NULL     | <oldState> |
    And the following items are part of orders
      | order | item                | quantity |
      | all   | Eggs                |        1 |
      | all   | Chicken noodle soup |        2 |
      | all   | Banana              |        3 |
      | all   | Grain of rice       |        4 |
      | some  | Chicken noodle soup |       10 |
      | some  | Banana              |        9 |
    When the user attempts to cancel the order with ID "<orderId>"
    Then the system shall not raise any errors
    And the order shall be "cancelled"
    # Need to add back inventory that was "reserved" for this order (if it was already placed)
    And the quantity of item "Eggs" shall be <newEggsQty>
    And the quantity of item "Chicken noodle soup" shall be <newSoupQty>
    And the quantity of item "Banana" shall be <newBananaQty>
    And the quantity of item "Grain of rice" shall be <newRiceQty>
    # No change to the customer's points
    And "<customer>" shall have <points> points

    Examples: 
      | orderId | oldState           | newEggsQty | newSoupQty | newBananaQty | newRiceQty | customer  | points |
      | all     | placed             |         21 |          4 |           11 |        104 | obiwan212 |    212 |
      | all     | placed             |         21 |          4 |           11 |        104 | alice     |      2 |
      | some    | placed             |         20 |         12 |           17 |        100 | obiwan212 |    212 |
      # No change in inventory if the order has not yet been placed
      | all     | under construction |         20 |          2 |            8 |        100 | obiwan212 |    212 |
      | some    | pending            |         20 |          2 |            8 |        100 | alice     |      2 |

  Scenario Outline: Unsuccessfully cancel order
    Given the following orders exist in the system
      | id        | datePlaced | deadline | customer  | assignee      | state      |
      | bad_state | yesterday  | InOneDay | anakin501 | <oldAssignee> | <oldState> |
    And the following items are part of orders
      | order     | item | quantity |
      | bad_state | Eggs |        1 |
    When the user attempts to cancel the order with ID "bad_state"
    Then the system shall raise the error "<error>"
    And the order shall be "<oldState>"
    And the quantity of item "Eggs" shall be 20
    And the quantity of item "Chicken noodle soup" shall be 2
    And the quantity of item "Banana" shall be 8
    And the quantity of item "Grain of rice" shall be 100
    And "anakin501" shall have 501 points

    Examples: 
      | oldState           | oldAssignee | error                                                                |
      | in preparation     | alice       | cannot cancel an order that has already been assigned to an employee |
      | ready for delivery | bob         | cannot cancel an order that has already been assigned to an employee |
      | delivered          | claire      | cannot cancel an order that has already been assigned to an employee |
      | cancelled          | NULL        | order was already cancelled                                          |
      | cancelled          | alice       | order was already cancelled                                          |

  Scenario Outline: Successfully deliver order
    Given the following orders exist in the system
      | id    | datePlaced   | deadline   | customer  | assignee | state              |
      | ready | <datePlaced> | <deadline> | obiwan212 | claire   | ready for delivery |
    And the following items are part of orders
      | order | item | quantity |
      | ready | Eggs |        1 |
    When the manager attempts to mark the order with ID "ready" as delivered
    Then the system shall not raise any errors
    And the order shall be "delivered"

    Examples: 
      | datePlaced     | deadline    |
      | today          | SameDay     |
      | yesterday      | InOneDay    |
      | two days ago   | InTwoDays   |
      | three days ago | InThreeDays |
      # Better late than never
      | yesterday      | SameDay     |
      | two days ago   | SameDay     |
      | two days ago   | InOneDay    |

  Scenario Outline: Unsuccessfully mark order as delivered
    Given the following orders exist in the system
      | id        | datePlaced   | deadline   | customer  | assignee      | state      |
      | not_ready | <datePlaced> | <deadline> | anakin501 | <oldAssignee> | <oldState> |
    And the following items are part of orders
      | order     | item | quantity |
      | not_ready | Eggs |        1 |
    When the manager attempts to mark the order with ID "not_ready" as delivered
    Then the system shall raise the error "<error>"
    And the order shall be "<oldState>"

    Examples: 
      | datePlaced   | deadline    | oldState           | oldAssignee | error                                                             |
      | today        | InOneDay    | ready for delivery | alice       | cannot mark order as delivered before the delivery date           |
      | today        | InTwoDays   | ready for delivery | bob         | cannot mark order as delivered before the delivery date           |
      | today        | InThreeDays | ready for delivery | claire      | cannot mark order as delivered before the delivery date           |
      | yesterday    | InTwoDays   | ready for delivery | claire      | cannot mark order as delivered before the delivery date           |
      | yesterday    | InThreeDays | ready for delivery | claire      | cannot mark order as delivered before the delivery date           |
      | two days ago | InThreeDays | ready for delivery | claire      | cannot mark order as delivered before the delivery date           |
      | NULL         | SameDay     | under construction | NULL        | cannot mark an order as delivered if it is not ready for delivery |
      | NULL         | InOneDay    | pending            | NULL        | cannot mark an order as delivered if it is not ready for delivery |
      | today        | SameDay     | placed             | NULL        | cannot mark an order as delivered if it is not ready for delivery |
      | yesterday    | InOneDay    | in preparation     | bob         | cannot mark an order as delivered if it is not ready for delivery |
      | NULL         | InOneDay    | cancelled          | NULL        | cannot mark an order as delivered if it is not ready for delivery |
      | yesterday    | InOneDay    | cancelled          | NULL        | cannot mark an order as delivered if it is not ready for delivery |
