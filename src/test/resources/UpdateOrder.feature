Feature: Update order
  As a customer, I want to be able to add an item to an order so that I can buy the item.
  As a customer, I want to be able to change the quantity of an item in my order.

  Background: 
    Given the following customers exist in the system
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
      # Also, please convert the string "NULL" to null.
      | id | datePlaced | deadline    | customer  |
      | a  | NULL       | SameDay     | alice     |
      | b  | 2025-02-24 | InOneDay    | obiwan212 |
      | c  | NULL       | InTwoDays   | anakin501 |
      | d  | 2025-02-24 | InThreeDays | alice     |
    And the following items are part of orders
      | order | item                | quantity |
      | a     | Eggs                |        2 |
      | b     | Eggs                |        1 |
      | b     | Chicken noodle soup |        3 |
      | d     | Chicken noodle soup |        5 |

  Scenario Outline: Successfully add a new item to an order
    When the user attempts to add item "<item>" to the order with ID "<orderId>"
    Then the system shall not raise any errors
    And the order with ID "<orderId>" shall include 1 "<item>"
    And the order with ID "<orderId>" shall include <numItems> distinct items

    Examples: 
      | item                | orderId | numItems |
      | Chicken noodle soup | a       |        2 |
      | Eggs                | c       |        1 |
      | Chicken noodle soup | c       |        1 |

  Scenario Outline: Try to add item to an order that doesn't exist
    When the user attempts to add item "<item>" to the non-existent order with order number <orderNumber>
    Then the system shall raise the error "there is no order with number \"<orderNumber>\""
    And no order shall exist with order number <orderNumber>
    And the total number of orders shall be 4

    Examples: 
      | item                | orderNumber |
      | Eggs                |     9999999 |
      | Chicken noodle soup |   123456789 |

  Scenario Outline: Try to add item that doesn't exist to an order
    When the user attempts to add item "<item>" to the order with ID "<orderId>"
    Then the system shall raise the error "there is no item called \"<item>\""
    And no item shall exist with name "<item>"
    And the order with ID "<orderId>" shall not include any items called "<item>"
    And the order with ID "<orderId>" shall include <numItems> distinct items

    Examples: 
      | item          | orderId | numItems |
      | Unicorn steak | a       |        1 |
      | Dragon tail   | c       |        0 |

  Scenario: Try to add an item to an order that already contains that item
    When the user attempts to add item "Eggs" to the order with ID "a"
    Then the system shall raise the error "order already includes item \"Eggs\""
    And the order with ID "a" shall include 2 "Eggs"
    And the order with ID "a" shall include 1 distinct item

  Scenario: Try to add an item to an order that's already been placed
    When the user attempts to add item "Eggs" to the order with ID "d"
    Then the system shall raise the error "order has already been placed"
    And the order with ID "d" shall not include any items called "Eggs"
    And the order with ID "d" shall include 1 distinct item

  Scenario Outline: Successfully update quantity of item in order
    When the user attempts to set the quantity of item "<item>" in the order with ID "<orderId>" to <newQty>
    Then the system shall not raise any errors
    And the order with ID "<orderId>" shall include <newQty> "<item>"
    And the order with ID "<orderId>" shall include 1 distinct item

    Examples: 
      | item | orderId | newQty |
      | Eggs | a       |      1 |
      | Eggs | a       |      3 |
      | Eggs | a       |     42 |

  Scenario: Successfully remove item from order by setting its quantity to zero
    When the user attempts to set the quantity of item "Eggs" in the order with ID "a" to 0
    Then the system shall not raise any errors
    And the order with ID "a" shall not include any items called "Eggs"
    And the order with ID "a" shall include 0 distinct items

  Scenario Outline: Try to update quantity of item in an order that doesn't exist
    When the user attempts to set the quantity of item "<item>" in the non-existent order <orderNumber> to <newQty>
    Then the system shall raise the error "there is no order with number \"<orderNumber>\""
    And no order shall exist with order number <orderNumber>
    And the total number of orders shall be 4

    Examples: 
      | item                | orderNumber | newQty |
      | Eggs                |     9999999 |      2 |
      | Chicken noodle soup |   123456789 |      3 |

  Scenario Outline: Update quantity of item when order doesn't contain that item
    When the user attempts to set the quantity of item "<item>" in the order with ID "<orderId>" to <newQty>
    Then the system shall raise the error "<error>"
    And the order with ID "<orderId>" shall not include any items called "<item>"
    And the order with ID "<orderId>" shall include <numItems> distinct items

    Examples: 
      | item        | orderId | newQty | numItems | error                                     |
      | Dragon tail | a       |      2 |        1 | there is no item called \\"Dragon tail\\" |
      | Eggs        | c       |      1 |        0 | order does not include item \\"Eggs\\"    |

  Scenario Outline: Unsuccessfully update quantity of item in order
    When the user attempts to set the quantity of item "<item>" in the order with ID "<orderId>" to <newQty>
    Then the system shall raise the error "<error>"
    And the order with ID "<orderId>" shall include <oldQty> "<item>"
    And the order with ID "<orderId>" shall include <numItems> distinct items

    Examples: 
      | item                | orderId | newQty | oldQty | numItems | error                         |
      | Eggs                | a       |     -1 |      2 |        1 | quantity must be non-negative |
      | Eggs                | a       |     -2 |      2 |        1 | quantity must be non-negative |
      | Eggs                | b       |     10 |      1 |        2 | order has already been placed |
      | Chicken noodle soup | b       |      7 |      3 |        2 | order has already been placed |
