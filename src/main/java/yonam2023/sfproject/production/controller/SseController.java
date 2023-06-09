package yonam2023.sfproject.production.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import yonam2023.sfproject.production.sse.SseService;

import java.io.IOException;

@RestController
@RequestMapping("/sse")
public class SseController {

    private Logger logger = LoggerFactory.getLogger(SseController.class);

    @Autowired
    private SseService sseService;


    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect() {
        logger.info("SSEController:Receive Connect Request");
        SseEmitter emitter = new SseEmitter();
        sseService.add(emitter);

        try {
            //연결후에 아무런 데이터를 수신받지 못하면 503에러가 날 수 있으므로 던지는 connect 데이터
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(emitter);
    }
}
