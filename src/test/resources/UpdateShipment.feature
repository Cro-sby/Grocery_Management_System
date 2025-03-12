Feature: Update shipment
  As a manager, I want to be able to add an item to a shipment so that I can buy the item.
  As a manager, I want to be able to change the quantity of an item in a shipment.

  Background:
    Given the following items exist in the system
      | name                | price | perishableOrNot | quantity | points |
      | Eggs                |   549 | perishable      |       20 |      5 |
      | Chicken noodle soup |   179 | non-perishable  |        0 |      2 |
    And the following shipments exist
      # There's no way to set the autounique shipment number, so refer to shipments here using a separate ID.
      # The controller should still identify shipments by their shipment number.
      # You'll need to create a map from string IDs to shipment numbers.
      # Also, please convert the string "NULL" to null.
      | id | datePlaced |
      | a  | NULL       |
      | b  | 2025-02-24 |
      | c  | NULL       |
      | d  | 2025-02-24 |
    And the following items are part of shipments
      | shipment | item                | quantity |
      | a        | Eggs                |      200 |
      | b        | Eggs                |      100 |
      | b        | Chicken noodle soup |      150 |
      | d        | Chicken noodle soup |       50 |

  Scenario Outline: Successfully add a new item to a shipment
    When the manager attempts to add item "<item>" to the shipment with ID "<shipmentId>"
    Then the system shall not raise any errors
    And the shipment with ID "<shipmentId>" shall include 1 "<item>"
    And the shipment with ID "<shipmentId>" shall include <numItems> distinct items

    Examples:
      | item                | shipmentId | numItems |
      | Chicken noodle soup | a          |        2 |
      | Eggs                | c          |        1 |
      | Chicken noodle soup | c          |        1 |

  Scenario Outline: Try to add item to a shipment that doesn't exist
    When the user attempts to add item "<item>" to the non-existent shipment with number <shipmentNumber>
    Then the system shall raise the error "there is no shipment with number \"<shipmentNumber>\""
    And no shipment shall exist with number <shipmentNumber>
    And the total number of shipments shall be 4

    Examples:
      | item                | shipmentNumber |
      | Eggs                |        9999999 |
      | Chicken noodle soup |      123456789 |

  Scenario Outline: Try to add item that doesn't exist to a shipment
    When the manager attempts to add item "<item>" to the shipment with ID "<shipmentId>"
    Then the system shall raise the error "there is no item called \"<item>\""
    And no item shall exist with name "<item>"
    And the shipment with ID "<shipmentId>" shall not include any items called "<item>"
    And the shipment with ID "<shipmentId>" shall include <numItems> distinct items

    Examples:
      | item          | shipmentId | numItems |
      | Unicorn steak | a          |        1 |
      | Dragon tail   | c          |        0 |

  Scenario: Try to add an item to a shipment that already contains that item
    When the manager attempts to add item "Eggs" to the shipment with ID "a"
    Then the system shall raise the error "shipment already includes item \"Eggs\""
    And the shipment with ID "a" shall include 200 "Eggs"
    And the shipment with ID "a" shall include 1 distinct item

  Scenario: Try to add an item to a shipment that's already been ordered
    When the manager attempts to add item "Eggs" to the shipment with ID "d"
    Then the system shall raise the error "shipment has already been ordered"
    And the shipment with ID "d" shall not include any items called "Eggs"
    And the shipment with ID "d" shall include 1 distinct item

  Scenario Outline: Successfully update quantity of item in shipment
    When the manager attempts to set the quantity of item "<item>" in the shipment with ID "<shipmentId>" to <newQty>
    Then the system shall not raise any errors
    And the shipment with ID "<shipmentId>" shall include <newQty> "<item>"
    And the shipment with ID "<shipmentId>" shall include 1 distinct item

    Examples:
      | item | shipmentId | newQty |
      | Eggs | a          |      1 |
      | Eggs | a          |      3 |
      | Eggs | a          |     42 |

  Scenario: Successfully remove item from shipment by setting its quantity to zero
    When the manager attempts to set the quantity of item "Eggs" in the shipment with ID "a" to 0
    Then the system shall not raise any errors
    And the shipment with ID "a" shall not include any items called "Eggs"
    And the shipment with ID "a" shall include 0 distinct items

  Scenario Outline: Try to update quantity of item in a shipment that doesn't exist
    When the manager attempts to set the quantity of item "<item>" in the non-existent shipment <shipmentNumber> to <newQty>
    Then the system shall raise the error "there is no shipment with number \"<shipmentNumber>\""
    And no shipment shall exist with number <shipmentNumber>
    And the total number of shipments shall be 4

    Examples:
      | item                | shipmentNumber | newQty |
      | Eggs                |        9999999 |      2 |
      | Chicken noodle soup |      123456789 |      3 |

  Scenario Outline: Update quantity of item when shipment doesn't contain that item
    When the manager attempts to set the quantity of item "<item>" in the shipment with ID "<shipmentId>" to <newQty>
    Then the system shall raise the error "<error>"
    And the shipment with ID "<shipmentId>" shall not include any items called "<item>"
    And the shipment with ID "<shipmentId>" shall include <numItems> distinct items

    Examples:
      | item        | shipmentId | newQty | numItems | error                                     |
      | Dragon tail | a          |      2 |        1 | there is no item called \\"Dragon tail\\" |
      | Eggs        | c          |      1 |        0 | shipment does not include item \\"Eggs\\" |

  Scenario Outline: Unsuccessfully update quantity of item in shipment
    When the manager attempts to set the quantity of item "<item>" in the shipment with ID "<shipmentId>" to <newQty>
    Then the system shall raise the error "<error>"
    And the shipment with ID "<shipmentId>" shall include <oldQty> "<item>"
    And the shipment with ID "<shipmentId>" shall include <numItems> distinct items

    Examples:
      | item                | shipmentId | newQty | oldQty | numItems | error                             |
      | Eggs                | a          |     -1 |    200 |        1 | quantity must be non-negative     |
      | Eggs                | a          |     -2 |    200 |        1 | quantity must be non-negative     |
      | Eggs                | b          |     10 |    100 |        2 | shipment has already been ordered |
      | Chicken noodle soup | b          |      7 |    150 |        2 | shipment has already been ordered |
