package plus.meo.fanta.fantaserivce.ctr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import plus.meo.fanta.fantaserivce.ctr.vo.*;
import plus.meo.fanta.fantaserivce.entity.*;
import plus.meo.fanta.fantaserivce.redis.RedisKeys;
import plus.meo.fanta.fantaserivce.redis.RedisUtils;
import plus.meo.fanta.fantaserivce.repo.*;
import plus.meo.fanta.fantaserivce.utils.DateTools;
import plus.meo.fanta.fantaserivce.utils.PrimaryKey;
import plus.meo.fanta.fantaserivce.utils.gson.GsonJson;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

import static plus.meo.fanta.fantaserivce.ctr.FantaConfig.*;

@RestController
@RequestMapping("/api/fanta")
public class IndexCtr {

    private final static Logger logger= LoggerFactory.getLogger(IndexCtr.class);


    /**
     * 用户信息获取地址
     */
    private static String FANTA_USER_PROFILE_URL="/backend/open-api/v1/users/profile";

    @Autowired
    private CurrentStoreInfoTRepo currentStoreInfoTRepo;
    @Autowired
    private AllStoreInfoTRepo allStoreInfoTRepo;
    @Autowired
    private FantaUtils fantaUtils;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private FantaMemberInfoTRepo fantaMemberInfoTRepo;

    @Autowired
    private FantaPrizeTRepo fantaPrizeTRepo;

    @Autowired
    private MaterialAddressTRepo materialAddressTRepo;




    @PostMapping(value = "getFantaH5Token4App")
    public ResponseEntity<Map>getFantaH5Token4App(@RequestParam("code")String code){
        Map<String,Object>data= Maps.newHashMap();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        JSONObject param = new JSONObject();
        param.put("code", code);
        HttpEntity formEntity = new HttpEntity(param, headers);
        String result = restTemplate.postForObject(FantaConfig.KO_RELEASEURL+FANTA_H5_TOKEN_URL, formEntity, String.class);
        JSONObject jsonObject=JSONObject.parseObject(result);
        JSONObject data1=jsonObject.getJSONObject("data");
        data.put("token",data1.getString("token"));
        data.put("sucflag",true);
        return ResponseEntity.ok(data);
    }

    /**
     * 获取用户信息
     * @param code
     * @return
     * @throws IOException
     */
    @GetMapping(value = "getUserProfile")
    public ResponseEntity<Map>getUserProfile(@RequestParam("code")String code) throws IOException {
        Map<String,Object>data= Maps.newHashMap();
        if (StringUtils.isNotBlank(code)){
            String token=this.fantaUtils.getFantaH5TokenInApp(code);
            if (!token.isEmpty()){
                //获取用户信息
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");
                headers.add("Authorization", token);
                HttpEntity<String>stringHttpEntity=new HttpEntity<>(headers);
                ResponseEntity<String> result = restTemplate.exchange(FantaConfig.KO_RELEASEURL+FANTA_USER_PROFILE_URL, HttpMethod.GET,stringHttpEntity,String.class);
                logger.info("body=="+result);
                JSONObject jsonObject=JSONObject.parseObject(result.getBody());
                JSONObject jdata=jsonObject.getJSONObject("data");
                String openId=jdata.getString("openId");
                FantaMemberInfoT fantaMemberInfoT=this.fantaMemberInfoTRepo.findFantaMemberInfoTByOpenid(openId);
                if (fantaMemberInfoT==null){
                    String unionid=jdata.getString("unionId");
                    String phoneNumber= jdata.getString("phoneNumber");
                    String nickName= jdata.getString("nickName");
                    String avatarUrl= jdata.getString("avatarUrl");
                    String country= jdata.getString("country");
                    String province= jdata.getString("province");
                    String city= jdata.getString("city");
                    String id= jdata.getString("id");
                    String language= jdata.getString("language");
                    String membershipId= jdata.getString("membershipId");
                    int gender=jdata.getInteger("gender");
                    fantaMemberInfoT=new FantaMemberInfoT();
                    fantaMemberInfoT.setOpenid(openId);
                    fantaMemberInfoT.setUnionid(unionid);
                    fantaMemberInfoT.setMembershipid(membershipId);
                    fantaMemberInfoT.setCreatetime(DateTools.getSystemTimeLong());
                    fantaMemberInfoT.setUpdatetime(DateTools.getSystemTimeLong());
                    fantaMemberInfoT.setStatus("1");
                    fantaMemberInfoT.setVersiont(1);
                    fantaMemberInfoT.setMid(id);
                    fantaMemberInfoT.setNickname(nickName);
                    fantaMemberInfoT.setAvatarurl(avatarUrl);
                    fantaMemberInfoT.setGender(gender);
                    fantaMemberInfoT.setCountry(country);
                    fantaMemberInfoT.setProvince(province);
                    fantaMemberInfoT.setLanguage(language);
                    fantaMemberInfoT.setPhonenumber(phoneNumber);
                    fantaMemberInfoT.setCity(city);
                    fantaMemberInfoT.setUsertoken(SHA1.getSHA1StrJava(fantaMemberInfoT.getMid()));
                    this.fantaMemberInfoTRepo.save(fantaMemberInfoT);
                }
                data.put("openid",fantaMemberInfoT.getOpenid());
                data.put("userToken",fantaMemberInfoT.getUsertoken());
                data.put("avatar",fantaMemberInfoT.getAvatarurl());
                data.put("nickname",fantaMemberInfoT.getNickname());
                data.put("mid",fantaMemberInfoT.getMid());
                data.put("sucflag",true);
                data.put("msg","ok");
            }
        }else{
            data.put("sucflag",false);
            data.put("msg","code参数错误");
        }
        return ResponseEntity.ok(data);
    }



