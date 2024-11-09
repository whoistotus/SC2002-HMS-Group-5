package SC2002_Assignment;

import java.util.ArrayList;

public class InventoryController {

    private ArrayList<Medication> medicationList = new ArrayList<>(); 

    public void addMedication(Medication medication, int initialStock)
    {
        medication.setQuantity(initialStock);
        medicationList.add(medication);
    }
    
    public boolean isLowStock(Medication medication, int threshold)
    {
        return medication.getQuantity() < threshold;
    }

    public void removeMedication(Medication medication)
    { 
        medicationList.remove(medication);
    }

    public void displayInventory()
    {
        for (Medication med : medicationList)
        {
            System.out.println(med.getName() + " - Stock: " + med.getQuantity());
        }
    }
}

