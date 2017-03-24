package ua.softserveinc.tc.dao.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.softserveinc.tc.dao.DayDiscountDao;
import ua.softserveinc.tc.entity.DayDiscount;
import ua.softserveinc.tc.util.DateUtil;
import ua.softserveinc.tc.util.Log;

@Repository
public class DayDiscountDaoImp extends BaseDaoImpl<DayDiscount> implements DayDiscountDao {

  @Log
  public static Logger log;

  @PersistenceContext
  protected EntityManager entityManager;

  private CriteriaBuilder builder;

  @Override
  public DayDiscount getDayDiscountById(long id) {
    CriteriaQuery<DayDiscount> query = builder.createQuery(DayDiscount.class);
    Root<DayDiscount> root = query.from(DayDiscount.class);
    query.select(root).where(
        builder.equal(root.get("id"), id));
    return entityManager.createQuery(query).getSingleResult();
  }

  @Override
  public List<DayDiscount> getDayDiscountForCurrentDays(Date startDate, Date endDate) {
    CriteriaQuery<DayDiscount> query = builder.createQuery(DayDiscount.class);
    Root<DayDiscount> root = query.from(DayDiscount.class);
    query.select(root).where(
        builder.not(builder.lessThan(root.get("endDate"),startDate)),
        builder.not(builder.greaterThan(root.get("startDate"),endDate))
    );
    return entityManager.createQuery(query).getResultList();
  }

  @Override
  @Transactional
  public void updateDayDiscountById(DayDiscount newDayDiscount) {
    CriteriaUpdate<DayDiscount> update = builder.createCriteriaUpdate(DayDiscount.class);
    Root<DayDiscount> root = update.from(DayDiscount.class);
    update.set("reason", newDayDiscount.getReason());
    update.set("value", newDayDiscount.getValue());
    update.set("startDate", newDayDiscount.getStartDate());
    update.set("endDate", newDayDiscount.getEndDate());
    update.where(builder.equal(root.get("id"), newDayDiscount.getId()));
    entityManager.createQuery(update).executeUpdate();
  }

  @Override
  @Transactional
  public void updateDayDiscountState(DayDiscount newDayDiscount) {
    CriteriaUpdate<DayDiscount> update = builder.createCriteriaUpdate(DayDiscount.class);
    Root<DayDiscount> root = update.from(DayDiscount.class);
    update.set("active", newDayDiscount.getActive());
    update.where(builder.equal(root.get("id"), newDayDiscount.getId()));
    entityManager.createQuery(update).executeUpdate();
  }

  @PostConstruct
  private void createDefaultState() {
    builder = entityManager.getCriteriaBuilder();
  }
}
