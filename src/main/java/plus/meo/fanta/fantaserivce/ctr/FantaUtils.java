package plus.meo.fanta.fantaserivce.ctr;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import plus.meo.fanta.fantaserivce.redis.RedisKeys;
import plus.meo.fanta.fantaserivce.redis.RedisUtils;

import java.util.concurrent.TimeUnit;

@Component
public class FantaUtils {

    private final static Logger logger= LoggerFactory.getLogger(FantaUtils.class);
    private static String username="eric.xuan@ogilvy.com";
    private static String password="Q$UTNShwBX7q8T%1"; //Q$UTNShwBX7q8T%1   //@rquHFZO46zYi2f!

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisUtils redisUtils;
    /**
     * 获取芬达业务token
     * @return
     */
    public String getFantaToken(){
        String token= StringUtils.EMPTY;
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        JSONObject param = new JSONObject();
        param.put("username", username);
        param.put("password", password);
        HttpEntity formEntity = new HttpEntity(param, headers);
        String result = restTemplate.postForObject(FantaConfig.KO_RELEASEURL+FantaConfig.FANTATOKEN_URL, formEntity, String.class);

        logger.info("body=="+result);
        JSONObject jsonObject=JSONObject.parseObject(result);
        JSONObject data=jsonObject.getJSONObject("data");
        token =data.getString("token");
        if (token!=null){
            redisUtils.set(RedisKeys.FANTATOKENKEY,token,7200, TimeUnit.SECONDS);
        }
        return token;
    }


    /**
     * 获取芬达小程序token 从h5进入
     * @return
     */
    public String getFantaH5TokenInApp(String code){
        String token= StringUtils.EMPTY;
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        JSONObject param = new JSONObject();
        param.put("code", code);
        HttpEntity formEntity = new HttpEntity(param, headers);
        String result = restTemplate.postForObject(FantaConfig.KO_RELEASEURL+FantaConfig.FANTA_H5_TOKEN_URL, formEntity, String.class);
        JSONObject jsonObject=JSONObject.parseObject(result);
        JSONObject data=jsonObject.getJSONObject("data");
        token =data.getString("token");
        return token;
    }

}
