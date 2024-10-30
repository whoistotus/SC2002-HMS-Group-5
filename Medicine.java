package SC2002_Assignment;


public class Medicine
{
    //Name of Medicine
    private String name;

    //amt of Medicine
    private int amount;

    //constructor for Medicine
    public Medicine(String name, int amount)
    {
        this.name = name;
        this.amount = amount;
    }

    public String getMedicineName()
    {
        return this.name;
    }

    public int getMedicineAmount()
    {
        return this.amount;
    }

    //used by the pharmacist to give prescription
    //@param amount of medicine given
    public boolean prescribe(int given)
    {
        if (amount >= given)
        {
            amount -= given;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void restockMedicine(int quantity)
    {
        amount += quantity;
    }

    //for inventory to request restock
    //public idk yet 

    public void print()
    {
        System.out.println(name + " : " + amount);
    }
}