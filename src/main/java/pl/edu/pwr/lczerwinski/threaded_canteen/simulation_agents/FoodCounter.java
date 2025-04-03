package pl.edu.pwr.lczerwinski.threaded_canteen.simulation_agents;


public class FoodCounter {
    public static int[] food;
    public static void initialiseFood(int howManyTypes)
    {
        food = new int[howManyTypes];
    }
    public static synchronized void incrementFood(int id)
    {
        id=id-1;
        food[id]=food[id]+1;
    }
}
