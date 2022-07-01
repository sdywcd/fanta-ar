package plus.meo.fanta.fantaserivce.ctr.vo;

import java.io.Serializable;

public class StoreVo implements Serializable {
    private String cityname;
    private String storename;

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }
}
