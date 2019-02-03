/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.Webmessages;
import models.Webuser;

/**
 *
 * @author nikolaos
 */
public class WebmessagesJpaController implements Serializable {

    public WebmessagesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Webmessages webmessages) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Webuser fromuserid = webmessages.getFromuserid();
            if (fromuserid != null) {
                fromuserid = em.getReference(fromuserid.getClass(), fromuserid.getUserid());
                webmessages.setFromuserid(fromuserid);
            }
            Webuser touserid = webmessages.getTouserid();
            if (touserid != null) {
                touserid = em.getReference(touserid.getClass(), touserid.getUserid());
                webmessages.setTouserid(touserid);
            }
            em.persist(webmessages);
            if (fromuserid != null) {
                fromuserid.getWebmessagesCollection().add(webmessages);
                fromuserid = em.merge(fromuserid);
            }
            if (touserid != null) {
                touserid.getWebmessagesCollection().add(webmessages);
                touserid = em.merge(touserid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Webmessages webmessages) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Webmessages persistentWebmessages = em.find(Webmessages.class, webmessages.getMsgid());
            Webuser fromuseridOld = persistentWebmessages.getFromuserid();
            Webuser fromuseridNew = webmessages.getFromuserid();
            Webuser touseridOld = persistentWebmessages.getTouserid();
            Webuser touseridNew = webmessages.getTouserid();
            if (fromuseridNew != null) {
                fromuseridNew = em.getReference(fromuseridNew.getClass(), fromuseridNew.getUserid());
                webmessages.setFromuserid(fromuseridNew);
            }
            if (touseridNew != null) {
                touseridNew = em.getReference(touseridNew.getClass(), touseridNew.getUserid());
                webmessages.setTouserid(touseridNew);
            }
            webmessages = em.merge(webmessages);
            if (fromuseridOld != null && !fromuseridOld.equals(fromuseridNew)) {
                fromuseridOld.getWebmessagesCollection().remove(webmessages);
                fromuseridOld = em.merge(fromuseridOld);
            }
            if (fromuseridNew != null && !fromuseridNew.equals(fromuseridOld)) {
                fromuseridNew.getWebmessagesCollection().add(webmessages);
                fromuseridNew = em.merge(fromuseridNew);
            }
            if (touseridOld != null && !touseridOld.equals(touseridNew)) {
                touseridOld.getWebmessagesCollection().remove(webmessages);
                touseridOld = em.merge(touseridOld);
            }
            if (touseridNew != null && !touseridNew.equals(touseridOld)) {
                touseridNew.getWebmessagesCollection().add(webmessages);
                touseridNew = em.merge(touseridNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = webmessages.getMsgid();
                if (findWebmessages(id) == null) {
                    throw new NonexistentEntityException("The webmessages with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Webmessages webmessages;
            try {
                webmessages = em.getReference(Webmessages.class, id);
                webmessages.getMsgid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The webmessages with id " + id + " no longer exists.", enfe);
            }
            Webuser fromuserid = webmessages.getFromuserid();
            if (fromuserid != null) {
                fromuserid.getWebmessagesCollection().remove(webmessages);
                fromuserid = em.merge(fromuserid);
            }
            Webuser touserid = webmessages.getTouserid();
            if (touserid != null) {
                touserid.getWebmessagesCollection().remove(webmessages);
                touserid = em.merge(touserid);
            }
            em.remove(webmessages);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Webmessages> findWebmessagesEntities() {
        return findWebmessagesEntities(true, -1, -1);
    }

    public List<Webmessages> findWebmessagesEntities(int maxResults, int firstResult) {
        return findWebmessagesEntities(false, maxResults, firstResult);
    }

    private List<Webmessages> findWebmessagesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Webmessages.class));
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

    public Webmessages findWebmessages(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Webmessages.class, id);
        } finally {
            em.close();
        }
    }

    public int getWebmessagesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Webmessages> rt = cq.from(Webmessages.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Webmessages> fetchMessagesFromUser(int userId){
        List<Webmessages> messages = new ArrayList();
        EntityManager em = getEntityManager();
        try{
            Query q = em.createQuery("SELECT w FROM Webmessages w WHERE w.touserid.userid = :touserid", Webmessages.class);
            q.setParameter("touserid", userId);
            messages = q.getResultList();
        } finally {
            em.close();
        }
        
        return messages;
    }
    
}
