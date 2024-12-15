package VPR41.demo.repository;


import VPR41.demo.models.StopInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StopInfoRepository extends JpaRepository<StopInfo, String> {
}