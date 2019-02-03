/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.Webuser;
import models.Webgroup;
import models.Webusergroup;
import models.WebusergroupPK;

/**
 *
 * @author nikolaos
 */
public class WebusergroupJpaController implements Serializable {

    public WebusergroupJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Webusergroup webusergroup) throws PreexistingEntityException, Exception {
        if (webusergroup.getWebusergroupPK() == null) {
            webusergroup.setWebusergroupPK(new WebusergroupPK());
        }
        webusergroup.getWebusergroupPK().setUserid(webusergroup.getWebuser().getUserid());
        webusergroup.getWebusergroupPK().setGroupid(webusergroup.getWebgroup().getGroupid());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Webuser webuser = webusergroup.getWebuser();
            if (webuser != null) {
                webuser = em.getReference(webuser.getClass(), webuser.getUserid());
                webusergroup.setWebuser(webuser);
            }
            Webgroup webgroup = webusergroup.getWebgroup();
            if (webgroup != null) {
                webgroup = em.getReference(webgroup.getClass(), webgroup.getGroupid());
                webusergroup.setWebgroup(webgroup);
            }
            em.persist(webusergroup);
            if (webuser != null) {
                webuser.getWebusergroupCollection().add(webusergroup);
                webuser = em.merge(webuser);
            }
            if (webgroup != null) {
                webgroup.getWebusergroupCollection().add(webusergroup);
                webgroup = em.merge(webgroup);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findWebusergroup(webusergroup.getWebusergroupPK()) != null) {
                throw new PreexistingEntityException("Webusergroup " + webusergroup + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Webusergroup webusergroup) throws NonexistentEntityException, Exception {
        webusergroup.getWebusergroupPK().setUserid(webusergroup.getWebuser().getUserid());
        webusergroup.getWebusergroupPK().setGroupid(webusergroup.getWebgroup().getGroupid());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Webusergroup persistentWebusergroup = em.find(Webusergroup.class, webusergroup.getWebusergroupPK());
            Webuser webuserOld = persistentWebusergroup.getWebuser();
            Webuser webuserNew = webusergroup.getWebuser();
            Webgroup webgroupOld = persistentWebusergroup.getWebgroup();
            Webgroup webgroupNew = webusergroup.getWebgroup();
            if (webuserNew != null) {
                webuserNew = em.getReference(webuserNew.getClass(), webuserNew.getUserid());
                webusergroup.setWebuser(webuserNew);
            }
            if (webgroupNew != null) {
                webgroupNew = em.getReference(webgroupNew.getClass(), webgroupNew.getGroupid());
                webusergroup.setWebgroup(webgroupNew);
            }
            webusergroup = em.merge(webusergroup);
            if (webuserOld != null && !webuserOld.equals(webuserNew)) {
                webuserOld.getWebusergroupCollection().remove(webusergroup);
                webuserOld = em.merge(webuserOld);
            }
            if (webuserNew != null && !webuserNew.equals(webuserOld)) {
                webuserNew.getWebusergroupCollection().add(webusergroup);
                webuserNew = em.merge(webuserNew);
            }
            if (webgroupOld != null && !webgroupOld.equals(webgroupNew)) {
                webgroupOld.getWebusergroupCollection().remove(webusergroup);
                webgroupOld = em.merge(webgroupOld);
            }
            if (webgroupNew != null && !webgroupNew.equals(webgroupOld)) {
                webgroupNew.getWebusergroupCollection().add(webusergroup);
                webgroupNew = em.merge(webgroupNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                WebusergroupPK id = webusergroup.getWebusergroupPK();
                if (findWebusergroup(id) == null) {
                    throw new NonexistentEntityException("The webusergroup with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(WebusergroupPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Webusergroup webusergroup;
            try {
                webusergroup = em.getReference(Webusergroup.class, id);
                webusergroup.getWebusergroupPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The webusergroup with id " + id + " no longer exists.", enfe);
            }
            Webuser webuser = webusergroup.getWebuser();
            if (webuser != null) {
                webuser.getWebusergroupCollection().remove(webusergroup);
                webuser = em.merge(webuser);
            }
            Webgroup webgroup = webusergroup.getWebgroup();
            if (webgroup != null) {
                webgroup.getWebusergroupCollection().remove(webusergroup);
                webgroup = em.merge(webgroup);
            }
            em.remove(webusergroup);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Webusergroup> findWebusergroupEntities() {
        return findWebusergroupEntities(true, -1, -1);
    }

    public List<Webusergroup> findWebusergroupEntities(int maxResults, int firstResult) {
        return findWebusergroupEntities(false, maxResults, firstResult);
    }

    private List<Webusergroup> findWebusergroupEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Webusergroup.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Webusergroup findWebusergroup(WebusergroupPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Webusergroup.class, id);
        } finally {
            em.close();
        }
    }

    public int getWebusergroupCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Webusergroup> rt = cq.from(Webusergroup.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
