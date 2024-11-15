package SC2002_Assignment;

import java.util.Map;

public class InventoryView {

    // Method to display the inventory
    public void displayInventory(Map<Medication, Integer> medicationStock) {
        if (medicationStock == null || medicationStock.isEmpty()) {
            System.out.println("The inventory is empty.");
            return;
        }

        System.out.println("Inventory:");
        for (Map.Entry<Medication, Integer> entry : medicationStock.entrySet()) {
            Medication medication = entry.getKey();
            Integer quantity = entry.getValue();

            // Display the medication name and its stock quantity
            System.out.println(medication.getName() + ": " + quantity + " units available");
        }
    }
}
