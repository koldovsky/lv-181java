package ua.softserveinc.tc.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.softserveinc.tc.constants.AbonnementConstants;
import ua.softserveinc.tc.dao.AbonnementDao;
import ua.softserveinc.tc.entity.Abonnement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class AbonnementDaoImpl extends BaseDaoImpl<Abonnement> implements AbonnementDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void updateByActiveState(long id, boolean active) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Abonnement> criteria = builder.createCriteriaUpdate(Abonnement.class);
        Root root = criteria.from(Abonnement.class);
        criteria.set(AbonnementConstants.Hibernate.ABONNEMENT_IS_ACTIVE, active);
        criteria.where(builder.equal(root.get(AbonnementConstants.Hibernate.ABONNEMENT_ID), id));
        entityManager.createQuery(criteria).executeUpdate();
    }

    @Override
    public Optional<Long> getMaxPrice() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Abonnement> root = criteria.from(Abonnement.class);

        criteria.select(builder.max(root.get("price").as(Long.class)));

        return Optional.of(entityManager.createQuery(criteria).getSingleResult());
    }

    @Override
    public Optional<Long> getMinPrice() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Abonnement> root = criteria.from(Abonnement.class);

        criteria.select(builder.min(root.get("price").as(Long.class)));

        return Optional.of(entityManager.createQuery(criteria).getSingleResult());
    }
}
