Feature: Create, update, and delete items
  As a manager, I want to add new items.
  As a manager, I want to update the price and point value of items.
  As a manager, I want to delete items that are no longer offered at my store.

  Background: 
    Given the following items exist in the system
      | name                | price | perishableOrNot | quantity | points |
      | Eggs                |   549 | perishable      |       20 |      5 |
      | Chicken noodle soup |   179 | non-perishable  |        0 |      2 |

  Scenario Outline: Successfully add a new item
    When the manager attempts to add a new "<perishableOrNot>" item with name "<name>" that's worth <points> points and costs <price> cents
    Then the system shall not raise any errors
    And an item shall exist with name "<name>"
    And the item shall cost <price> cents
    And the item shall be "<perishableOrNot>"
    And the item shall be worth <points> points
    And the quantity of the item shall be 0
    And the total number of items shall be 3

    Examples: 
      | name         | price | perishableOrNot | points |
      | Maple syrup  |   577 | perishable      |      5 |
      | 1% milk (1L) |   389 | perishable      |      4 |
      | 2% milk (2L) |   529 | perishable      |      5 |
      | Beans        |   179 | non-perishable  |      1 |

  Scenario Outline: Try to add an item with a duplicate name
    When the manager attempts to add a new "<newPerishableOrNot>" item with name "<name>" that's worth <newPoints> points and costs <newPrice> cents
    Then the system shall raise the error "an item called \"<name>\" already exists"
    And an item shall exist with name "<name>"
    And the item shall cost <oldPrice> cents
    And the item shall be "<oldPerishableOrNot>"
    And the item shall be worth <oldPoints> points
    And the quantity of the item shall be <oldQuantity>
    And the total number of items shall be 2

    Examples: 
      | name                | oldPrice | newPrice | oldPerishableOrNot | newPerishableOrNot | oldQuantity | newQuantity | oldPoints | newPoints |
      | Eggs                |      549 |      549 | perishable         | perishable         |          20 |          20 |         5 |         5 |
      | Chicken noodle soup |      179 |      199 | non-perishable     | perishable         |           0 |           1 |         2 |         3 |

  Scenario Outline: Try to add an invalid item
    When the manager attempts to add a new "<perishableOrNot>" item with name "<name>" that's worth <points> points and costs <price> cents
    Then the system shall raise the error "<error>"
    And no item shall exist with name "<name>"
    And the total number of items shall be 2

    Examples: 
      | name    | points | price | perishableOrNot | error                               |
      |         |      1 |   499 | perishable      | name is required                    |
      | Chicken |      0 |   499 | perishable      | points must be between one and five |
      | Chicken |     -1 |   499 | perishable      | points must be between one and five |
      | Chicken |      6 |   499 | perishable      | points must be between one and five |
      | Chicken |      7 |   499 | perishable      | points must be between one and five |
      | Beans   |      1 |     0 | non-perishable  | price must be positive              |
      | Beans   |      1 |    -1 | non-perishable  | price must be positive              |

  Scenario Outline: Successfully update item price
    When the manager attempts to update the price of item "<item>" to <newPrice>
    Then the system shall not raise any errors
    And an item shall exist with name "<item>"
    And the item shall cost <newPrice> cents
    And the item shall be "<oldPerishableOrNot>"
    And the item shall be worth <oldPoints> points
    And the quantity of the item shall be <oldQuantity>
    And the total number of items shall be 2

    Examples: 
      | item                | newPrice | oldPerishableOrNot | oldQuantity | oldPoints |
      | Eggs                |      599 | perishable         |          20 |         5 |
      | Eggs                |      500 | perishable         |          20 |         5 |
      | Chicken noodle soup |      199 | non-perishable     |           0 |         2 |
      | Chicken noodle soup |      135 | non-perishable     |           0 |         2 |

  Scenario Outline: Successfully update item point value
    When the manager attempts to update the point value of item "<item>" to <newPoints>
    Then the system shall not raise any errors
    And an item shall exist with name "<item>"
    And the item shall cost <newPrice> cents
    And the item shall be "<oldPerishableOrNot>"
    And the item shall be worth <newPoints> points
    And the quantity of the item shall be <oldQuantity>
    And the total number of items shall be 2

    Examples: 
      | item                | newPrice | oldPerishableOrNot | oldQuantity | newPoints |
      | Eggs                |      549 | perishable         |          20 |         4 |
      | Eggs                |      549 | perishable         |          20 |         3 |
      | Chicken noodle soup |      179 | non-perishable     |           0 |         1 |
      | Chicken noodle soup |      179 | non-perishable     |           0 |         3 |

  Scenario Outline: Try to update item that doesn't exist
    When the manager attempts to update the <field> of item "<item>" to <newValue>
    Then the system shall raise the error "there is no item called \"<item>\""
    And no item shall exist with name "<item>"
    And the total number of items shall be 2

    Examples: 
      | item        | field       | newValue |
      | nonexistent | price       |      200 |
      | not real    | price       |      200 |
      | nonexistent | point value |      200 |
      | not real    | point value |      200 |

  Scenario Outline: Try to update item with invalid values
    When the manager attempts to update the <field> of item "<item>" to <newValue>
    Then the system shall raise the error "<error>"
    And an item shall exist with name "<item>"
    And the item shall cost <oldPrice> cents
    And the item shall be "<oldPerishableOrNot>"
    And the item shall be worth <oldPoints> points
    And the quantity of the item shall be <oldQuantity>
    And the total number of items shall be 2

    Examples: 
      | item                | field       | newValue | oldPrice | oldPerishableOrNot | oldQuantity | oldPoints | error                               |
      | Eggs                | price       |        0 |      549 | perishable         |          20 |         5 | price must be positive              |
      | Chicken noodle soup | price       |       -1 |      179 | non-perishable     |           0 |         2 | price must be positive              |
      | Eggs                | point value |       -1 |      549 | perishable         |          20 |         5 | points must be between one and five |
      | Chicken noodle soup | point value |        0 |      179 | non-perishable     |           0 |         2 | points must be between one and five |
      | Eggs                | point value |        6 |      549 | perishable         |          20 |         5 | points must be between one and five |
      | Chicken noodle soup | point value |        7 |      179 | non-perishable     |           0 |         2 | points must be between one and five |

  Scenario Outline: Successfully delete an item
    When the manager attempts to delete the item "<name>"
    Then the system shall not raise any errors
    And no item shall exist with name "<name>"
    And the total number of items shall be 1

    Examples: 
      | name                |
      | Eggs                |
      | Chicken noodle soup |

  Scenario Outline: Try to delete an item that doesn't exist
    When the manager attempts to delete the item "<name>"
    Then the system shall raise the error "there is no item called \"<name>\""
    And no item shall exist with name "<name>"
    And the total number of items shall be 2

    Examples: 
      | name           |
      | Milk           |
      | Strawberry jam |