    /**
     * 执行抽奖
     * @return
     */
    @Transactional
    @PostMapping(value = "doLuck")
    public ResponseEntity<Map>doLuck(@RequestBody VoRq rq){
        Map<String,Object>data= Maps.newHashMap();
        Object token=this.redisUtils.get(RedisKeys.FANTATOKENKEY);
        if (token==null){
            token=this.fantaUtils.getFantaToken();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type",  MediaType.APPLICATION_JSON.toString());
        headers.add("Authorization", token.toString());
        JSONObject param = new JSONObject();
        param.put("ticket", DateTools.getSystemTimeLong()+"");
        param.put("userIdType","OPEN_ID");
        param.put("userId",rq.getMid());
        param.put("campaignCode",FantaConfig.COMPAIGNCODE);
        if (StringUtils.isNotBlank(rq.getJackpotid())){
            param.put("campaignCode",FantaConfig.COMPAIGNCODE+"_"+rq.getJackpotid());
        }else{
            param.put("campaignCode",FantaConfig.COMPAIGNCODE);
        }
        HttpEntity formEntity = new HttpEntity(param, headers);
        String result=StringUtils.EMPTY;
        //TODO  需要正式对接到UAT的发券 根据情况去走是否要调用权益绑定
        //String result = restTemplate.postForObject(RELEASEURL+FANTA_DRAW_URL, formEntity, String.class);
        //ResponseEntity<String> result = restTemplate.postForEntity(RELEASEURL+FANTA_DRAW_URL, formEntity, String.class);
//        logger.info("body=="+result);

        FantaPrizeT bean=this.savePrizeToDb(result,rq.getMid());
        if (bean!=null){
            if (bean.getPrizecode().equals("1")){
                //中奖
                data.put("openid",bean.getOpenid());
                data.put("prizeCode",bean.getPrizecode());
                data.put("userToken",bean.getUsertoken());
                data.put("prizeId",bean.getPrizeid());
                data.put("prizeName",bean.getPrizename());
                data.put("prizeTime",bean.getPrizetime());
                data.put("prizeAcquireId",bean.getPrizeticket());
                data.put("prizeStatus",bean.getPrizestatus());
                data.put("prizeType",bean.getPtype());
                data.put("isMaterial",bean.getIsmaterial());
            }else{
                data.put("openid",StringUtils.EMPTY);
                data.put("prizeCode",bean.getPrizecode());
                data.put("userToken",StringUtils.EMPTY);
                data.put("prizeId",StringUtils.EMPTY);
                data.put("prizeName",StringUtils.EMPTY);
                data.put("prizeTime",StringUtils.EMPTY);
                data.put("prizeAcquireId",StringUtils.EMPTY);
                data.put("prizeStatus",StringUtils.EMPTY);
                data.put("prizeType",StringUtils.EMPTY);
                data.put("isMaterial",StringUtils.EMPTY);
            }
            data.put("sucflag",true);
            data.put("msg","ok");
        }else{
            data.put("sucflag",false);
            data.put("msg","奖品信息获取失败，请稍后再试");
        }
        return ResponseEntity.ok(data);
    }

    /**
     * 把用户的中奖信息保存到本地数据库
     */
    private FantaPrizeT savePrizeToDb(String result,String openid){
        FantaPrizeT bean=null;
//        JSONObject jsonObject=JSONObject.parseObject(result);
//        JSONObject jdata=jsonObject.getJSONObject("data");
//        String prizeNumber= jdata.getInteger("id")+"";
//        String prizeCode=jdata.getString("code");
//        String prizeTime=jdata.getString("time");
//        String prizeTicket=jdata.getString("ticket");
//        String prizeId=jdata.getString("prizeId");
//        String prizeName=jdata.getString("prizeName");
        String prizeId="bilibili001";
        String prizeName="Bilibili大会员";
        String prizeCode="1";
        String prizeNumber="84054380525056000";
        String prizeStatus="pending";
        String pType="bilibili";//如果是B站的就是pending，如果是KO的就是直接已经领取的，会调用CEP直接发
        if (pType.equals("ko")){
            prizeStatus="received";
        }
        int isMaterial=1;//实物

        //查询下memberid
        FantaMemberInfoT fantaMemberInfoT=this.fantaMemberInfoTRepo.findFantaMemberInfoTByOpenid(openid);
        if (fantaMemberInfoT!=null){
            bean=new FantaPrizeT();
            bean.setOpenid(openid);
            bean.setUnionid(fantaMemberInfoT.getUnionid());
            bean.setMembershipid(fantaMemberInfoT.getMembershipid());
            bean.setCreatetime(DateTools.getSystemTimeLong());
            bean.setUpdatetime(DateTools.getSystemTimeLong());
            bean.setStatus("1");
            bean.setVersiont(1);
            bean.setMid(fantaMemberInfoT.getMid());
            bean.setPrizeid(prizeId);
            bean.setPrizename(prizeName);
            bean.setPrizetime(DateTools.formateLongDateToString(DateTools.getSystemTimeLong()));
            bean.setPrizeticket(PrimaryKey.get().toString());
            bean.setPrizecode(prizeCode);
            bean.setPrizenumber(prizeNumber);
            bean.setUsertoken(fantaMemberInfoT.getUsertoken());
            bean.setPtype(pType);
            bean.setIsmaterial(isMaterial);//0 不是实物 1 是实物
            bean.setPrizestatus(prizeStatus);
            this.fantaPrizeTRepo.save(bean);

            boolean a=true;
            if (a){
                //需要进行权益绑定
                this.pendingBenefits(bean);
            }


        }
        return bean;

    }

    private boolean pendingBenefits(FantaPrizeT fantaPrizeT){
        //请求CEP绑定B站的权益
        Object token=this.redisUtils.get(RedisKeys.FANTATOKENKEY);
        if (token==null){
            token=this.fantaUtils.getFantaToken();
        }
        JSONObject param=new JSONObject();
        param.put("uid", fantaPrizeT.getMid());
        param.put("accountType","OCP_ID");//CRM_ID OCP_ID
        param.put("benefitCode",FantaConfig.BENEFITCODE);//权益编码需要获取
        param.put("activityCode",FantaConfig.COMPAIGNCODE);
        param.put("acquireTime", DateTools.formateLongDateToString(DateTools.getSystemTimeLong()));
        param.put("acquireId",fantaPrizeT.getPrizeticket());
        param.put("status","PENDING");//待领取
        String signStr=FantaConfig.FANTA_BIND_BENEFITS+param.toJSONString()+FantaConfig.SKEY;
        String signKey=H256.getSHA256StrJava(signStr);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type",  MediaType.APPLICATION_JSON.toString());
        headers.add("Authorization", token.toString());
        headers.add("X-TW-SIGNATURE", signKey);
        //TODO
        return true;
    }

    /**
     * 获取奖品列表
     * @param rq
     * @return
     */
    @PostMapping(value = "/findUserPrizeList")
    public ResponseEntity<Map>findUserPrizeList(@RequestBody VoRq rq){
        Map<String,Object>data= Maps.newHashMap();
        List<FantaPrizeTVo>vos=new ArrayList<>();
        List<FantaPrizeT> list=this.fantaPrizeTRepo.findFantaPrizeTByOpenid(rq.getOpenid());
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
        data.put("sucflag",true);
        data.put("msg","ok");
        return ResponseEntity.ok(data);
    }



    //实物奖品需要填写收获地址信息并保存，然后通知到CEP进行权益的领取

    /**
     * 用户领取实物奖品
     * @return
     */
    @PostMapping(value = "/receivedMaterialPrize")
    public ResponseEntity<Map>receivedMaterialPrize(@RequestBody MaterialPrizeVo rq){
        Map<String,Object>data= Maps.newHashMap();
//        Map<String,Object>fdata=this.getParams(request);
        //读取参数
//        String params= (String) fdata.get("params");
//        MaterialPrizeVo materialPrizeVo= GsonJson.parsejsonToObject(params,MaterialPrizeVo.class);
//        if (materialPrizeVo!=null){
//            ShippingVo shippingVo=new ShippingVo();
//            shippingVo.setProvince(materialPrizeVo.getProvince());
//            shippingVo.setCity(materialPrizeVo.getCity());
//            shippingVo.setArea(materialPrizeVo.getArea());
//            shippingVo.setAddress(materialPrizeVo.getAddress());
//            shippingVo.setName(materialPrizeVo.getName());
//            shippingVo.setPhoneNumber(materialPrizeVo.getPhoneNumber());
//
//            //请求CEP绑定B站的权益
//            Object token=this.redisUtils.get(RedisKeys.FANTATOKENKEY);
//            if (token==null){
//                token=this.fantaUtils.getFantaToken();
//            }
//            JSONObject param=new JSONObject();
//            param.put("uid", materialPrizeVo.getMid());
//            param.put("accountType","OCP_ID");//CRM_ID OCP_ID
//            param.put("benefitCode",FantaConfig.BENEFITCODE);//权益编码需要获取
//            param.put("activityCode",FantaConfig.COMPAIGNCODE);
//            param.put("acquireTime", DateTools.formateLongDateToString(DateTools.getSystemTimeLong()));
//            param.put("acquireId",materialPrizeVo.getPrizeAcquireId());
//            param.put("status","RECEIVED");//已领取
//            param.put("extra",GsonJson.parseDataToJson(shippingVo));
//            String signStr=FantaConfig.FANTA_BIND_BENEFITS+param.toJSONString()+FantaConfig.SKEY;
//            String signKey=H256.getSHA256StrJava(signStr);
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Type",  MediaType.APPLICATION_JSON.toString());
//            headers.add("Authorization", token.toString());
//            headers.add("X-TW-SIGNATURE", signKey);
            //TODO
            FantaPrizeT fantaPrizeT=this.fantaPrizeTRepo.findFantaPrizeTByOpenidAndPrizeticket(rq.getMid(),rq.getPrizeAcquireId());
            if (fantaPrizeT!=null){
                fantaPrizeT.setPrizestatus("received");
                fantaPrizeT.setUpdatetime(DateTools.getSystemTimeLong());
                fantaPrizeT.setVersiont(fantaPrizeT.getVersiont()+1);
                this.fantaPrizeTRepo.save(fantaPrizeT);
                MaterialAddressT materialAddressT=this.materialAddressTRepo.findMaterialAddressTByFantaprizeid(fantaPrizeT.getId());
                if (materialAddressT==null){
                    materialAddressT=new MaterialAddressT();
                    materialAddressT.setProvince(rq.getProvince());
                    materialAddressT.setCity(rq.getCity());
                    materialAddressT.setArea(rq.getArea());
                    materialAddressT.setAddress(rq.getAddress());
                    materialAddressT.setName(rq.getName());
                    materialAddressT.setPhonenumber(rq.getPhoneNumber());
                    materialAddressT.setIssync(0);
                    materialAddressT.setFantaprizeid(fantaPrizeT.getId());
                    materialAddressT.setCreatetime(DateTools.getSystemTimeLong());
                    materialAddressT.setUpdatetime(DateTools.getSystemTimeLong());
                    materialAddressT.setVersiont(1);
                    materialAddressT.setStatus("1");
                    this.materialAddressTRepo.save(materialAddressT);
                }else{
                    materialAddressT.setProvince(rq.getProvince());
                    materialAddressT.setCity(rq.getCity());
                    materialAddressT.setArea(rq.getArea());
                    materialAddressT.setAddress(rq.getAddress());
                    materialAddressT.setName(rq.getName());
                    materialAddressT.setPhonenumber(rq.getPhoneNumber());
                    materialAddressT.setUpdatetime(DateTools.getSystemTimeLong());
                    materialAddressT.setVersiont(materialAddressT.getVersiont()+1);
                    this.materialAddressTRepo.save(materialAddressT);
                }
                data.put("sucflag",true);
                data.put("msg","ok");
        }
        return ResponseEntity.ok(data);
    }

    /**
     * 查询可用门店
     * @param rq
     * @return
     */
    @PostMapping(value = "/findCouponStore")
    public ResponseEntity<Map>findCouponStore(@RequestBody CouponStoreVo rq){
        Map<String,Object>data= Maps.newHashMap();
        String cname=rq.getCityname();
        if (StringUtils.isNotBlank(cname)){
            //如果城市名称有的话，就从本地数据库查询
            CurrentStoreInfoT currentStoreInfoT=this.currentStoreInfoTRepo.findCurrentStoreInfoTByCityname(cname);
            if (currentStoreInfoT!=null){
                data.put("currentCityName",currentStoreInfoT.getCityname());
                data.put("currentStoreList",currentStoreInfoT.getStorename());
            }
        }else{
            //读取地理位置从接口查询
            CurrentStoreInfoT currentStoreInfoT=this.findCurrentCouponStores(rq.getLongitude(),rq.getLatitude());
            if (currentStoreInfoT!=null){
                data.put("currentCityName",currentStoreInfoT.getCityname());
                data.put("currentStoreList",currentStoreInfoT.getStorename());
                cname=currentStoreInfoT.getCityname();
            }

        }
        //获取其他城市
        String finalCname = cname;
        Specification<AllStoreInfoT> all = (root, query1, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.notEqual(root.get("cityname"), finalCname));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        List<AllStoreInfoT> allStoreInfoTS =this.allStoreInfoTRepo.findAll(all);
        List<StoreVo>storeVoList=new ArrayList<>();
        for (AllStoreInfoT alls:allStoreInfoTS){
            StoreVo bean=new StoreVo();
            bean.setCityname(alls.getCityname());
            bean.setStorename(alls.getStorename());
            storeVoList.add(bean);
        }
        data.put("allStoreList",storeVoList);
        data.put("sucflag",true);
        return ResponseEntity.ok(data);
    }

    private CurrentStoreInfoT findCurrentCouponStores(String lng,String lat){
        TreeMap<String, String> param = new TreeMap<>();
        param.put("longitude",lng);
        param.put("latitude", lat);
        param.put("timestamp", new Date().getTime()+"");
        param.put("qrcodeScene","FantaAR");
        String sign = signForOpen(param,"WehUgxg3VkKmObKreJZSNP8TPt695OAb");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("longitude",lng);
        map.add("latitude",lat);
        map.add("timestamp", param.get("timestamp"));
        map.add("qrcodeScene","FantaAR");
        map.add("sign",sign);
        logger.info(map.toString());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(ZLX_LBS_COUPON_URL+COUPONSTORE, request , String.class );
        JSONObject jsonObject=JSONObject.parseObject(response.getBody());
        //处理currentCity数据
        String currentCityName=StringUtils.EMPTY;
        String currentMdianJson=StringUtils.EMPTY;
        int currentLevel=0;
        JSONObject jdata=jsonObject.getJSONObject("data");
        JSONArray jsonArrayCurrentCity=jdata.getJSONArray("currentCity");
        CurrentStoreInfoT currentStoreInfoT=null;
        if (jsonArrayCurrentCity.size()>0){
            JSONObject ob= (JSONObject) jsonArrayCurrentCity.get(0);
            currentCityName=ob.getString("name");
            currentMdianJson=ob.getString("children");
            currentLevel=ob.getInteger("level");
            List<CurrentStoreVo>currentStoreVoList=GsonJson.parseJsonToData(currentMdianJson,CurrentStoreVo.class);
            for(CurrentStoreVo currentStoreVo:currentStoreVoList){
                Set<String>citySet=new HashSet<>();
                List<CurrentStoreChildrenVo>childSetVos=currentStoreVo.getChildren();
                for (CurrentStoreChildrenVo s:childSetVos){
                    citySet.add(s.getName());
                }
                Object[] array = citySet.toArray();
                String splitSetWithComma = StringUtils.join(array, ",");
                currentStoreInfoT=this.currentStoreInfoTRepo.findCurrentStoreInfoTByCityname(currentCityName);
                if (currentStoreInfoT==null){
                    currentStoreInfoT=new CurrentStoreInfoT();
                    currentStoreInfoT.setCityname(currentCityName);
                    currentStoreInfoT.setStorename(splitSetWithComma);
                    currentStoreInfoT.setLevel(currentLevel);
                    currentStoreInfoT.setCreatetime(DateTools.getSystemTimeLong());
                    currentStoreInfoT.setUpdatetime(DateTools.getSystemTimeLong());
                    currentStoreInfoT.setStatus("1");
                    currentStoreInfoT.setVersiont(1);
                    this.currentStoreInfoTRepo.save(currentStoreInfoT);
                }else{
                    currentStoreInfoT.setStorename(splitSetWithComma);
                    currentStoreInfoT.setUpdatetime(DateTools.getSystemTimeLong());
                    currentStoreInfoT.setVersiont(currentStoreInfoT.getVersiont()+1);
                    this.currentStoreInfoTRepo.save(currentStoreInfoT);
                }
            }
        }
        return currentStoreInfoT;
    }


    /**
     * 查询可用门店
     * @param rq
     * @return
     */
    @PostMapping(value = "/updateCouponStore")
    public ResponseEntity<Map>updateCouponStore(@RequestBody CouponStoreVo rq) {
        Map<String,Object>data= Maps.newHashMap();
        TreeMap<String, String> param = new TreeMap<>();
        param.put("longitude", rq.getLongitude());
        param.put("latitude", rq.getLatitude());
        param.put("timestamp", new Date().getTime()+"");
        param.put("qrcodeScene","FantaAR");
        String sign = signForOpen(param,"WehUgxg3VkKmObKreJZSNP8TPt695OAb");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("longitude", rq.getLongitude());
        map.add("latitude", rq.getLatitude());
        map.add("timestamp", param.get("timestamp"));
        map.add("qrcodeScene","FantaAR");
        map.add("sign",sign);
        logger.info(map.toString());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(ZLX_LBS_COUPON_URL+COUPONSTORE, request , String.class );
        JSONObject jsonObject=JSONObject.parseObject(response.getBody());
//        logger.info(response.getBody());
        JSONObject jdata=jsonObject.getJSONObject("data");
        JSONArray jsonArrayCurrentCity=jdata.getJSONArray("currentCity");
        //处理currentCity数据
        String currentCityName=StringUtils.EMPTY;
        String currentMdianJson=StringUtils.EMPTY;
        int currentLevel=0;
        if (jsonArrayCurrentCity.size()>0){
            JSONObject ob= (JSONObject) jsonArrayCurrentCity.get(0);
            currentCityName=ob.getString("name");
            currentMdianJson=ob.getString("children");
            currentLevel=ob.getInteger("level");
            List<CurrentStoreVo>currentStoreVoList=GsonJson.parseJsonToData(currentMdianJson,CurrentStoreVo.class);
            for(CurrentStoreVo currentStoreVo:currentStoreVoList){
                Set<String>citySet=new HashSet<>();
                List<CurrentStoreChildrenVo>childSetVos=currentStoreVo.getChildren();
                for (CurrentStoreChildrenVo s:childSetVos){
                    citySet.add(s.getName());
                }
                Object[] array = citySet.toArray();
                String splitSetWithComma = StringUtils.join(array, ",");
                CurrentStoreInfoT currentStoreInfoT=this.currentStoreInfoTRepo.findCurrentStoreInfoTByCityname(currentCityName);
                if (currentStoreInfoT==null){
                    currentStoreInfoT=new CurrentStoreInfoT();
                    currentStoreInfoT.setCityname(currentCityName);
                    currentStoreInfoT.setStorename(splitSetWithComma);
                    currentStoreInfoT.setLevel(currentLevel);
                    currentStoreInfoT.setCreatetime(DateTools.getSystemTimeLong());
                    currentStoreInfoT.setUpdatetime(DateTools.getSystemTimeLong());
                    currentStoreInfoT.setStatus("1");
                    currentStoreInfoT.setVersiont(1);
                    this.currentStoreInfoTRepo.save(currentStoreInfoT);
                }else{
                    currentStoreInfoT.setStorename(splitSetWithComma);
                    currentStoreInfoT.setUpdatetime(DateTools.getSystemTimeLong());
                    currentStoreInfoT.setVersiont(currentStoreInfoT.getVersiont()+1);
                    this.currentStoreInfoTRepo.save(currentStoreInfoT);
                }
            }
        }

        //处理所有城市
        JSONArray jsonArrayAllCity=jdata.getJSONArray("allCity");
        //处理currentCity数据
        if (jsonArrayAllCity.size()>0){
            for (int i=0;i<jsonArrayAllCity.size();i++){
                JSONObject ob= (JSONObject) jsonArrayAllCity.get(i);
                String cityName=ob.getString("name");
                String mdianJson=ob.getString("children");
                int level=ob.getInteger("level");
                List<CurrentStoreVo>storeVoList=GsonJson.parseJsonToData(mdianJson,CurrentStoreVo.class);
                for(CurrentStoreVo storeVo:storeVoList){
                    Set<String>citySet=new HashSet<>();
                    List<CurrentStoreChildrenVo>childSetVos=storeVo.getChildren();
                    for (CurrentStoreChildrenVo s:childSetVos){
                        citySet.add(s.getName());
                    }
                    Object[] array = citySet.toArray();
                    String splitSetWithComma = StringUtils.join(array, ",");
                    AllStoreInfoT allStoreInfoT=this.allStoreInfoTRepo.findAllStoreInfoTByCityname(cityName);
                    if (allStoreInfoT==null){
                        allStoreInfoT=new AllStoreInfoT();
                        allStoreInfoT.setCityname(cityName);
                        allStoreInfoT.setStorename(splitSetWithComma);
                        allStoreInfoT.setLevel(level);
                        allStoreInfoT.setCreatetime(DateTools.getSystemTimeLong());
                        allStoreInfoT.setUpdatetime(DateTools.getSystemTimeLong());
                        allStoreInfoT.setStatus("1");
                        allStoreInfoT.setVersiont(1);
                        this.allStoreInfoTRepo.save(allStoreInfoT);
                    }else{
                        allStoreInfoT.setStorename(splitSetWithComma);
                        allStoreInfoT.setUpdatetime(DateTools.getSystemTimeLong());
                        allStoreInfoT.setVersiont(allStoreInfoT.getVersiont()+1);
                        this.allStoreInfoTRepo.save(allStoreInfoT);
                    }
                }
            }
        }
        return ResponseEntity.ok(data);
    }




    /**
     * 开放平台的签名验证
     */
    public String signForOpen(TreeMap<String, String> param,String signkey) {
        StringBuilder sb = new StringBuilder();
        param.forEach((k, v) -> {
            if (k != null && v != null) {
                sb.append(k).append('=').append(v).append('&');
            }
        });
        sb.deleteCharAt(sb.length() - 1);
        String signStr=sb+signkey;
        return DigestUtils.md5Hex(signStr);
    }

    private Map<String,Object> getParams(HttpServletRequest request)  {
        Map<String,Object>data= Maps.newHashMap();
        String str= StringUtils.EMPTY;
        BufferedReader br = null;
        try {
            br = request.getReader();
            StringBuilder rStr = new StringBuilder();
            while ((str = Objects.requireNonNull(br).readLine()) != null) {
                rStr.append(str.trim());
            }
            data.put("params",rStr.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    //FANTA自己的优惠券就是直接通知CEP进行一个核销的操作


    @PostMapping(value = "/insertTest")
    public ResponseEntity<Map>insertTest(){
        Map<String,Object>data= Maps.newHashMap();
        for (int i=0;i<5;i++){
            FantaPrizeT bean1=new FantaPrizeT();
            bean1.setOpenid("oYMJh5ErFQxcGDrlHB-JtOs6VfUI");
            bean1.setUnionid("oYMJh_WgAwQ0VF2MzC-SNUQQUw");
            bean1.setMembershipid("1");
            bean1.setCreatetime(DateTools.getSystemTimeLong());
            bean1.setUpdatetime(DateTools.getSystemTimeLong());
            bean1.setStatus("1");
            bean1.setVersiont(1);
            bean1.setMid("312115491729346111");
            bean1.setPrizeid("bilibili001");
            bean1.setPrizename("Bilibili大会员");
            bean1.setPrizetime("2022-06-29 18:25:33");
            bean1.setPrizeticket(PrimaryKey.get().toString());
            bean1.setUsertoken(SHA1.getSHA1StrJava(bean1.getMid()));
            bean1.setPrizestatus("pending");
            bean1.setPtype("bilibili");
            bean1.setIsmaterial(0);
            bean1.setPrizecode("1");
            bean1.setPrizenumber("84054380525056000");
            this.fantaPrizeTRepo.save(bean1);
        }

        for (int i=0;i<5;i++){
            FantaPrizeT bean1=new FantaPrizeT();
            bean1.setOpenid("oYMJh5ErFQxcGDrlHB-JtOs6VfUI");
            bean1.setUnionid("oYMJh_WgAwQ0VF2MzC-SNUQQUw");
            bean1.setMembershipid("1");
            bean1.setCreatetime(DateTools.getSystemTimeLong());
            bean1.setUpdatetime(DateTools.getSystemTimeLong());
            bean1.setStatus("1");
            bean1.setVersiont(1);
            bean1.setMid("312115491729346111");
            bean1.setPrizeid("bilibili002");
            bean1.setPrizename("Bilibili NFT");
            bean1.setPrizetime("2022-06-28 18:25:33");
            bean1.setPrizeticket(PrimaryKey.get().toString());
            bean1.setUsertoken(SHA1.getSHA1StrJava(bean1.getMid()));
            bean1.setPrizestatus("pending");
            bean1.setPtype("bilibili");
            bean1.setIsmaterial(0);
            bean1.setPrizecode("1");
            bean1.setPrizenumber("84054380525056001");

            this.fantaPrizeTRepo.save(bean1);
        }


        return ResponseEntity.ok(data);
    }
}
