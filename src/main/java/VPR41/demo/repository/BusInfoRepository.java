package VPR41.demo.repository;


import VPR41.demo.models.BusInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusInfoRepository extends JpaRepository<BusInfo, Long> {
    List<BusInfo> findByBusNumber(String busNumber);
}
