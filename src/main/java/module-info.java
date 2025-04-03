module pl.edu.pwr.jp.lczerwinski.lab05 {
    requires javafx.controls;
    requires javafx.fxml;


    opens pl.edu.pwr.lczerwinski.threaded_canteen.gui to javafx.fxml;
    exports pl.edu.pwr.lczerwinski.threaded_canteen.gui;
}