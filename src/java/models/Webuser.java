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
import javax.persistence.Lob;
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
@Table(name = "webuser")
@NamedQueries({
    @NamedQuery(name = "Webuser.findAll", query = "SELECT w FROM Webuser w"),
    @NamedQuery(name = "Webuser.findByUserid", query = "SELECT w FROM Webuser w WHERE w.userid = :userid"),
    @NamedQuery(name = "Webuser.findByUsername", query = "SELECT w FROM Webuser w WHERE w.username = :username"),
    @NamedQuery(name = "Webuser.findByUserpwd", query = "SELECT w FROM Webuser w WHERE w.userpwd = :userpwd"),
    @NamedQuery(name = "Webuser.findByUsercreationdate", query = "SELECT w FROM Webuser w WHERE w.usercreationdate = :usercreationdate"),
    @NamedQuery(name = "Webuser.findByFname", query = "SELECT w FROM Webuser w WHERE w.fname = :fname"),
    @NamedQuery(name = "Webuser.findByLname", query = "SELECT w FROM Webuser w WHERE w.lname = :lname"),
    @NamedQuery(name = "Webuser.findByEmail", query = "SELECT w FROM Webuser w WHERE w.email = :email")})
public class Webuser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "userid")
    private Integer userid;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "userpwd")
    private String userpwd;
    @Basic(optional = false)
    @Column(name = "usercreationdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date usercreationdate;
    @Basic(optional = false)
    @Column(name = "fname")
    private String fname;
    @Basic(optional = false)
    @Column(name = "lname")
    private String lname;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Lob
    @Column(name = "photo")
    private byte[] photo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fromuserid")
    private Collection<Webmessages> webmessagesCollection;
    @OneToMany(mappedBy = "touserid")
    private Collection<Webmessages> webmessagesCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "webuser")
    private Collection<Webusergroup> webusergroupCollection;

    public Webuser() {
    }

    public Webuser(Integer userid) {
        this.userid = userid;
    }

    public Webuser(String username, String userpwd, Date usercreationdate, String fname, String lname, String email) {
        this.username = username;
        this.userpwd = userpwd;
        this.usercreationdate = usercreationdate;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public Date getUsercreationdate() {
        return usercreationdate;
    }

    public void setUsercreationdate(Date usercreationdate) {
        this.usercreationdate = usercreationdate;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Collection<Webmessages> getWebmessagesCollection() {
        return webmessagesCollection;
    }

    public void setWebmessagesCollection(Collection<Webmessages> webmessagesCollection) {
        this.webmessagesCollection = webmessagesCollection;
    }

    public Collection<Webmessages> getWebmessagesCollection1() {
        return webmessagesCollection1;
    }

    public void setWebmessagesCollection1(Collection<Webmessages> webmessagesCollection1) {
        this.webmessagesCollection1 = webmessagesCollection1;
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
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Webuser)) {
            return false;
        }
        Webuser other = (Webuser) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Webuser{" + "username=" + username + ", fname=" + fname + ", lname=" + lname + '}';
    }

    
    
}
