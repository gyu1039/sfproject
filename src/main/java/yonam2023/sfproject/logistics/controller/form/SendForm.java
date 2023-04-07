package yonam2023.sfproject.logistics.controller.form;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import yonam2023.sfproject.logistics.domain.SendRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SendForm {

    @NoArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class Request {
        private String itemName;
        private int amount;
        @DateTimeFormat(pattern = "yyyy-MM-dd") // html에서 넘어온 Model의 String 타입을 알맞게 변환해줌.
        private LocalDate date;
        private String destination;

        /* Dto -> Entity */
        public SendRecord toEntity() {
            return new SendRecord(itemName, amount, destination,
                    date.atTime((int) (Math.random() * 24) + 1, (int) (Math.random() * 59) + 1));
        }
        public LocalDateTime getDateTime(){
            return date.atTime((int) (Math.random() * 24) + 1, (int) (Math.random() * 59) + 1);
        }
    }

}