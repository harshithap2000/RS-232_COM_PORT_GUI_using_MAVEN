module com.example.rs232_com_port_gui_using_maven {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;


    opens com.example.rs232_com_port_gui_using_maven to javafx.fxml;
    exports com.example.rs232_com_port_gui_using_maven;
}