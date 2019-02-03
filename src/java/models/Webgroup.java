/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author nikolaos
 */
@Entity
@Table(name = "webgroup")
@NamedQueries({
    @NamedQuery(name = "Webgroup.findAll", query = "SELECT w FROM Webgroup w"),
    @NamedQuery(name = "Webgroup.findByGroupid", query = "SELECT w FROM Webgroup w WHERE w.groupid = :groupid"),
    @NamedQuery(name = "Webgroup.findByGroupname", query = "SELECT w FROM Webgroup w WHERE w.groupname = :groupname"),
    @NamedQuery(name = "Webgroup.findByGroupcreationdate", query = "SELECT w FROM Webgroup w WHERE w.groupcreationdate = :groupcreationdate")})
public class Webgroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "groupid")
    private Integer groupid;
    @Basic(optional = false)
    @Column(name = "groupname")
    private String groupname;
    @Basic(optional = false)
    @Column(name = "groupcreationdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date groupcreationdate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "webgroup")
    private Collection<Webusergroup> webusergroupCollection;

    public Webgroup() {
    }

    public Webgroup(Integer groupid) {
        this.groupid = groupid;
    }

    public Webgroup(String groupname, Date groupcreationdate) {
        this.groupname = groupname;
        this.groupcreationdate = groupcreationdate;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public Date getGroupcreationdate() {
        return groupcreationdate;
    }

    public void setGroupcreationdate(Date groupcreationdate) {
        this.groupcreationdate = groupcreationdate;
    }

    public Collection<Webusergroup> getWebusergroupCollection() {
        return webusergroupCollection;
    }

    public void setWebusergroupCollection(Collection<Webusergroup> webusergroupCollection) {
        this.webusergroupCollection = webusergroupCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupid != null ? groupid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Webgroup)) {
            return false;
        }
        Webgroup other = (Webgroup) object;
        if ((this.groupid == null && other.groupid != null) || (this.groupid != null && !this.groupid.equals(other.groupid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Webgroup[ groupid=" + groupid + " ]";
    }
    
}
