module com.example.parsecurse {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;
    requires poi;


    opens com.example.parsecurse to javafx.fxml;
    exports com.example.parsecurse;
}