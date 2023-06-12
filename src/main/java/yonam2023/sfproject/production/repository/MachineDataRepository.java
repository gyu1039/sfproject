package yonam2023.sfproject.production.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yonam2023.sfproject.production.domain.MachineData;

public interface MachineDataRepository  extends JpaRepository<MachineData, Long> {
    public MachineData findByMachineId(int machineId);
}
