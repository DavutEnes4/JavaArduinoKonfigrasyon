package tr.com.my_app.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tr.com.my_app.model.PinConfig;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class PinConfigDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public Object loadObject(Class clazz, Serializable id) {
        return getSession().get(clazz, id);
    }

    public boolean saveOrUpdate(Object object) {
        boolean success = true;
        try {
            getSession().persist(object);
        }
        catch (Exception e) {
            success = false;
            e.printStackTrace();
        }

        return success;
    }

    public boolean removeObject(Object object) {
        boolean success = true;
        try {
            getSession().delete(object);
        }
        catch (Exception e) {
            success = false;
            e.printStackTrace();
        }

        return success;
    }

    public List<PinConfig> loadPinConfig(Integer start, Integer limit, String query) {
        Session session = getSession();
        CriteriaBuilder  criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<PinConfig> criteriaQuery = criteriaBuilder.createQuery(PinConfig.class);
        Root<PinConfig> root = criteriaQuery.from(PinConfig.class);

        criteriaQuery.select(root);

        if (query != null && !query.equals("")) {
            Predicate predicate;
            if (StringUtils.isNumeric(query)) {
                Predicate byId = criteriaBuilder.equal(root.get("id"), Integer.parseInt(query));
                Predicate byAdi = criteriaBuilder.like(root.get("adi"), "%" + query + "%");
                predicate = criteriaBuilder.or(byId, byAdi);
            } else {
                predicate = criteriaBuilder.like(root.get("adi"), "%" + query + "%");
            }
            criteriaQuery.where(predicate);
        }


        Query<PinConfig> dbQuery = session.createQuery(criteriaQuery);
        dbQuery.setFirstResult(start);
        dbQuery.setMaxResults(limit);
        List<PinConfig> pinConfigList = dbQuery.getResultList();

        return pinConfigList;
    }

    public Long getTotalCount(String query) {
        Session session = getSession();
        CriteriaBuilder  criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<PinConfig> root = criteriaQuery.from(PinConfig.class);

        criteriaQuery.select(criteriaBuilder.count(root));

        if (query != null && !query.equals("")) {
            if (StringUtils.isNumeric(query)) {
                Predicate predicate = criteriaBuilder.equal(root.get("id"), Integer.parseInt(query));
                criteriaQuery.where(predicate);
            } else {
                Predicate predicate = criteriaBuilder.like(root.get("adi"), "%"+query+"%");
                criteriaQuery.where(predicate);
            }
        }

        Query<Long> dbQuery = session.createQuery(criteriaQuery);
        Long totalCount = dbQuery.getSingleResult();

        return totalCount;
    }
}
