package yonam2023.sfproject.logistics.controller.proxyPattern;

import yonam2023.sfproject.employee.domain.DepartmentType;
import yonam2023.sfproject.logistics.controller.form.SendForm;
import yonam2023.sfproject.logistics.domain.SendRecord;
import yonam2023.sfproject.logistics.repository.SendRecordRepository;
import yonam2023.sfproject.logistics.service.SendService;
import yonam2023.sfproject.notification.fcm.NotifyService;

// 이 프록시 객체는 yonam2023.sfproject.config.BeanConfig에서 수동 빈 등록을 하고 있다.
public class SendServiceProxy extends SendService {

    private final SendService target;
    private final NotifyService notifyService;
    private final SendRecordRepository sendRecordRepository;

    public SendServiceProxy(SendService target, NotifyService notifyService, SendRecordRepository sendRecordRepository){
        super(null, null);
        this.target = target;
        this.notifyService = notifyService;
        this.sendRecordRepository = sendRecordRepository;
    }

    @Override
    public Long saveSendRecord(SendForm.Request sendReqForm) {
        Long result = target.saveSendRecord(sendReqForm);
        notifyService.departmentNotify(DepartmentType.LOGISTICS,"[출고 예약 알림]", sendReqForm.getItemName()+"의 출고가 예약되었습니다!");
        return result;
    }

    @Override
    public void editSendRecord(long id, SendForm.Request sendReqForm) {
        target.editSendRecord(id, sendReqForm);
        notifyService.departmentNotify(DepartmentType.LOGISTICS,"[출고 예약 수정 알림]", sendReqForm.getItemName()+"의 출고 예약이 수정되었습니다!");
    }

    @Override
    public void deleteSendRecord(long recordId) {
        SendRecord sendRecord = sendRecordRepository.findById(recordId).orElse(null);
        if (sendRecord != null) {
            target.deleteSendRecord(recordId);
            notifyService.departmentNotify(DepartmentType.LOGISTICS, "[출고 취소 알림]", sendRecord.getItemName() + "의 출고 예약이 취소되었습니다!");
        }
    }
}
