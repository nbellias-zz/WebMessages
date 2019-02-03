/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author nikolaos
 */
@Entity
@Table(name = "webmessages")
@NamedQueries({
    @NamedQuery(name = "Webmessages.findAll", query = "SELECT w FROM Webmessages w"),
    @NamedQuery(name = "Webmessages.findByMsgid", query = "SELECT w FROM Webmessages w WHERE w.msgid = :msgid")})
public class Webmessages implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "msgid")
    private Integer msgid;
    @Basic(optional = false)
    @Lob
    @Column(name = "message")
    private String message;

    @Basic(optional = false)
    @Column(name = "doc")
    private Timestamp doc;

    @JoinColumn(name = "fromuserid", referencedColumnName = "userid")
    @ManyToOne(optional = false)
    private Webuser fromuserid;
    @JoinColumn(name = "touserid", referencedColumnName = "userid")
    @ManyToOne
    private Webuser touserid;

    public Webmessages() {
    }

    public Webmessages(Integer msgid) {
        this.msgid = msgid;
    }

    public Webmessages(Webuser fromu, Webuser tou, String message) {
        this.fromuserid = fromu;
        this.touserid = tou;
        this.message = message;
        this.doc = new Timestamp(System.currentTimeMillis());
    }

    public Webmessages(Webuser fromu, Webuser tou, String message, Timestamp doc) {
        this.fromuserid = fromu;
        this.touserid = tou;
        this.message = message;
        this.doc = doc;
    }

    public Integer getMsgid() {
        return msgid;
    }

    public void setMsgid(Integer msgid) {
        this.msgid = msgid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getDoc() {
        return doc;
    }

    public void setDoc(Timestamp doc) {
        this.doc = doc;
    }

    public Webuser getFromuserid() {
        return fromuserid;
    }

    public void setFromuserid(Webuser fromuserid) {
        this.fromuserid = fromuserid;
    }

    public Webuser getTouserid() {
        return touserid;
    }

    public void setTouserid(Webuser touserid) {
        this.touserid = touserid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (msgid != null ? msgid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Webmessages)) {
            return false;
        }
        Webmessages other = (Webmessages) object;
        if ((this.msgid == null && other.msgid != null) || (this.msgid != null && !this.msgid.equals(other.msgid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Webmessages[ msgid=" + msgid + " ]";
    }

}
