package pl.edu.pwr.lczerwinski.threaded_canteen.simulation_agents;

import java.util.LinkedList;
import java.util.Queue;

public class FoodStation {
    public Cook cook = null;
    public Queue<Client> queue = new LinkedList<>();
    public boolean isOpen = false;

    public boolean isStaffed()
    {
        return cook != null;
    }
    public String staffId()
    {
        if(isStaffed())
        {
            return cook.id;
        }
        return "";
    }
    public int getQueueSize()
    {
        return queue.size();
    }

}
