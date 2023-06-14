package yonam2023.sfproject.production.sse.sendInterface;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface SendState {
    public void send(List<SseEmitter> emitters, String str);
}
