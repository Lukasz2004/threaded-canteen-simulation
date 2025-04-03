package pl.edu.pwr.lczerwinski.threaded_canteen.simulation_agents;

import pl.edu.pwr.lczerwinski.threaded_canteen.logic.Logic;

public class Cashier extends Threads {
    public Checkout assignedStation = null;
    public void checkout()
    {
        sleepProtected(randomTimeInSeconds(10,20)/ Logic.simulationSpeed);
        if(assignedStation.getQueueSize()>0)
        {
            FoodCounter.incrementFood(assignedStation.queue.peek().currentDish);

            assignedStation.queue.peek().hasPaid=true;
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
                Logic.addCashierToStation(this);
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
                Logic.removeCashierFromStation(this);
                sleepProtected(randomTimeInSeconds(10,30)/Logic.simulationSpeed);
                continue;
            }
            checkout();
        }
    }
    public Cashier(String id)
    {
        super(id);
    }
}
