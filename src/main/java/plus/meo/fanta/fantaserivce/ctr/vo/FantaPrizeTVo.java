package plus.meo.fanta.fantaserivce.ctr.vo;

public class FantaPrizeTVo {

    private String userToken;

    private String prizeId;

    private String prizeName;

    private String prizeTime;

    private String prizeAcquireId;

    private String prizeStatus;

    public String getPrizeStatus() {
        return prizeStatus;
    }

    public void setPrizeStatus(String prizeStatus) {
        this.prizeStatus = prizeStatus;
    }

    public String getPrizeAcquireId() {
        return prizeAcquireId;
    }

    public void setPrizeAcquireId(String prizeAcquireId) {
        this.prizeAcquireId = prizeAcquireId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(String prizeId) {
        this.prizeId = prizeId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizeTime() {
        return prizeTime;
    }

    public void setPrizeTime(String prizeTime) {
        this.prizeTime = prizeTime;
    }
}
