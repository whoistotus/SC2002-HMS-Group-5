package SC2002_Assignment;

import java.util.ArrayList;

public class Pharmacy {
    private ArrayList<Medicine> inventory;

    public Pharmacy() {
        inventory = new ArrayList<>();
    }

    public void addMedicine(Medicine medicine) {
        inventory.add(medicine);
    }

    public void displayInventory() {
        System.out.println("Medicine Inventory:");
        for (Medicine med : inventory) {
            med.print();
        }
    }

    public Medicine getMedicineByName(String name) {
        for (Medicine medicine : inventory) {
            if (medicine.getMedicineName().equalsIgnoreCase(name)) {
                return medicine;
            }
        }
        return null; // Return null if no match is found
    }

}
