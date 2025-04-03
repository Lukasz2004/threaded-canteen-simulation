package pl.edu.pwr.lczerwinski.threaded_canteen.logic;

import pl.edu.pwr.lczerwinski.threaded_canteen.simulation_agents.*;

import java.util.ArrayList;

public class Logic {
    public static ArrayList<Client> clients = new ArrayList<>();
    public static ArrayList<FoodStation> foodStations = new ArrayList<>();
    public static ArrayList<Cook> cooks = new ArrayList<>();
    public static ArrayList<Checkout> checkouts = new ArrayList<>();
    public static ArrayList<Cashier> cashiers = new ArrayList<>();
    public static ArrayList<Table> tables = new ArrayList<>();
    public static ArrayList<String> onBreak = new ArrayList<>();
    public static double simulationSpeed=1;
    public static void startSimulation(int numberOfClients,int numberOfCooks, int numberOfCashiers, int numberOfFoods)
    {
        Cook.availableFoods=numberOfFoods;
        FoodCounter.initialiseFood(numberOfFoods);
        for (int i=0; i<2; i++)
        {
            foodStations.add(new FoodStation());
        }
        for(int i=0; i<4; i++)
        {
            checkouts.add(new Checkout());
        }
        for (int i = 0; i < 2; i++)
        {
            tables.add(new Table(4));
        }


        for (int i=0; i<numberOfClients; i++)
        {
            clients.add(new Client(String.valueOf((char) (97+i))));
            clients.get(i).start();
        }
        for (int i = 0; i < numberOfCooks; i++)
        {
            cooks.add(new Cook("Cook " + i));
            cooks.get(i).start();
            onBreak.add(cooks.get(i).id);
        }
        for (int i = 0; i < numberOfCashiers; i++)
        {
            cashiers.add(new Cashier("Cashier " + i));
            cashiers.get(i).start();
            onBreak.add(cashiers.get(i).id);
        }
    }

    public static synchronized void addClientToFoodStation(Client client)
    {
        FoodStation bestFoodStation = foodStations.get(0);
        for(FoodStation foodStation : foodStations)
        {
            if(!bestFoodStation.isOpen && foodStation.isOpen)
            {
                bestFoodStation = foodStation;
            }
            if(foodStation.getQueueSize()<bestFoodStation.getQueueSize() && foodStation.isOpen)
            {
                bestFoodStation = foodStation;
            }
        }
        bestFoodStation.queue.add(client);
    }


    public static synchronized void addClientToCheckout(Client client) {
        Checkout bestCheckout = checkouts.get(0);
        for(Checkout checkout : checkouts)
        {
            if(!bestCheckout.isOpen && checkout.isOpen)
            {
                bestCheckout = checkout;
            }
            if(checkout.getQueueSize()<bestCheckout.getQueueSize() && checkout.isOpen)
            {
                bestCheckout = checkout;
            }
        }
        bestCheckout.queue.add(client);
    }

    public static synchronized void addClientToTable(Client client) {
        for (Table table : tables)
        {
            for (int i=0; i<(2*table.n); i++)
            {
                if(table.clients[i] == null)
                {
                    table.clients[i] = client;
                    client.isSitting = true;
                    client.table=table;
                    client.tableIndex=i;
                    return;
                }
            }
        }
    }
    public static synchronized void removeClientFromTable(Client client) {
        client.table.clients[client.tableIndex] = null;
    }
    public static synchronized void addCookToStation(Cook cook)
    {
        for(FoodStation foodStation : foodStations)
        {
            if(!foodStation.isStaffed())
            {
                foodStation.cook = cook;
                cook.assignedStation = foodStation;
                foodStation.isOpen = true;
                for(int i=0; i<onBreak.size(); i++)
                {
                    if(onBreak.get(i).equals(cook.id))
                    {
                        onBreak.remove(i);
                    }
                }
                return;
            }
        }
    }
    public static synchronized void removeCookFromStation(Cook cook)
    {
        cook.assignedStation.cook = null;
        cook.assignedStation = null;
        onBreak.add(cook.id);
    }

    public static synchronized void addCashierToStation(Cashier cashier) {
        for(Checkout checkout : checkouts)
        {
            if(!checkout.isStaffed())
            {
                checkout.cashier = cashier;
                cashier.assignedStation = checkout;
                checkout.isOpen = true;
                for(int i=0; i<onBreak.size(); i++)
                {
                    if(onBreak.get(i).equals(cashier.id))
                    {
                        onBreak.remove(i);
                    }
                }
                return;
            }
        }
    }

    public static synchronized void removeCashierFromStation(Cashier cashier) {
        cashier.assignedStation.cashier = null;
        cashier.assignedStation = null;
        onBreak.add(cashier.id);
    }
}
