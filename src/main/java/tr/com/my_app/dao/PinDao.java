package tr.com.my_app.dao;

import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tr.com.my_app.model.Pin;

import java.util.List;

@Repository
@Transactional
public class PinDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // Tüm pinleri listele
    public List<Pin> findAll() {
        Session session = getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Pin> cq = cb.createQuery(Pin.class);
        Root<Pin> root = cq.from(Pin.class);
        cq.select(root);
        Query<Pin> query = session.createQuery(cq);
        return query.getResultList();
    }

    // ID ile pin bul
    public Pin findById(Long id) {
        Session session = getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Pin> cq = cb.createQuery(Pin.class);
        Root<Pin> root = cq.from(Pin.class);
        Predicate p = cb.equal(root.get("id"), id);
        cq.select(root).where(p);
        Query<Pin> query = session.createQuery(cq);
        return query.uniqueResult();
    }

    // Pin adına göre ara (tam eşleşme)
    public List<Pin> findByAdi(String adi) {
        Session session = getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Pin> cq = cb.createQuery(Pin.class);
        Root<Pin> root = cq.from(Pin.class);
        Predicate p = cb.equal(root.get("adi"), adi);
        cq.select(root).where(p);
        Query<Pin> query = session.createQuery(cq);
        return query.getResultList();
    }

    // Pin ekle/güncelle
    public void saveOrUpdate(Pin pin) {
        getSession().saveOrUpdate(pin);
    }

    // Pin sil
    public void delete(Long id) {
        Pin pin = findById(id);
        if (pin != null) {
            getSession().delete(pin);
        }
    }

    // (Ekstra) Açıklama diline göre pin listele (JOIN ile PinAciklama’ya bağlanabilirsin)
    // (Örnek: Türkçe açıklaması olan pinler)
    public List<Pin> findByAciklamaDil(String dil) {
        Session session = getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Pin> cq = cb.createQuery(Pin.class);
        Root<Pin> root = cq.from(Pin.class);
        // JOIN ile PinAciklama'ya bağlan
        Root<Pin> pinRoot = cq.from(Pin.class);
        Join<Object, Object> aciklamaJoin = pinRoot.join("aciklamalar");
        Predicate p = cb.equal(aciklamaJoin.get("dil"), dil);
        cq.select(pinRoot).where(p).distinct(true);
        Query<Pin> query = session.createQuery(cq);
        return query.getResultList();
    }
}
