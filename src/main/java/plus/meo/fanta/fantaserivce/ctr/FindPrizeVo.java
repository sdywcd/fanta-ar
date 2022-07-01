package plus.meo.fanta.fantaserivce.ctr;

import java.io.Serializable;

public class FindPrizeVo implements Serializable {

    private String userToken;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
