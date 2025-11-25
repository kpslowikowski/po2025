module auto_ui.symulatorgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens auto_ui.symulatorgui to javafx.fxml;
    exports auto_ui.symulatorgui;
}