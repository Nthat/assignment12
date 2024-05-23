module com.example.nthatisi {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.nthatisi to javafx.fxml;
    exports com.example.nthatisi;
}