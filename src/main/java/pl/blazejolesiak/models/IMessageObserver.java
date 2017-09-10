package pl.blazejolesiak.models;

import java.util.List;

public interface IMessageObserver {
    void handleMessage(String s);
   // List<String> loadAllNicks();
}
