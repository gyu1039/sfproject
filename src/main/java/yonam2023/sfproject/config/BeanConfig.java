package yonam2023.sfproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yonam2023.sfproject.logistics.controller.proxyPattern.SendServiceProxy;
import yonam2023.sfproject.logistics.repository.SendRecordRepository;
import yonam2023.sfproject.logistics.repository.StoredItemRepository;
import yonam2023.sfproject.logistics.service.SendService;
import yonam2023.sfproject.notification.fcm.NotifyService;

@Configuration
public class BeanConfig {

    @Bean
    public SendServiceProxy sendService(SendRecordRepository sendRecordRepository, StoredItemRepository storedItemRepository, NotifyService notifyService){
        SendService target = new SendService(sendRecordRepository, storedItemRepository);
        return new SendServiceProxy(target, notifyService, sendRecordRepository);
    }

}
