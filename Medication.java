package SC2002_Assignment;


public class Medication
{
    private String medicationName;
    private int amount;
    private String description;
    private int lowStockThreshold;

    public Medication(String medicationName, int amount, String description, int lowStockThreshold)
    {
        this.medicationName = medicationName.trim().toLowerCase(); //standardised to lower case
        setQuantity(amount);
        this.description = description;
        setThreshold(lowStockThreshold);
    }

    public String getName(){
        return medicationName;
    }

    public int getThreshold()
    {
        return lowStockThreshold;
    }

    public void setThreshold(int lowStockThreshold){
        if (lowStockThreshold <= 0)
        {
            System.out.println("Invalid threshold.");
            return;
        }
        this.lowStockThreshold = lowStockThreshold;
    }

    public int getQuantity(){
        return amount;
    }

    public void setQuantity(int amount) {
        if (amount < 0) {
            System.out.println("Invalid stock amount.");
            return;
        }
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void subtractQuantity(int quantityToSubtract) {
        if (quantityToSubtract <= 0) {
            System.out.println("Invalid quantity to subtract.");
            return;
        }
        
        if (quantityToSubtract > amount) {
            System.out.println("Insufficient stock. Available stock: " + amount);
            return;
        }
    
        amount -= quantityToSubtract;
        System.out.println("Subtracted " + quantityToSubtract + " from " + medicationName + ". Remaining stock: " + amount);
    
        if (amount < lowStockThreshold) {
            System.out.println("Warning: Stock of " + medicationName + " is below the low stock threshold.");
        }
    }

    @Override
    public String toString()
    {
        return "Medication: " + medicationName + ", Stock: " + amount + ", Threshold: " + lowStockThreshold + ", Description: " + description;
    }
}