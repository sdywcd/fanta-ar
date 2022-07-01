package plus.meo.fanta.fantaserivce.ctr;

import java.io.Serializable;

public class VoRq implements Serializable {


    private String url;

    private String body;

    private String openid;

    private String mid;

    private String campaignCode;

    private String jackpotid;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getJackpotid() {
        return jackpotid;
    }

    public void setJackpotid(String jackpotid) {
        this.jackpotid = jackpotid;
    }

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
