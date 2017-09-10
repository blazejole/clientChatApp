package pl.blazejolesiak.models;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


@ClientEndpoint // do socketow zapamietac dodac trzeba
    public class ChatSocket {//singleton

    private static ChatSocket socket = new ChatSocket();

    public static ChatSocket getSocket(){
        return socket;
    }


    private IMessageObserver observer;

    public IMessageObserver getObserver() {
        return observer;
    }

    public void setObserver(IMessageObserver observer) {
        this.observer = observer;
    }

    private WebSocketContainer webSocketContainer;
    private Session session;
    private  ChatSocket(){
        webSocketContainer = ContainerProvider.getWebSocketContainer();
    }


    @OnOpen
    public void open(Session session){
        this.session = session;
        System.out.println("Polaczono");
    }

    @OnMessage
    public void message(Session session, String message){
        observer.handleMessage(message);
    }

    public void sendMessage(String message){
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(){
        try {
            webSocketContainer.connectToServer(this, new URI("ws://localhost:8080/chat"));
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
