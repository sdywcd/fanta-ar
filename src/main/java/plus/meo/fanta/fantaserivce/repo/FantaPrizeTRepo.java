package plus.meo.fanta.fantaserivce.repo;

import org.springframework.stereotype.Service;
import plus.meo.fanta.fantaserivce.entity.FantaPrizeT;

import java.util.List;

@Service
public interface FantaPrizeTRepo extends BaseJpaRepository<FantaPrizeT,Integer> {

    List<FantaPrizeT> findFantaPrizeTByUsertokenAndPtype(String userToken,String btype);

    List<FantaPrizeT>findFantaPrizeTByOpenid(String openid);

    FantaPrizeT findFantaPrizeTByUsertokenAndPrizeticket(String userToken,String ticket);

    FantaPrizeT findFantaPrizeTByOpenidAndPrizeticket(String openid,String ticket);
}
