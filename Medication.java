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

    public void setThreshold(int lowStockThreshold){
        this.lowStockThreshold = lowStockThreshold;
    }

    public int getQuantity(){
        return amount;
    }


}