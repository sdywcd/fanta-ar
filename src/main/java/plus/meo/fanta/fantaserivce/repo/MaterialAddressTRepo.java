package plus.meo.fanta.fantaserivce.repo;

import org.springframework.stereotype.Service;
import plus.meo.fanta.fantaserivce.entity.MaterialAddressT;

@Service
public interface MaterialAddressTRepo extends BaseJpaRepository<MaterialAddressT, Integer>{

    MaterialAddressT findMaterialAddressTByFantaprizeid(int id);

}
