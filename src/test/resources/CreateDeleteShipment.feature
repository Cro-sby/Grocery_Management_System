Feature: Create and delete shipments
  As a manager, I want to create shipments to buy new items.
  As a manager, I want to delete shipments if I no longer intend to order them.

  Background:
    Given the following shipments exist
      # There's no way to set the autounique shipment number, so refer to shipments here using a separate ID.
      # The controller should still identify shipments by their shipment number.
      # You'll need to create a map from string IDs to shipment numbers.
      # Also, please convert the string "NULL" to null.
      | id | dateOrdered |
      | a  | NULL        |
      | b  | NULL        |
      | c  | 2025-02-24  |
      | d  | 2025-01-01  |

  Scenario: Successfully create a new shipment
    When the manager attempts to create a new shipment
    Then the system shall not raise any errors
    And a new shipment shall exist
    And the newly-created shipment shall have 0 items
    And the newly-created shipment shall not have been ordered yet
    And the total number of shipments shall be 5

  Scenario Outline: Successfully delete a shipment
    When the manager attempts to delete the shipment with ID "<id>"
    Then the system shall not raise any errors
    And no shipment shall exist with ID "<id>"
    And the total number of shipments shall be 3

    Examples:
      | id |
      | a  |
      | b  |

  Scenario Outline: Try to delete a shipment that doesn't exist
    When the manager attempts to delete the non-existent shipment with shipment number <number>
    Then the system shall raise the error "there is no shipment with number \"<number>\""
    And no shipment shall exist with shipment number <number>
    And the total number of shipments shall be 4

    Examples:
      | number    |
      |   9999999 |
      | 123456789 |

  Scenario Outline: Try to delete a shipment that's already been ordered
    When the manager attempts to delete the shipment with ID "<id>"
    Then the system shall raise the error "cannot delete a shipment which has already been ordered"
    And a shipment shall exist with ID "<id>"
    And the total number of shipments shall be 4

    Examples:
      | id |
      | c  |
      | d  |
