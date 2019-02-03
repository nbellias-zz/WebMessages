/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author nikolaos
 */
@Entity
@Table(name = "webusergroup")
@NamedQueries({
    @NamedQuery(name = "Webusergroup.findAll", query = "SELECT w FROM Webusergroup w"),
    @NamedQuery(name = "Webusergroup.findByUserid", query = "SELECT w FROM Webusergroup w WHERE w.webusergroupPK.userid = :userid"),
    @NamedQuery(name = "Webusergroup.findByGroupid", query = "SELECT w FROM Webusergroup w WHERE w.webusergroupPK.groupid = :groupid"),
    @NamedQuery(name = "Webusergroup.findByUsergroupdoc", query = "SELECT w FROM Webusergroup w WHERE w.usergroupdoc = :usergroupdoc")})
public class Webusergroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected WebusergroupPK webusergroupPK;
    @Basic(optional = false)
    @Column(name = "usergroupdoc")
    @Temporal(TemporalType.TIMESTAMP)
    private Date usergroupdoc;
    @JoinColumn(name = "userid", referencedColumnName = "userid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Webuser webuser;
    @JoinColumn(name = "groupid", referencedColumnName = "groupid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Webgroup webgroup;

    public Webusergroup() {
    }

    public Webusergroup(WebusergroupPK webusergroupPK) {
        this.webusergroupPK = webusergroupPK;
    }

    public Webusergroup(WebusergroupPK webusergroupPK, Date usergroupdoc) {
        this.webusergroupPK = webusergroupPK;
        this.usergroupdoc = usergroupdoc;
    }

    public Webusergroup(int userid, int groupid) {
        this.webusergroupPK = new WebusergroupPK(userid, groupid);
    }

    public WebusergroupPK getWebusergroupPK() {
        return webusergroupPK;
    }

    public void setWebusergroupPK(WebusergroupPK webusergroupPK) {
        this.webusergroupPK = webusergroupPK;
    }

    public Date getUsergroupdoc() {
        return usergroupdoc;
    }

    public void setUsergroupdoc(Date usergroupdoc) {
        this.usergroupdoc = usergroupdoc;
    }

    public Webuser getWebuser() {
        return webuser;
    }

    public void setWebuser(Webuser webuser) {
        this.webuser = webuser;
    }

    public Webgroup getWebgroup() {
        return webgroup;
    }

    public void setWebgroup(Webgroup webgroup) {
        this.webgroup = webgroup;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (webusergroupPK != null ? webusergroupPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Webusergroup)) {
            return false;
        }
        Webusergroup other = (Webusergroup) object;
        if ((this.webusergroupPK == null && other.webusergroupPK != null) || (this.webusergroupPK != null && !this.webusergroupPK.equals(other.webusergroupPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Webusergroup[ webusergroupPK=" + webusergroupPK + " ]";
    }
    
}
