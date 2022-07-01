package plus.meo.fanta.fantaserivce.repo;

import org.springframework.stereotype.Service;
import plus.meo.fanta.fantaserivce.entity.AllStoreInfoT;

import java.util.List;

@Service
public interface AllStoreInfoTRepo extends BaseJpaRepository<AllStoreInfoT,Integer> {
    AllStoreInfoT findAllStoreInfoTByCityname(String cityName);


}
