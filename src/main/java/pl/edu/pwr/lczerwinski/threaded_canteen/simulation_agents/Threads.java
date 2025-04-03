package pl.edu.pwr.lczerwinski.threaded_canteen.simulation_agents;

import java.util.Random;

public abstract class Threads extends Thread {
    public String id;
    public static boolean threadActive;
    protected void sleepProtected(double durationInSeconds)
    {
        try {
            sleep((long) (durationInSeconds*1000));
        } catch (InterruptedException e) {
            System.err.println("Przerwano watek");
        }
    }
    public Threads(String id)
    {
        super(id);
        this.id = id;
        threadActive = true;
    }
    protected int randomTimeInSeconds(int minInSeconds, int maxInSeconds)
    {
        Random random = new Random();
        return random.nextInt(maxInSeconds-minInSeconds) + minInSeconds;
    }
    protected Boolean getRandomFlip (int chanceInPercent)
    {
        Random random = new Random();
        return (random.nextInt(100) < chanceInPercent);
    }
}
