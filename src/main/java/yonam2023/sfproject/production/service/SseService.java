package yonam2023.sfproject.production.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import yonam2023.sfproject.production.domain.MachineData;
import yonam2023.sfproject.production.domain.Production;
import yonam2023.sfproject.production.repository.MachineDataRepository;
import yonam2023.sfproject.production.repository.ProductionRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class SseService {
    @Autowired
    ProductionRepository pr;
    @Autowired
    MachineDataRepository mr;

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
    //모든 이벤트를 통합할 지 고려
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

    public void updateServerState(String str){
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("server")
                        .data(str));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateMachineData(String str){
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("mdata")
                        .data(str));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void updateMachineState(String str){
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("mstate")
                        .data(str));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void updateMachineFatal(String str){
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("mfatal")
                        .data(str));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void updateMachineDetailState(String str){
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("mdstate")
                        .data(str));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void updateMachineDetailFatal(String str){
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

    public void updateMachineDetailGraph(int mid){

        MachineData machineData = mr.findByMid(mid);
        //graphData
        List<Production> graph = pr.findTop10ByMidOrderByIdDesc(mid);
        StringBuilder sbId = new StringBuilder();
        StringBuilder sbValues = new StringBuilder();
        sbId.append(mid+":");

        for(int i = graph.size()-1; i>=0; i--){
            Production p = graph.get(i);
            sbId.append(Long.toString(p.getId())+",");
            sbValues.append(p.getSvalue()+",");
        }
        //끝의 ,를 제거
        if(sbId.length()>0) sbId.deleteCharAt(sbId.length()-1);
        if(sbValues.length()>0) sbValues.deleteCharAt(sbValues.length()-1);

        sbId.append(":"+sbValues);

        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("updateGraph")
                        .data(sbId.toString()));//id,id,id:v,v,v 형태의 자료. 최대 10개
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateMachineDetailStock(String str){
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("mdstock")
                        .data(str));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}