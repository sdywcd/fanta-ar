package plus.meo.fanta.fantaserivce.entity;

import javax.persistence.*;

@Table(name = "fanta_member_info_t")
@Entity
public class FantaMemberInfoT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "OPENID", nullable = false, length = 128)
    private String openid;

    @Column(name = "UNIONID", nullable = false, length = 128)
    private String unionid;

    @Column(name = "MEMBERSHIPID", nullable = false, length = 128)
    private String membershipid;

    @Column(name = "CREATETIME", nullable = false)
    private Long createtime;

    @Column(name = "UPDATETIME", nullable = false)
    private Long updatetime;

    @Column(name = "STATUS", nullable = false, length = 1)
    private String status;

    @Column(name = "VERSIONT", nullable = false)
    private Integer versiont;

    @Column(name = "MID", nullable = false, length = 128)
    private String mid;

    @Column(name = "NICKNAME", length = 64)
    private String nickname;

    @Column(name = "AVATARURL", nullable = false, length = 500)
    private String avatarurl;

    @Column(name = "GENDER", nullable = false)
    private Integer gender;

    @Column(name = "COUNTRY", nullable = false, length = 45)
    private String country;

    @Column(name = "PROVINCE", nullable = false, length = 45)
    private String province;

    @Column(name = "CITY", nullable = false, length = 45)
    private String city;

    @Column(name = "LANGUAGE", nullable = false, length = 45)
    private String language;

    @Column(name = "PHONENUMBER", nullable = false, length = 45)
    private String phonenumber;

    @Column(name = "USERTOKEN", nullable = false, length = 500)
    private String usertoken;

    public String getUsertoken() {
        return usertoken;
    }

    public void setUsertoken(String usertoken) {
        this.usertoken = usertoken;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Integer getVersiont() {
        return versiont;
    }

    public void setVersiont(Integer versiont) {
        this.versiont = versiont;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Long updatetime) {
        this.updatetime = updatetime;
    }

    public Long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

    public String getMembershipid() {
        return membershipid;
    }

    public void setMembershipid(String membershipid) {
        this.membershipid = membershipid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}