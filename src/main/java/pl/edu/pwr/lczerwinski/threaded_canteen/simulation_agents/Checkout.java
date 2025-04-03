package pl.edu.pwr.lczerwinski.threaded_canteen.simulation_agents;

import java.util.LinkedList;
import java.util.Queue;

public class Checkout {
    public Cashier cashier = null;
    public Queue<Client> queue = new LinkedList<>();
    public boolean isOpen = false;

    public boolean isStaffed()
    {
        return cashier != null;
    }
    public String staffId()
    {
        if(isStaffed())
        {
            return cashier.id;
        }
        return "";
    }
    public int getQueueSize()
    {
        return queue.size();
    }

}
