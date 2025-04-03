package pl.edu.pwr.lczerwinski.threaded_canteen.simulation_agents;

import pl.edu.pwr.lczerwinski.threaded_canteen.logic.Logic;

public class Client extends Threads {
    public int currentDish = 0;
    public boolean hasPaid = false;
    public boolean isInside = false;
    public boolean isSitting = false;
    public Table table = null;
    public int tableIndex = 0;

    public void run()
    {
        System.out.println("Starting Client " + this.id);
        while (threadActive)
        {
            sleepProtected(randomTimeInSeconds(10,90)/ Logic.simulationSpeed);
            isInside = true;
            Logic.addClientToFoodStation(this);
            while (currentDish == 0) {sleepProtected(1);}
            Logic.addClientToCheckout(this);
            while (!hasPaid) {sleepProtected(1);}
            while(!isSitting)
            {
                sleepProtected(1);
                Logic.addClientToTable(this);
            }
            sleepProtected(randomTimeInSeconds(10,30)/Logic.simulationSpeed);
            Logic.removeClientFromTable(this);
            currentDish=0;
            hasPaid=false;
            isInside=false;
            isSitting=false;
        }
    }
    public Client(String id)
    {
        super(id);
    }
}
