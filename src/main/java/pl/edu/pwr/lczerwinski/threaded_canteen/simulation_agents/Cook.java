package pl.edu.pwr.lczerwinski.threaded_canteen.simulation_agents;

import pl.edu.pwr.lczerwinski.threaded_canteen.logic.Logic;

import java.util.Random;

public class Cook extends Threads {
    public static int availableFoods;
    public FoodStation assignedStation = null;
    public void serveFood()
    {
        sleepProtected(randomTimeInSeconds(10,20)/ Logic.simulationSpeed);
        if(assignedStation.getQueueSize()>0)
        {
            Random random = new Random();
            assignedStation.queue.peek().currentDish=random.nextInt(availableFoods)+1;
            assignedStation.queue.remove();
        }
    }
    public void run()
    {
        System.out.println("Starting " + this.id);
        while (threadActive)
        {
            if(assignedStation==null)
            {
                sleepProtected(randomTimeInSeconds(5,10)/Logic.simulationSpeed);
                Logic.addCookToStation(this);
                continue;
            }

            //Chance of Going on Break
            if(assignedStation.isOpen && getRandomFlip(10))
            {
                assignedStation.isOpen = false;
            }

            //Go on Break
            if(!assignedStation.isOpen && assignedStation.getQueueSize()==0)
            {
                Logic.removeCookFromStation(this);
                sleepProtected(randomTimeInSeconds(10,30)/Logic.simulationSpeed);
                continue;
            }
            serveFood();
        }
    }
    public Cook(String id)
    {
        super(id);
    }
}
