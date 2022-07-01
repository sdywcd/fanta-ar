package plus.meo.fanta.fantaserivce.ctr.vo;

import java.io.Serializable;

public class MaterialPrizeVo implements Serializable {
    private String mid;
    private String prizeAcquireId;
    private String province;
    private String city;
    private String area;
    private String address;
    private String name;
    private String phoneNumber;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getPrizeAcquireId() {
        return prizeAcquireId;
    }

    public void setPrizeAcquireId(String prizeAcquireId) {
        this.prizeAcquireId = prizeAcquireId;
    }
}
