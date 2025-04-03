package pl.edu.pwr.lczerwinski.threaded_canteen.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.edu.pwr.lczerwinski.threaded_canteen.logic.Logic;
import pl.edu.pwr.lczerwinski.threaded_canteen.simulation_agents.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GUI extends Application implements Initializable {
    private boolean isSimulationRunning = false;
    private ScheduledExecutorService guiUpdater;
    private final int queueDisplayLength=15;
    public BarChart chart;
    @FXML
    private VBox clientList;
    @FXML
    private Label foodStation0Employee;
    @FXML
    private Label foodStation1Employee;
    @FXML
    private Label foodStation0Queue;
    @FXML
    private Label foodStation1Queue;
    @FXML
    private Label checkout0Queue;
    @FXML
    private Label checkout1Queue;
    @FXML
    private Label checkout2Queue;
    @FXML
    private Label checkout3Queue;
    @FXML
    private Label checkout0Employee;
    @FXML
    private Label checkout1Employee;
    @FXML
    private Label checkout2Employee;
    @FXML
    private Label checkout3Employee;
    @FXML
    private TextArea seating;
    @FXML
    private TextArea onBreak;
    @FXML
    private VBox chartVbox;
    @FXML
    private Slider speedSlider;
    @FXML
    private Label speedShow;
    private int chartRefreshIterator;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("GUI-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("Canteen Simulation");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e -> Threads.threadActive=false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public static void main(String[] args) {
        launch();
    }



    public void runSimulation() {
        if(!isSimulationRunning)
        {
            isSimulationRunning=true;
            Logic.startSimulation(20,3,3,2);
            speedSlider.valueProperty().addListener(
                    new ChangeListener<Number>() {

                        public void changed(ObservableValue<? extends Number >
                                                    observable, Number oldValue, Number newValue)
                        {

                            speedShow.setText("x" + newValue);
                            Logic.simulationSpeed= (double) newValue;
                        }
                    });
            guiUpdater = Executors.newSingleThreadScheduledExecutor();
            guiUpdater.scheduleAtFixedRate(this::updateGUI, 0, 100, TimeUnit.MILLISECONDS);
        }
    }
    public void runCustomSimulation() {
        if(!isSimulationRunning)
        {
            int nClients=dialogModal("klientów",20);
            int nCooks=dialogModal("kucharzy",3);
            int nCashiers=dialogModal("kasjerów",3);
            int nFoods=dialogModal("potraw",2);

            isSimulationRunning=true;
            Logic.startSimulation(nClients,nCooks,nCashiers,nFoods);
            guiUpdater = Executors.newSingleThreadScheduledExecutor();
            guiUpdater.scheduleAtFixedRate(this::updateGUI, 0, 100, TimeUnit.MILLISECONDS);
        }
    }





    public int dialogModal(String inputType, int defaultValue)
    {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Parametry symulacji");
        dialog.setHeaderText("Własne parametry symulacji");
        dialog.setContentText("Podaj liczbę " + inputType + ":");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return Integer.parseInt(result.get());
        }
        else
        {
            return defaultValue;
        }
    }

    private void updateGUI() {
        Platform.runLater(() -> {
            try {
                updateClientList(Logic.clients);
                updateFoodStationList(Logic.foodStations);
                updateCheckoutList(Logic.checkouts);
                updateSeating(Logic.tables);
                updateBreak(Logic.onBreak);
                updateGraph();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }


    public void updateClientList(ArrayList<Client> clients)
    {
        if(clientList.getChildren().size()!=clients.size())
        {
            clientList.getChildren().clear();
            for (Client client:clients)
            {
                clientList.getChildren().add(new Label(client.id));
            }
        }
        for(Client client:clients)
        {
            Label label = (Label) clientList.getChildren().get(clients.indexOf(client));
            if(!client.isInside)
            {
                label.setText(client.id);
            }
            else
            {
                label.setText(".");
            }
        }

    }
    public void updateFoodStationList(ArrayList<FoodStation> foodStations)
    {
        StringBuilder temp = new StringBuilder();
        for(Client client:foodStations.get(0).queue)
        {
            temp.append(client.id);
        }
        foodStation0Queue.setText(formatString(temp,foodStations.get(0).isOpen));
        foodStation0Employee.setText("Punkt 1: " + foodStations.get(0).staffId());
        temp = new StringBuilder();
        for(Client client:foodStations.get(1).queue)
        {
            temp.append(client.id);
        }
        foodStation1Employee.setText("Punkt 2: " + foodStations.get(1).staffId());
        foodStation1Queue.setText(formatString(temp,foodStations.get(1).isOpen));
    }
    public void updateCheckoutList(ArrayList<Checkout> checkouts)
    {
        StringBuilder temp = new StringBuilder();
        for(Client client:checkouts.get(0).queue)
        {
            temp.append(client.id);
        }
        checkout0Queue.setText(formatString(temp,checkouts.get(0).isOpen));
        checkout0Employee.setText("Kasa 1: " + checkouts.get(0).staffId());

        temp = new StringBuilder();
        for(Client client:checkouts.get(1).queue)
        {
            temp.append(client.id);
        }
        checkout1Queue.setText(formatString(temp,checkouts.get(1).isOpen));
        checkout1Employee.setText("Kasa 2: " + checkouts.get(1).staffId());

        temp = new StringBuilder();
        for(Client client:checkouts.get(2).queue)
        {
            temp.append(client.id);
        }
        checkout2Queue.setText(formatString(temp,checkouts.get(2).isOpen));
        checkout2Employee.setText("Kasa 3: " + checkouts.get(2).staffId());

        temp = new StringBuilder();
        for(Client client:checkouts.get(3).queue)
        {
            temp.append(client.id);
        }
        checkout3Queue.setText(formatString(temp,checkouts.get(3).isOpen));
        checkout3Employee.setText("Kasa 4: " + checkouts.get(3).staffId());

    }
    public void updateSeating(ArrayList<Table> tables)
    {
        seating.clear();
        StringBuilder temp = new StringBuilder();
        for(int i=0; i<tables.get(0).n; i++)
        {
            for(Table table:tables)
            {
                if(table.clients[i]!=null)
                {
                    temp.append(table.clients[i].id);
                }
                else
                {
                    temp.append(" ");
                }
                temp.append("[||]");
                if(table.clients[(table.n) + i]!=null)
                {
                    temp.append(table.clients[(table.n) + i].id);
                }
                else
                {
                    temp.append(" ");
                }
                temp.append("   ");
            }
            temp.append("\n");
        }
        seating.setText(temp.toString());
    }
    public void updateBreak(ArrayList<String> onBreakList)
    {
        onBreak.clear();
        StringBuilder temp = new StringBuilder();
        for(String s:onBreakList)
        {
            temp.append(s);
            temp.append("\n");
        }
        onBreak.setText(temp.toString());
    }
    public void createGraph()
    {
        if(chart==null)
        {
            final NumberAxis xAxis = new NumberAxis();
            final CategoryAxis yAxis = new CategoryAxis();
            final BarChart<Number,String> bc =
                    new BarChart<Number,String>(xAxis,yAxis);
            xAxis.setLabel("Ilość");
            xAxis.setTickLabelRotation(90);
            yAxis.setLabel("Numer dania");
            chartVbox.getChildren().add(bc);
            XYChart.Series series1 = new XYChart.Series();
            for(int i = 0; i< FoodCounter.food.length; i++)
            {
                series1.getData().add(new XYChart.Data(0,String.valueOf(i+1)));
            }

            bc.setMaxSize(300, 200);
            bc.setMinSize(300, 200);
            bc.getData().add(series1);
            chart=bc;
        }
    }
    public void updateGraph()
    {
        createGraph();
        chartRefreshIterator++;
        if(chartRefreshIterator>10) {
            XYChart.Series series1 = (XYChart.Series) chart.getData().get(0);
            for(int i=0; i<series1.getData().size(); i++)
            {
                XYChart.Data data = (XYChart.Data) series1.getData().get(i);
                data.setXValue(FoodCounter.food[i]);
            }

            chartRefreshIterator=0;
        }
    }
    private String formatString(StringBuilder toFormat, boolean isActive)
    {
        while (toFormat.length()<queueDisplayLength)
        {
            if(isActive){toFormat.append(".");}
            else{toFormat.append("|");}
        }
        return toFormat.reverse().toString();
    }
}