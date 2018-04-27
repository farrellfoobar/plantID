package uci.plantID;

public class rankedPlant implements Comparable
{
    private plant p;
    private double rank;

    public rankedPlant( plant P, double rank)
    {
        this.p = p;
        this.rank = rank;
    }

    @Override
    public int compareTo(Object o)
    {
        if( o instanceof rankedPlant )
            return Double.compare( this.rank, ((rankedPlant) o).rank);
        else
            return -1;
    }

    public double getRank()
    {
        return this.rank;
    }

    public plant getPlant()
    {
        return this.p;
    }
}
