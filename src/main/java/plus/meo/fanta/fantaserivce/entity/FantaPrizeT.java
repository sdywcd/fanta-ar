package plus.meo.fanta.fantaserivce.entity;

import javax.persistence.*;

@Table(name = "fanta_prize_t")
@Entity
public class FantaPrizeT {
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

    @Column(name = "PRIZEID", nullable = false, length = 45)
    private String prizeid;

    @Column(name = "PRIZENAME", nullable = false, length = 500)
    private String prizename;

    @Column(name = "PRIZETIME", nullable = false, length = 45)
    private String prizetime;

    @Column(name = "PRIZETICKET", nullable = false, length = 45)
    private String prizeticket;

    @Column(name = "PRIZECODE", nullable = false, length = 45)
    private String prizecode;

    @Column(name = "PRIZENUMBER", nullable = false, length = 45)
    private String prizenumber;

    @Column(name = "USERTOKEN", nullable = false, length = 500)
    private String usertoken;

    @Column(name = "PRIZESTATUS", nullable = false, length = 45)
    private String prizestatus;

    @Column(name = "PTYPE", nullable = false, length = 45)
    private String ptype;

    @Column(name = "ISMATERIAL", nullable = false)
    private Integer ismaterial;

    public Integer getIsmaterial() {
        return ismaterial;
    }

    public void setIsmaterial(Integer ismaterial) {
        this.ismaterial = ismaterial;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getPrizestatus() {
        return prizestatus;
    }

    public void setPrizestatus(String prizestatus) {
        this.prizestatus = prizestatus;
    }

    public String getUsertoken() {
        return usertoken;
    }

    public void setUsertoken(String usertoken) {
        this.usertoken = usertoken;
    }

    public String getPrizenumber() {
        return prizenumber;
    }

    public void setPrizenumber(String prizenumber) {
        this.prizenumber = prizenumber;
    }

    public String getPrizecode() {
        return prizecode;
    }

    public void setPrizecode(String prizecode) {
        this.prizecode = prizecode;
    }

    public String getPrizeticket() {
        return prizeticket;
    }

    public void setPrizeticket(String prizeticket) {
        this.prizeticket = prizeticket;
    }

    public String getPrizetime() {
        return prizetime;
    }

    public void setPrizetime(String prizetime) {
        this.prizetime = prizetime;
    }

    public String getPrizename() {
        return prizename;
    }

    public void setPrizename(String prizename) {
        this.prizename = prizename;
    }

    public String getPrizeid() {
        return prizeid;
    }

    public void setPrizeid(String prizeid) {
        this.prizeid = prizeid;
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