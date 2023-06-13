package yonam2023.sfproject.production.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import yonam2023.sfproject.production.sse.sendInterface.SendFatal;
import yonam2023.sfproject.production.sse.sendInterface.SendState;

import java.io.IOException;
import java.util.List;

public class DetailSender extends SseSender{

    @Override
    public void sendState(String str) {
        sendState.send(emitters, str);
    }

    @Override
    public void sendFatal(String str) {
        sendFatal.send(emitters, str);
    }

    public DetailSender(List<SseEmitter> emitters, SendFatal sendFatal, SendState sendState){
        this.emitters = emitters;
        this.sendFatal = sendFatal;
        this.sendState = sendState;
    }
}
