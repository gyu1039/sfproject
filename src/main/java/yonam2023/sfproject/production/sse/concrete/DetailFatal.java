package yonam2023.sfproject.production.sse.concrete;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import yonam2023.sfproject.production.sse.sendInterface.SendFatal;

import java.io.IOException;
import java.util.List;

public class DetailFatal implements SendFatal {
    @Override
    public void send(List<SseEmitter> emitters, String str) {
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("mdfatal")
                        .data(str));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
