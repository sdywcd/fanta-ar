package plus.meo.fanta.fantaserivce.repo;

import org.springframework.stereotype.Service;
import plus.meo.fanta.fantaserivce.entity.CurrentStoreInfoT;

@Service
public interface CurrentStoreInfoTRepo extends BaseJpaRepository<CurrentStoreInfoT,Integer>{

    CurrentStoreInfoT findCurrentStoreInfoTByCityname(String cityName);



}
