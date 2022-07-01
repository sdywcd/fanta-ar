package plus.meo.fanta.fantaserivce.entity;

import javax.persistence.*;

@Table(name = "material_address_t")
@Entity
public class MaterialAddressT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "FANTAPRIZEID", nullable = false)
    private Integer fantaprizeid;

    @Column(name = "PROVINCE", nullable = false, length = 45)
    private String province;

    @Column(name = "CITY", nullable = false, length = 45)
    private String city;

    @Column(name = "CREATETIME", nullable = false)
    private Long createtime;

    @Column(name = "UPDATETIME", nullable = false)
    private Long updatetime;

    @Column(name = "STATUS", nullable = false, length = 1)
    private String status;

    @Column(name = "VERSIONT", nullable = false)
    private Integer versiont;

    @Column(name = "AREA", nullable = false, length = 45)
    private String area;

    @Column(name = "ADDRESS", nullable = false, length = 500)
    private String address;

    @Column(name = "NAME", nullable = false, length = 45)
    private String name;

    @Column(name = "PHONENUMBER", nullable = false, length = 45)
    private String phonenumber;

    @Column(name = "ISSYNC", nullable = false)
    private Integer issync;

    public Integer getIssync() {
        return issync;
    }

    public void setIssync(Integer issync) {
        this.issync = issync;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public Integer getFantaprizeid() {
        return fantaprizeid;
    }

    public void setFantaprizeid(Integer fantaprizeid) {
        this.fantaprizeid = fantaprizeid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}