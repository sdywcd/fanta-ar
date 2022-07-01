package plus.meo.fanta.fantaserivce.ctr;

import java.io.Serializable;

public class ReceivedBenefitVo implements Serializable {

    private String userToken;

    private String prizeAcquireId;

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
}
