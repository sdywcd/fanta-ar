package plus.meo.fanta.fantaserivce.ctr.vo;

import java.io.Serializable;

public class CouponStoreVo implements Serializable {

    private String longitude;

    private String latitude;

    private String cityname;

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
