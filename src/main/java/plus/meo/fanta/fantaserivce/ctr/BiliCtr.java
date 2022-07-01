package plus.meo.fanta.fantaserivce.ctr;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import plus.meo.fanta.fantaserivce.ctr.vo.FantaPrizeTVo;
import plus.meo.fanta.fantaserivce.entity.FantaMemberInfoT;
import plus.meo.fanta.fantaserivce.entity.FantaPrizeT;
import plus.meo.fanta.fantaserivce.redis.RedisKeys;
import plus.meo.fanta.fantaserivce.redis.RedisUtils;
import plus.meo.fanta.fantaserivce.repo.FantaMemberInfoTRepo;
import plus.meo.fanta.fantaserivce.repo.FantaPrizeTRepo;
import plus.meo.fanta.fantaserivce.utils.DateTools;
import plus.meo.fanta.fantaserivce.utils.gson.GsonJson;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/api/fanta/bilibili")
public class BiliCtr {
    private final static Logger logger= LoggerFactory.getLogger(BiliCtr.class);
    private static String key="lKjpYy96qI";

     @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FantaUtils fantaUtils;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private FantaPrizeTRepo fantaPrizeTRepo;

    @Autowired
    private FantaMemberInfoTRepo fantaMemberInfoTRepo;

    // Tvo tvo=GsonJson.parsejsonToObject(wholeStr.toString(),Tvo.class);
    @PostMapping(value = "/sign1")
    public ResponseEntity<Map>sign1(HttpServletRequest request){
        String path="/fanta-service/api/fanta/bilibili/findUserPrizeList";
//        String path="/fanta-service/api/fanta/bilibili/reveivedBenefits";
        Map<String,Object>data= Maps.newHashMap();
//        String openid="ox3-v4qOlVmiauX_1cGnfHn18SUo";
//        String sopenid=SHA1.getSHA1StrJava(openid);
//        data.put("userToken",sopenid);
        String str= StringUtils.EMPTY;
        BufferedReader br = null;
        try {
            br = request.getReader();
            StringBuilder rStr = new StringBuilder();
            while ((str = Objects.requireNonNull(br).readLine()) != null) {
                rStr.append(str);
            }
            String sign=H256.getSHA256StrJava(path+rStr+key);
            data.put("sign",sign);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(data);
    }

    @PostMapping(value = "/sign2")
    public ResponseEntity<Map>sign2(HttpServletRequest request){
//        String path="/fanta-service/api/fanta/bilibili/findUserPrizeList";
        String path="/fanta-service/api/fanta/bilibili/reveivedBenefits";
        Map<String,Object>data= Maps.newHashMap();
//        String openid="ox3-v4qOlVmiauX_1cGnfHn18SUo";
//        String sopenid=SHA1.getSHA1StrJava(openid);
//        data.put("userToken",sopenid);
        String str= StringUtils.EMPTY;
        BufferedReader br = null;
        try {
            br = request.getReader();
            StringBuilder rStr = new StringBuilder();
            while ((str = Objects.requireNonNull(br).readLine()) != null) {
                rStr.append(str);
            }
            String sign=H256.getSHA256StrJava(path+rStr+key);
            data.put("sign",sign);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(data);
    }





    //同步奖品是否中奖核销的信息到CEP

    //根据b站给过来的openid查询用户的奖品列表
    /**
     * 提供给B站的查询用户的获奖列表
     * @param request
     * @return
     */
    @PostMapping(value = "/findUserPrizeList")
    public ResponseEntity<Map>findUserPrizeList(HttpServletRequest request){
        Map<String,Object>data= Maps.newHashMap();
        String path="/fanta-service/api/fanta/bilibili/findUserPrizeList";
        Map<String,Object>fdata=this.validateSign(path,request);
        boolean validate= (boolean) fdata.get("validate");
        if (validate){
            //读取参数
            String params= (String) fdata.get("params");
            FindPrizeVo vo= GsonJson.parsejsonToObject(params,FindPrizeVo.class);
            if (vo!=null){
                List<FantaPrizeTVo>vos=new ArrayList<>();
                List<FantaPrizeT> list=this.fantaPrizeTRepo.findFantaPrizeTByUsertokenAndPtype(vo.getUserToken(),"bilibili");
                for (FantaPrizeT fantaPrizeT:list){
                    FantaPrizeTVo f=new FantaPrizeTVo();
                    f.setUserToken(fantaPrizeT.getUsertoken());
                    f.setPrizeId(fantaPrizeT.getPrizeid());
                    f.setPrizeName(fantaPrizeT.getPrizename());
                    f.setPrizeTime(fantaPrizeT.getPrizetime());
                    f.setPrizeAcquireId(fantaPrizeT.getPrizeticket());
                    f.setPrizeStatus(fantaPrizeT.getPrizestatus());
                    vos.add(f);
                }
                data.put("pirzeList",vos);
                data.put("code",200);
            }else{
                data.put("code",400);
                data.put("msg","userToken获取失败");
            }
        }else{
            //返回参数检查失败
            data.put("code",400);
            data.put("msg","签名失败");
        }
        return ResponseEntity.ok(data);
    }


    @PostMapping(value = "/test1")
    public ResponseEntity<Map>test1(HttpServletRequest request){
        Map<String,Object>data= Maps.newHashMap();
        String path="/fanta-service/api/fanta/bilibili/findUserPrizeList";
        JSONObject param=new JSONObject();
        param.put("userToken","5ff991f94681071e4d425020f2a9dca634c36bd4");
        param.put("prizeAcquireId","62788184e4b04f77f53ec8ef");
        String sign=H256.getSHA256StrJava(path+param.toJSONString()+key);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type",  MediaType.APPLICATION_JSON.toString());
        headers.add("sign",sign);
        HttpEntity formEntity = new HttpEntity(param, headers);
        String result = restTemplate.postForObject("https://mg.meo.plus/fanta-service/api/fanta/bilibili/findUserPrizeList", formEntity, String.class);
        data.put("rest",result);
//        String signStr=FantaConfig.FANTA_BIND_BENEFITS+param.toJSONString()+FantaConfig.SKEY;
//        String signKey=H256.getSHA256StrJava(sign);
        data.put("sign",sign);
        return ResponseEntity.ok(data);
    }

    @PostMapping(value = "/test2")
    public ResponseEntity<Map>test2(HttpServletRequest request){
        Map<String,Object>data= Maps.newHashMap();
        String path="/fanta-service/api/fanta/bilibili/receivedBenefits";
        JSONObject param=new JSONObject();
        param.put("userToken","5ff991f94681071e4d425020f2a9dca634c36bd4");
        param.put("prizeAcquireId","62788184e4b04f77f53ec8ef");
        String sign=H256.getSHA256StrJava(path+param.toJSONString()+key);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type",  MediaType.APPLICATION_JSON.toString());
        headers.add("sign",sign);
        HttpEntity formEntity = new HttpEntity(param, headers);
        String result = restTemplate.postForObject("https://mg.meo.plus/fanta-service/api/fanta/bilibili/receivedBenefits", formEntity, String.class);
        data.put("rest",result);
//        String signStr=FantaConfig.FANTA_BIND_BENEFITS+param.toJSONString()+FantaConfig.SKEY;
//        String signKey=H256.getSHA256StrJava(sign);
        data.put("sign",sign);
        return ResponseEntity.ok(data);
    }

    /**
     * 已经领取权益
     * @param request
     * @return
     */
    @PostMapping(value = "/receivedBenefits")
    public ResponseEntity<Map>receivedBenefits(HttpServletRequest request){
        Map<String,Object>data= Maps.newHashMap();
        String path="/fanta-service/api/fanta/bilibili/receivedBenefits";
        Map<String,Object>fdata=this.validateSign(path,request);
        boolean validate= (boolean) fdata.get("validate");
        if (validate){
            //读取参数
            String params= (String) fdata.get("params");
            ReceivedBenefitVo vo=GsonJson.parsejsonToObject(params,ReceivedBenefitVo.class);
            if (vo!=null){
                //查询系统内的用户uid
                FantaMemberInfoT memberInfoT=this.fantaMemberInfoTRepo.findFantaMemberInfoTByUsertoken(vo.getUserToken());
                if (memberInfoT!=null){
                    //请求CEP绑定B站的权益
                    Object token=this.redisUtils.get(RedisKeys.FANTATOKENKEY);
                    if (token==null){
                        token=this.fantaUtils.getFantaToken();
                    }
                    JSONObject param=new JSONObject();
                    param.put("uid", memberInfoT.getMid());
                    param.put("accountType","OCP_ID");//CRM_ID OCP_ID
                    param.put("benefitCode",FantaConfig.BENEFITCODE);//权益编码需要获取
                    param.put("activityCode",FantaConfig.COMPAIGNCODE);
                    param.put("acquireTime", DateTools.formateLongDateToString(DateTools.getSystemTimeLong()));
                    param.put("acquireId",vo.getPrizeAcquireId());
                    param.put("status","RECEIVED");//已领取
                    String signStr=FantaConfig.FANTA_BIND_BENEFITS+param.toJSONString()+FantaConfig.SKEY;
                    String signKey=H256.getSHA256StrJava(signStr);
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Content-Type",  MediaType.APPLICATION_JSON.toString());
                    headers.add("Authorization", token.toString());
                    headers.add("X-TW-SIGNATURE", signKey);
                    //HttpEntity formEntity = new HttpEntity(param, headers);
                    //ResponseEntity<String> result = restTemplate.postForEntity(RELEASEURL+FANTA_DRAW_URL, formEntity, String.class);
                    //TODO
                    FantaPrizeT fantaPrizeT=this.fantaPrizeTRepo.findFantaPrizeTByUsertokenAndPrizeticket(vo.getUserToken(),vo.getPrizeAcquireId());
                    if (fantaPrizeT!=null){
                        fantaPrizeT.setPrizestatus("received");
                        fantaPrizeT.setUpdatetime(DateTools.getSystemTimeLong());
                        fantaPrizeT.setVersiont(fantaPrizeT.getVersiont()+1);
                        this.fantaPrizeTRepo.save(fantaPrizeT);
                        data.put("code",200);
                        data.put("msg","success");
                    }else{
                        data.put("code",400);
                        data.put("msg","数据异常");
                    }
                }else{
                    data.put("code",400);
                    data.put("msg","数据异常");
                }
            }else{
                data.put("code",400);
                data.put("msg","请求参数获取异常");
            }
        }else {
            //返回参数检查失败
            data.put("code", 400);
            data.put("msg", "签名失败");
        }
        return ResponseEntity.ok(data);
    }










    private Map<String,Object> validateSign(String path,HttpServletRequest request)  {
        Map<String,Object>data= Maps.newHashMap();
        String sign=request.getHeader("sign");
        String str= StringUtils.EMPTY;
        BufferedReader br = null;
        try {
            br = request.getReader();
            StringBuilder rStr = new StringBuilder();
            while ((str = Objects.requireNonNull(br).readLine()) != null) {
                rStr.append(str);
            }
            String signKey=H256.getSHA256StrJava(path+rStr+key);
            if (StringUtils.equals(signKey,sign)){
                data.put("validate",true);
                data.put("params",rStr.toString());
            }else{
                data.put("validate",false);
                data.put("params",StringUtils.EMPTY);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


}
