package com.example.rs232_com_port_gui_using_maven;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import com.fazecast.jSerialComm.SerialPort;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.io.OutputStream;
import java.io.IOException;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
public class HelloController implements Initializable {
    public Button search_port;
    public ChoiceBox port_box;
    public Button connect;
    public Button disconnect;
    public ChoiceBox<String> port_name;
    public ChoiceBox<Integer> baud_rate;
    public ChoiceBox<String> parity_bits;
    public ChoiceBox<Integer> data_bits;
    public ChoiceBox<Double>stop_bit;
    public ChoiceBox<Integer>flow_control;
    public Text connection_status;
    public TextField command_box;
    public TextArea sent_command;
    public Button clear_console;
    public TextArea console_box;
    public Button send_command;
    @FXML
    private Label welcomeText;
    Integer baud[]={4800,9600,38400,57600,115200};
    Integer dataBits[]={6,7,8};
    Double stopBits[]={1.0,1.5,2.0};
    String parityBits[]={"NO_PARITY","EVEN_PARITY","ODD_PARITY","MARK_PARITY","SPACE_PARITY"};
    ArrayList<String> port_array_list=new ArrayList<String>();
    SerialPort serialPortl;
    OutputStream outputStream;
    String dataBuffer="";
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void onSearchPort(ActionEvent actionEvent) {
    }

    public void onConnect(ActionEvent actionEvent) {

        try{
            SerialPort []portLists=SerialPort.getCommPorts();
            serialPortl=portLists[port_name.getItems().indexOf(port_name.getValue())];
            serialPortl.setBaudRate(baud_rate.getValue());
            serialPortl.setNumDataBits(data_bits.getValue());
            serialPortl.setNumStopBits((stop_bit.getValue()).intValue());
            serialPortl.setParity(parity_bits.getItems().indexOf(parity_bits.getValue()));
            System.out.println(parity_bits.getItems().indexOf(parity_bits.getValue()));
            System.out.println(port_name.getItems().indexOf(port_name.getValue()));
            serialPortl.openPort();
            if(serialPortl.isOpen()){

                System.out.println("successfully connected to "+serialPortl.getDescriptivePortName());
                disconnect.setDisable(false);
                connect.setDisable(true);
                connection_status.setText("CONNECTED");
                Serial_EventBasedReading(serialPortl);
            }
            else{
                System.out.println("Failed");
            }

        }catch(ArrayIndexOutOfBoundsException a) {
            System.out.println("array out of bounds");
        }
        catch(Exception b) {
            System.out.println("exception");
        }
    }

    public void onDisconnect(ActionEvent actionEvent) {
        if(serialPortl.isOpen()){
            serialPortl.closePort();
            connect.setDisable(false);
            disconnect.setDisable(true);
            connection_status.setText("DISCONNECTED");
        }

    }
    private void Serial_EventBasedReading(SerialPort activePort){
        activePort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                byte[] newData= serialPortEvent.getReceivedData();
                for(int i=0;i<newData.length;i++){
                    dataBuffer+=(char) newData[i];
                    console_box.setText(dataBuffer);
                }
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        baud_rate.getItems().addAll(baud);
        data_bits.getItems().addAll(dataBits);
        stop_bit.getItems().addAll(stopBits);
        parity_bits.getItems().addAll(parityBits);
        baud_rate.setValue(9600);
        data_bits.setValue(8);
        stop_bit.setValue(1.0);
        parity_bits.setValue("NO_PARITY");
        connection_status.setText("NOT CONNECTED");
        SerialPort []portList=SerialPort.getCommPorts();
        System.out.println("Testing Output");
        System.out.println(portList.length);
        for(SerialPort p:portList){
            System.out.println(p.getSystemPortName());
            port_array_list.add(p.getSystemPortName());
        }
        port_name.getItems().addAll(port_array_list);


    }

    public void onSendCommand(ActionEvent actionEvent) {
        outputStream=serialPortl.getOutputStream();
        String dataToSend="";
        dataToSend=command_box.getText();
        System.out.println(dataToSend);
        try{
            outputStream.write(dataToSend.getBytes());
        }catch(IOException e){
            System.out.println(e.getMessage());
        }


    }

    public void onClearConsole(ActionEvent actionEvent) {
        console_box.setText("");
    }
}