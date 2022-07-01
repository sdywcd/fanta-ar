package plus.meo.fanta.fantaserivce.entity;

import javax.persistence.*;

@Table(name = "all_store_info_t")
@Entity
public class AllStoreInfoT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "LEVEL", nullable = false)
    private Integer level;

    @Column(name = "CITYNAME", nullable = false, length = 45)
    private String cityname;

    @Column(name = "STORENAME", nullable = false, length = 1000)
    private String storename;

    @Column(name = "CREATETIME", nullable = false)
    private Long createtime;

    @Column(name = "UPDATETIME", nullable = false)
    private Long updatetime;

    @Column(name = "STATUS", nullable = false, length = 1)
    private String status;

    @Column(name = "VERSIONT", nullable = false)
    private Integer versiont;

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

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}