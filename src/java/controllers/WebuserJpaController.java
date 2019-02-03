/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import encryption.CryptoConverter;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.Webmessages;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import models.Webuser;
import models.Webusergroup;

/**
 *
 * @author nikolaos
 */
public class WebuserJpaController implements Serializable {

    public WebuserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Webuser webuser) {
        if (webuser.getWebmessagesCollection() == null) {
            webuser.setWebmessagesCollection(new ArrayList<Webmessages>());
        }
        if (webuser.getWebmessagesCollection1() == null) {
            webuser.setWebmessagesCollection1(new ArrayList<Webmessages>());
        }
        if (webuser.getWebusergroupCollection() == null) {
            webuser.setWebusergroupCollection(new ArrayList<Webusergroup>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Webmessages> attachedWebmessagesCollection = new ArrayList<Webmessages>();
            for (Webmessages webmessagesCollectionWebmessagesToAttach : webuser.getWebmessagesCollection()) {
                webmessagesCollectionWebmessagesToAttach = em.getReference(webmessagesCollectionWebmessagesToAttach.getClass(), webmessagesCollectionWebmessagesToAttach.getMsgid());
                attachedWebmessagesCollection.add(webmessagesCollectionWebmessagesToAttach);
            }
            webuser.setWebmessagesCollection(attachedWebmessagesCollection);
            Collection<Webmessages> attachedWebmessagesCollection1 = new ArrayList<Webmessages>();
            for (Webmessages webmessagesCollection1WebmessagesToAttach : webuser.getWebmessagesCollection1()) {
                webmessagesCollection1WebmessagesToAttach = em.getReference(webmessagesCollection1WebmessagesToAttach.getClass(), webmessagesCollection1WebmessagesToAttach.getMsgid());
                attachedWebmessagesCollection1.add(webmessagesCollection1WebmessagesToAttach);
            }
            webuser.setWebmessagesCollection1(attachedWebmessagesCollection1);
            Collection<Webusergroup> attachedWebusergroupCollection = new ArrayList<Webusergroup>();
            for (Webusergroup webusergroupCollectionWebusergroupToAttach : webuser.getWebusergroupCollection()) {
                webusergroupCollectionWebusergroupToAttach = em.getReference(webusergroupCollectionWebusergroupToAttach.getClass(), webusergroupCollectionWebusergroupToAttach.getWebusergroupPK());
                attachedWebusergroupCollection.add(webusergroupCollectionWebusergroupToAttach);
            }
            webuser.setWebusergroupCollection(attachedWebusergroupCollection);
            em.persist(webuser);
            for (Webmessages webmessagesCollectionWebmessages : webuser.getWebmessagesCollection()) {
                Webuser oldFromuseridOfWebmessagesCollectionWebmessages = webmessagesCollectionWebmessages.getFromuserid();
                webmessagesCollectionWebmessages.setFromuserid(webuser);
                webmessagesCollectionWebmessages = em.merge(webmessagesCollectionWebmessages);
                if (oldFromuseridOfWebmessagesCollectionWebmessages != null) {
                    oldFromuseridOfWebmessagesCollectionWebmessages.getWebmessagesCollection().remove(webmessagesCollectionWebmessages);
                    oldFromuseridOfWebmessagesCollectionWebmessages = em.merge(oldFromuseridOfWebmessagesCollectionWebmessages);
                }
            }
            for (Webmessages webmessagesCollection1Webmessages : webuser.getWebmessagesCollection1()) {
                Webuser oldTouseridOfWebmessagesCollection1Webmessages = webmessagesCollection1Webmessages.getTouserid();
                webmessagesCollection1Webmessages.setTouserid(webuser);
                webmessagesCollection1Webmessages = em.merge(webmessagesCollection1Webmessages);
                if (oldTouseridOfWebmessagesCollection1Webmessages != null) {
                    oldTouseridOfWebmessagesCollection1Webmessages.getWebmessagesCollection1().remove(webmessagesCollection1Webmessages);
                    oldTouseridOfWebmessagesCollection1Webmessages = em.merge(oldTouseridOfWebmessagesCollection1Webmessages);
                }
            }
            for (Webusergroup webusergroupCollectionWebusergroup : webuser.getWebusergroupCollection()) {
                Webuser oldWebuserOfWebusergroupCollectionWebusergroup = webusergroupCollectionWebusergroup.getWebuser();
                webusergroupCollectionWebusergroup.setWebuser(webuser);
                webusergroupCollectionWebusergroup = em.merge(webusergroupCollectionWebusergroup);
                if (oldWebuserOfWebusergroupCollectionWebusergroup != null) {
                    oldWebuserOfWebusergroupCollectionWebusergroup.getWebusergroupCollection().remove(webusergroupCollectionWebusergroup);
                    oldWebuserOfWebusergroupCollectionWebusergroup = em.merge(oldWebuserOfWebusergroupCollectionWebusergroup);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Webuser webuser) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Webuser persistentWebuser = em.find(Webuser.class, webuser.getUserid());
            Collection<Webmessages> webmessagesCollectionOld = persistentWebuser.getWebmessagesCollection();
            Collection<Webmessages> webmessagesCollectionNew = webuser.getWebmessagesCollection();
            Collection<Webmessages> webmessagesCollection1Old = persistentWebuser.getWebmessagesCollection1();
            Collection<Webmessages> webmessagesCollection1New = webuser.getWebmessagesCollection1();
            Collection<Webusergroup> webusergroupCollectionOld = persistentWebuser.getWebusergroupCollection();
            Collection<Webusergroup> webusergroupCollectionNew = webuser.getWebusergroupCollection();
            List<String> illegalOrphanMessages = null;
            for (Webmessages webmessagesCollectionOldWebmessages : webmessagesCollectionOld) {
                if (!webmessagesCollectionNew.contains(webmessagesCollectionOldWebmessages)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Webmessages " + webmessagesCollectionOldWebmessages + " since its fromuserid field is not nullable.");
                }
            }
            for (Webusergroup webusergroupCollectionOldWebusergroup : webusergroupCollectionOld) {
                if (!webusergroupCollectionNew.contains(webusergroupCollectionOldWebusergroup)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Webusergroup " + webusergroupCollectionOldWebusergroup + " since its webuser field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Webmessages> attachedWebmessagesCollectionNew = new ArrayList<Webmessages>();
            for (Webmessages webmessagesCollectionNewWebmessagesToAttach : webmessagesCollectionNew) {
                webmessagesCollectionNewWebmessagesToAttach = em.getReference(webmessagesCollectionNewWebmessagesToAttach.getClass(), webmessagesCollectionNewWebmessagesToAttach.getMsgid());
                attachedWebmessagesCollectionNew.add(webmessagesCollectionNewWebmessagesToAttach);
            }
            webmessagesCollectionNew = attachedWebmessagesCollectionNew;
            webuser.setWebmessagesCollection(webmessagesCollectionNew);
            Collection<Webmessages> attachedWebmessagesCollection1New = new ArrayList<Webmessages>();
            for (Webmessages webmessagesCollection1NewWebmessagesToAttach : webmessagesCollection1New) {
                webmessagesCollection1NewWebmessagesToAttach = em.getReference(webmessagesCollection1NewWebmessagesToAttach.getClass(), webmessagesCollection1NewWebmessagesToAttach.getMsgid());
                attachedWebmessagesCollection1New.add(webmessagesCollection1NewWebmessagesToAttach);
            }
            webmessagesCollection1New = attachedWebmessagesCollection1New;
            webuser.setWebmessagesCollection1(webmessagesCollection1New);
            Collection<Webusergroup> attachedWebusergroupCollectionNew = new ArrayList<Webusergroup>();
            for (Webusergroup webusergroupCollectionNewWebusergroupToAttach : webusergroupCollectionNew) {
                webusergroupCollectionNewWebusergroupToAttach = em.getReference(webusergroupCollectionNewWebusergroupToAttach.getClass(), webusergroupCollectionNewWebusergroupToAttach.getWebusergroupPK());
                attachedWebusergroupCollectionNew.add(webusergroupCollectionNewWebusergroupToAttach);
            }
            webusergroupCollectionNew = attachedWebusergroupCollectionNew;
            webuser.setWebusergroupCollection(webusergroupCollectionNew);
            webuser = em.merge(webuser);
            for (Webmessages webmessagesCollectionNewWebmessages : webmessagesCollectionNew) {
                if (!webmessagesCollectionOld.contains(webmessagesCollectionNewWebmessages)) {
                    Webuser oldFromuseridOfWebmessagesCollectionNewWebmessages = webmessagesCollectionNewWebmessages.getFromuserid();
                    webmessagesCollectionNewWebmessages.setFromuserid(webuser);
                    webmessagesCollectionNewWebmessages = em.merge(webmessagesCollectionNewWebmessages);
                    if (oldFromuseridOfWebmessagesCollectionNewWebmessages != null && !oldFromuseridOfWebmessagesCollectionNewWebmessages.equals(webuser)) {
                        oldFromuseridOfWebmessagesCollectionNewWebmessages.getWebmessagesCollection().remove(webmessagesCollectionNewWebmessages);
                        oldFromuseridOfWebmessagesCollectionNewWebmessages = em.merge(oldFromuseridOfWebmessagesCollectionNewWebmessages);
                    }
                }
            }
            for (Webmessages webmessagesCollection1OldWebmessages : webmessagesCollection1Old) {
                if (!webmessagesCollection1New.contains(webmessagesCollection1OldWebmessages)) {
                    webmessagesCollection1OldWebmessages.setTouserid(null);
                    webmessagesCollection1OldWebmessages = em.merge(webmessagesCollection1OldWebmessages);
                }
            }
            for (Webmessages webmessagesCollection1NewWebmessages : webmessagesCollection1New) {
                if (!webmessagesCollection1Old.contains(webmessagesCollection1NewWebmessages)) {
                    Webuser oldTouseridOfWebmessagesCollection1NewWebmessages = webmessagesCollection1NewWebmessages.getTouserid();
                    webmessagesCollection1NewWebmessages.setTouserid(webuser);
                    webmessagesCollection1NewWebmessages = em.merge(webmessagesCollection1NewWebmessages);
                    if (oldTouseridOfWebmessagesCollection1NewWebmessages != null && !oldTouseridOfWebmessagesCollection1NewWebmessages.equals(webuser)) {
                        oldTouseridOfWebmessagesCollection1NewWebmessages.getWebmessagesCollection1().remove(webmessagesCollection1NewWebmessages);
                        oldTouseridOfWebmessagesCollection1NewWebmessages = em.merge(oldTouseridOfWebmessagesCollection1NewWebmessages);
                    }
                }
            }
            for (Webusergroup webusergroupCollectionNewWebusergroup : webusergroupCollectionNew) {
                if (!webusergroupCollectionOld.contains(webusergroupCollectionNewWebusergroup)) {
                    Webuser oldWebuserOfWebusergroupCollectionNewWebusergroup = webusergroupCollectionNewWebusergroup.getWebuser();
                    webusergroupCollectionNewWebusergroup.setWebuser(webuser);
                    webusergroupCollectionNewWebusergroup = em.merge(webusergroupCollectionNewWebusergroup);
                    if (oldWebuserOfWebusergroupCollectionNewWebusergroup != null && !oldWebuserOfWebusergroupCollectionNewWebusergroup.equals(webuser)) {
                        oldWebuserOfWebusergroupCollectionNewWebusergroup.getWebusergroupCollection().remove(webusergroupCollectionNewWebusergroup);
                        oldWebuserOfWebusergroupCollectionNewWebusergroup = em.merge(oldWebuserOfWebusergroupCollectionNewWebusergroup);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = webuser.getUserid();
                if (findWebuser(id) == null) {
                    throw new NonexistentEntityException("The webuser with id " + id + " no longer exists.");
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
            Webuser webuser;
            try {
                webuser = em.getReference(Webuser.class, id);
                webuser.getUserid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The webuser with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Webmessages> webmessagesCollectionOrphanCheck = webuser.getWebmessagesCollection();
            for (Webmessages webmessagesCollectionOrphanCheckWebmessages : webmessagesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Webuser (" + webuser + ") cannot be destroyed since the Webmessages " + webmessagesCollectionOrphanCheckWebmessages + " in its webmessagesCollection field has a non-nullable fromuserid field.");
            }
            Collection<Webusergroup> webusergroupCollectionOrphanCheck = webuser.getWebusergroupCollection();
            for (Webusergroup webusergroupCollectionOrphanCheckWebusergroup : webusergroupCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Webuser (" + webuser + ") cannot be destroyed since the Webusergroup " + webusergroupCollectionOrphanCheckWebusergroup + " in its webusergroupCollection field has a non-nullable webuser field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Webmessages> webmessagesCollection1 = webuser.getWebmessagesCollection1();
            for (Webmessages webmessagesCollection1Webmessages : webmessagesCollection1) {
                webmessagesCollection1Webmessages.setTouserid(null);
                webmessagesCollection1Webmessages = em.merge(webmessagesCollection1Webmessages);
            }
            em.remove(webuser);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Webuser> findWebuserEntities() {
        return findWebuserEntities(true, -1, -1);
    }

    public List<Webuser> findWebuserEntities(int maxResults, int firstResult) {
        return findWebuserEntities(false, maxResults, firstResult);
    }

    private List<Webuser> findWebuserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Webuser.class));
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

    public Webuser findWebuser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Webuser.class, id);
        } finally {
            em.close();
        }
    }
    
    public Webuser findWebuserByUsername(String username) {
        Webuser user = null;
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT w FROM Webuser w WHERE w.username = :username", Webuser.class);
            q.setParameter("username", username);
            user = (Webuser) q.getSingleResult();
        } finally {
            em.close();
        }
        return user;
    }

    public int getWebuserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Webuser> rt = cq.from(Webuser.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public boolean checkLogin(String username, String password) {
        boolean answer = false;
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT w FROM Webuser w WHERE w.username = :username AND w.userpwd = :password", Webuser.class);
            q.setParameter("username", username);
            q.setParameter("password", CryptoConverter.encrypt(password));
            List<Webuser> userList = q.getResultList();
            if (!userList.isEmpty()) {
                answer = true;
            }
        } finally {
            em.close();
        }

        return answer;
    }

    public Webuser checkLogin2(String username, String password) {
        Webuser user = null;
        EntityManager em = getEntityManager();
        try {
            //Query q= em.createNamedQuery("Webuser.checkLogin",Webuser.class);
            Query q = em.createQuery("SELECT w FROM Webuser w WHERE w.username = :username AND w.userpwd = :password", Webuser.class);
            q.setParameter("username", username);
            q.setParameter("password", CryptoConverter.encrypt(password));
            user = (Webuser) q.getSingleResult();
        } catch (Exception ex) {
            user = null;
        } finally {
            em.close();
        }
        return user;
    }
}
