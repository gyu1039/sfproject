package yonam2023.sfproject.production.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import yonam2023.sfproject.production.sse.sendInterface.SendFatal;
import yonam2023.sfproject.production.sse.sendInterface.SendState;

import java.util.List;

public abstract  class SseSender {
    protected List<SseEmitter> emitters;

    protected SendFatal sendFatal;

    protected SendState sendState;

    abstract public void sendState(String str);

    abstract public void sendFatal(String str);

}
