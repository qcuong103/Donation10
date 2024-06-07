package ie.app.models;

public class Donation {
    public int amount;
    public String method;
    public int id;

    public Donation (int amount, String method)
    {
        this.amount = amount;
        this.method = method;
    }

    public Donation ()
    {
        this.amount = 0;
        this.method = "";
    }

    public String toString()
    {
        return id + ", " + amount + ", " + method;
    }
}


