package tr.com.my_app.dao;

import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tr.com.my_app.model.DevreKarti;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;

@Repository
@Transactional
public class DevreKartiDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public Object loadObject(Class clazz, Serializable id) {
        return getSession().load(clazz, id);
    }

    public boolean saveOrUpdate(Object object) {
        boolean success = true;
        try {
            getSession().persist(object);
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    public boolean removeObject(Object object) {
        boolean success = true;
        try {
            getSession().delete(object);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    public List<DevreKarti> loadDevrekarti(Integer start, Integer limit, String query) {
        Session session = getSession();
        CriteriaBuilder  criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<DevreKarti> criteriaQuery = criteriaBuilder.createQuery(DevreKarti.class);
        Root<DevreKarti> root = criteriaQuery.from(DevreKarti.class);

        criteriaQuery.select(root);

        if(query != null && !query.equals("")) {
            if(StringUtils.isNumeric(query)) {
                Predicate predicateNo = criteriaBuilder.equal(root.get("id"), Integer.parseInt(query));
                criteriaQuery.where(predicateNo);
            } else {
              Predicate predicate = criteriaBuilder.like(root.get("adi"), "%" + query + "%");
              criteriaQuery.where(predicate);
            }
        }

        Query<DevreKarti> dbQuery = session.createQuery(criteriaQuery);
        dbQuery.setFirstResult(start);
        dbQuery.setMaxResults(limit);

        List<DevreKarti> kartlar = dbQuery.getResultList();
        return kartlar;
    }

    public Long getTotalCount(String query) {
        Session session = getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<DevreKarti> root = criteriaQuery.from(DevreKarti.class);

        criteriaQuery.select(criteriaBuilder.count(root));

        if(query != null && !query.equals("")) {
            if(StringUtils.isNumeric(query)) {
                Predicate predicateNo = criteriaBuilder.equal(root.get("id"), Integer.parseInt(query));
                criteriaQuery.where(predicateNo);
            } else {
                Predicate predicate = criteriaBuilder.like(root.get("adi"), "%" + query + "%");
                criteriaQuery.where(predicate);
            }
        }

        Query<Long> dbQuery = session.createQuery(criteriaQuery);
        Long totalCount = dbQuery.getSingleResult();
        return totalCount;
    }

    public DevreKarti loadWithPins(Long kartId) {
        Session session = getSession();
        String hql = "SELECT d FROM DevreKarti d LEFT JOIN FETCH d.pinler WHERE d.id = :id";
        return session.createQuery(hql, DevreKarti.class)
                .setParameter("id", kartId)
                .uniqueResult();
    }

}
