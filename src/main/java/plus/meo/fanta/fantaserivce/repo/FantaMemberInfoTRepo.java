package plus.meo.fanta.fantaserivce.repo;

import org.springframework.stereotype.Service;
import plus.meo.fanta.fantaserivce.entity.FantaMemberInfoT;

@Service
public interface FantaMemberInfoTRepo extends BaseJpaRepository<FantaMemberInfoT,Integer>{

    FantaMemberInfoT findFantaMemberInfoTByOpenid(String openid);

    FantaMemberInfoT findFantaMemberInfoTByUsertoken(String usertoken);
}
