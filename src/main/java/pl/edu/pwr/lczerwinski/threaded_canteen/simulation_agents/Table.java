package pl.edu.pwr.lczerwinski.threaded_canteen.simulation_agents;

public class Table {
    public String id;
    public int n;
    public Client[] clients;
    public Table(int n)
    {
        this.n = n;
        clients = new Client[2*n];
    }
}
