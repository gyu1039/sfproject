package yonam2023.sfproject.production.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class SseEmitters {
    //브라우저 연결 데이터를 저장하는 객체
    //멀티스레딩에 안전한 리스트 형식이어야함.
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter add(SseEmitter emitter) {
        this.emitters.add(emitter);
        log.info("new emitter added: {}", emitter);
        log.info("emitter list size: {}", emitters.size());
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            this.emitters.remove(emitter);    // 만료되면 리스트에서 삭제
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });

        return emitter;
    }
    public void call() {
        //모든 등록한 브라우저에 called 데이터를 전송
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("called")
                        .data("yay"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}