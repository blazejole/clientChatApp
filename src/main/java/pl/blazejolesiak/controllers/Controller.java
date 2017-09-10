package pl.blazejolesiak.controllers;

import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import pl.blazejolesiak.DialogUtils;
import pl.blazejolesiak.models.ChatSocket;
import pl.blazejolesiak.models.IMessageObserver;
import pl.blazejolesiak.models.MessageFactory;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, IMessageObserver {

    @FXML
    TextArea yourMessageText;

    @FXML
    TextArea chatText;

    @FXML
    Button sendButton;
    

    public void initialize(URL location, ResourceBundle resources){
socket.connect();

sendNickPacket(DialogUtils.createNickDialog(null));

yourMessageText.requestFocus();
yourMessageText.setWrapText(true);
chatText.requestFocus();
chatText.setWrapText(true);
socket.setObserver(this);

        sendButton.setOnMouseClicked(s->{

            sendMessagePacket(yourMessageText.getText());
            yourMessageText.clear();
        });
    }


    private ChatSocket socket;
    private IMessageObserver iMessageObserver;

    public Controller(){
        socket = ChatSocket.getSocket();
    }


    public void handleMessage(String s) {
        Type token = new TypeToken<MessageFactory>() {
        }.getType();
        MessageFactory factory = MessageFactory.GSON.fromJson(s, token);

        switch (factory.getMessageType()) {
            case SEND_MESSAGE: {
                chatText.appendText("\n" + factory.getMessage());
                break;
            }
            case NICK_NOT_FREE: {
                Platform.runLater(() -> sendNickPacket(DialogUtils.createNickDialog(factory.getMessage())));
                break;
            }
            case USER_JOIN: {
                //todo
                chatText.appendText("\n"+"wchodzi na czat użytkownik:  ~~> " + factory.getMessage() + " <~~ ");
                break;
            }
            case USER_LEFT: {
                chatText.appendText("\n"+"wychodzi z czatu użytkownik:  <~~ " + factory.getMessage() + " ~~> ");
                break;
            }
            case GET_ALL_USERS:{
                //todo

            }
        }
    }

    private void sendNickPacket(String nick){
        MessageFactory factory = new MessageFactory();
        factory.setMessageType(MessageFactory.MessageType.SET_NICK);
        factory.setMessage(nick);
        sendMessage(factory);
    }

    private void sendMessagePacket(String message){
        MessageFactory factory = new MessageFactory();
        factory.setMessageType(MessageFactory.MessageType.SEND_MESSAGE);
        factory.setMessage(message);
        sendMessage(factory);
    }


    private void sendMessage(MessageFactory factory){
        socket.sendMessage(MessageFactory.GSON.toJson(factory));
    }
}
