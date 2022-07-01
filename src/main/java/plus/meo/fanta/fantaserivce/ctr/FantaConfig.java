package plus.meo.fanta.fantaserivce.ctr;

public class FantaConfig {

    /**
     * 活动编码
     */
    public static String COMPAIGNCODE="2022FantaAR";
    /**
     * 权益编码
     */
    public static String BENEFITCODE="bcode";

    public static String SKEY="yQTVAbGMxgGVxJI4EBtF1nHowkXoa9Ji";

    /**
     * 知而行的LBS优惠券门店列表URL
     */
    public static final String ZLX_LBS_COUPON_URL="https://coke-iu-test.icoke.cn";
    /**
     * 可用门店查询
     */
    public static final String COUPONSTORE="/zex-cola-iu/external/store/couponStore";

    /**
     * 测试密钥
     */
    public static final String COUPON_SIGN_KEY="WehUgxg3VkKmObKreJZSNP8TPt695OAb";


    /**
     * 测试地址
     * https://koplus.icoke.cn 正式
     */
//    public static String RELEASEURL="https://koplusuat.icoke.cn";
    /**
     * 测试地址
     * https://koplus.icoke.cn 正式
     */
    public static final String KO_RELEASEURL="https://koplusuat.icoke.cn";

    /**
     * 获取后端业务token
     */
    public static final String FANTATOKEN_URL="/backend/open-api/v1/tokens/bearer";

    /**
     * 获取用户信息token
     */
    public static final String FANTA_H5_TOKEN_URL="/backend/open-api/v1/users/token";

    /**
     * 抽奖
     */
    public static String FANTA_DRAW_URL="/crel/open-api/v1/lotteries/draw";

    /**
     * 绑定权益
     */
    public static String FANTA_BIND_BENEFITS="/cre/open-api/v2/admin/users/benefits/bind";





}
