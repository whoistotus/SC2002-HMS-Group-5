package SC2002_Assignment;


public class Medication
{
    private String medicationName;
    private int amount;
    private String description;
    private int lowStockThreshold;

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


}