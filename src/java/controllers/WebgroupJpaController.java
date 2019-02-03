/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.Webusergroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import models.Webgroup;

/**
 *
 * @author nikolaos
 */
public class WebgroupJpaController implements Serializable {

    public WebgroupJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Webgroup webgroup) {
        if (webgroup.getWebusergroupCollection() == null) {
            webgroup.setWebusergroupCollection(new ArrayList<Webusergroup>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Webusergroup> attachedWebusergroupCollection = new ArrayList<Webusergroup>();
            for (Webusergroup webusergroupCollectionWebusergroupToAttach : webgroup.getWebusergroupCollection()) {
                webusergroupCollectionWebusergroupToAttach = em.getReference(webusergroupCollectionWebusergroupToAttach.getClass(), webusergroupCollectionWebusergroupToAttach.getWebusergroupPK());
                attachedWebusergroupCollection.add(webusergroupCollectionWebusergroupToAttach);
            }
            webgroup.setWebusergroupCollection(attachedWebusergroupCollection);
            em.persist(webgroup);
            for (Webusergroup webusergroupCollectionWebusergroup : webgroup.getWebusergroupCollection()) {
                Webgroup oldWebgroupOfWebusergroupCollectionWebusergroup = webusergroupCollectionWebusergroup.getWebgroup();
                webusergroupCollectionWebusergroup.setWebgroup(webgroup);
                webusergroupCollectionWebusergroup = em.merge(webusergroupCollectionWebusergroup);
                if (oldWebgroupOfWebusergroupCollectionWebusergroup != null) {
                    oldWebgroupOfWebusergroupCollectionWebusergroup.getWebusergroupCollection().remove(webusergroupCollectionWebusergroup);
                    oldWebgroupOfWebusergroupCollectionWebusergroup = em.merge(oldWebgroupOfWebusergroupCollectionWebusergroup);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Webgroup webgroup) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Webgroup persistentWebgroup = em.find(Webgroup.class, webgroup.getGroupid());
            Collection<Webusergroup> webusergroupCollectionOld = persistentWebgroup.getWebusergroupCollection();
            Collection<Webusergroup> webusergroupCollectionNew = webgroup.getWebusergroupCollection();
            List<String> illegalOrphanMessages = null;
            for (Webusergroup webusergroupCollectionOldWebusergroup : webusergroupCollectionOld) {
                if (!webusergroupCollectionNew.contains(webusergroupCollectionOldWebusergroup)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Webusergroup " + webusergroupCollectionOldWebusergroup + " since its webgroup field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Webusergroup> attachedWebusergroupCollectionNew = new ArrayList<Webusergroup>();
            for (Webusergroup webusergroupCollectionNewWebusergroupToAttach : webusergroupCollectionNew) {
                webusergroupCollectionNewWebusergroupToAttach = em.getReference(webusergroupCollectionNewWebusergroupToAttach.getClass(), webusergroupCollectionNewWebusergroupToAttach.getWebusergroupPK());
                attachedWebusergroupCollectionNew.add(webusergroupCollectionNewWebusergroupToAttach);
            }
            webusergroupCollectionNew = attachedWebusergroupCollectionNew;
            webgroup.setWebusergroupCollection(webusergroupCollectionNew);
            webgroup = em.merge(webgroup);
            for (Webusergroup webusergroupCollectionNewWebusergroup : webusergroupCollectionNew) {
                if (!webusergroupCollectionOld.contains(webusergroupCollectionNewWebusergroup)) {
                    Webgroup oldWebgroupOfWebusergroupCollectionNewWebusergroup = webusergroupCollectionNewWebusergroup.getWebgroup();
                    webusergroupCollectionNewWebusergroup.setWebgroup(webgroup);
                    webusergroupCollectionNewWebusergroup = em.merge(webusergroupCollectionNewWebusergroup);
                    if (oldWebgroupOfWebusergroupCollectionNewWebusergroup != null && !oldWebgroupOfWebusergroupCollectionNewWebusergroup.equals(webgroup)) {
                        oldWebgroupOfWebusergroupCollectionNewWebusergroup.getWebusergroupCollection().remove(webusergroupCollectionNewWebusergroup);
                        oldWebgroupOfWebusergroupCollectionNewWebusergroup = em.merge(oldWebgroupOfWebusergroupCollectionNewWebusergroup);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = webgroup.getGroupid();
                if (findWebgroup(id) == null) {
                    throw new NonexistentEntityException("The webgroup with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Webgroup webgroup;
            try {
                webgroup = em.getReference(Webgroup.class, id);
                webgroup.getGroupid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The webgroup with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Webusergroup> webusergroupCollectionOrphanCheck = webgroup.getWebusergroupCollection();
            for (Webusergroup webusergroupCollectionOrphanCheckWebusergroup : webusergroupCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Webgroup (" + webgroup + ") cannot be destroyed since the Webusergroup " + webusergroupCollectionOrphanCheckWebusergroup + " in its webusergroupCollection field has a non-nullable webgroup field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(webgroup);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Webgroup> findWebgroupEntities() {
        return findWebgroupEntities(true, -1, -1);
    }

    public List<Webgroup> findWebgroupEntities(int maxResults, int firstResult) {
        return findWebgroupEntities(false, maxResults, firstResult);
    }

    private List<Webgroup> findWebgroupEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Webgroup.class));
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

    public Webgroup findWebgroup(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Webgroup.class, id);
        } finally {
            em.close();
        }
    }

    public int getWebgroupCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Webgroup> rt = cq.from(Webgroup.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
