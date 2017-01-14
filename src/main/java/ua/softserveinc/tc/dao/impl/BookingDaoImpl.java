package ua.softserveinc.tc.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.softserveinc.tc.constants.BookingConstants;
import ua.softserveinc.tc.dao.BookingDao;
import ua.softserveinc.tc.entity.Booking;
import ua.softserveinc.tc.util.BookingsCharacteristics;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * Rewritten by Sviatoslav Hryb on 05.10.2017
 */
@Repository
public class BookingDaoImpl extends BaseDaoImpl<Booking> implements BookingDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Booking> getDuplicateBookings(BookingsCharacteristics characteristics) {
        List<Booking> resultList = Collections.singletonList(new Booking());

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> criteria = builder.createQuery(Booking.class);
        Root<Booking> root = criteria.from(Booking.class);

        if (characteristics.isCorrectFotDuplicateCheck()) {

            List<Predicate> restrictions = new ArrayList<>();
            if (characteristics.hasSetRecurrentIdsOfBookings()) {
                restrictions.add(builder.or(
                        root.get(BookingConstants.Entity.RECURRENT_ID)
                                .isNull(),
                        root.get(BookingConstants.Entity.RECURRENT_ID)
                                .in(characteristics.getRecurrentIdsOfBookings()).not()
                ));

            } else if (characteristics.hasSetIdsOfBookings()) {
                restrictions.add(root.get(BookingConstants.Entity.ID_OF_BOOKING)
                        .in(characteristics.getIdsOfBookings()).not());

            }

            restrictions.addAll(Arrays.asList(
                    root.get(BookingConstants.Entity.CHILD)
                            .in(characteristics.getChildren()),
                    builder.lessThan(root.get(BookingConstants.Entity.START_TIME),
                            characteristics.getEndDateOfBookings()),
                    builder.greaterThan(root.get(BookingConstants.Entity.END_TIME),
                            characteristics.getStartDateOfBookings()),
                    root.get(BookingConstants.Entity.STATE).in(
                            (Object[]) BookingConstants.States.getActiveAndBooked())
            ));

            criteria.select(root).where(builder.and(
                    restrictions.toArray(new Predicate[restrictions.size()])));


            resultList = entityManager.createQuery(criteria).getResultList();
        }

        return resultList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookings(BookingsCharacteristics characteristics) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> criteria = builder.createQuery(Booking.class);
        Root<Booking> root = criteria.from(Booking.class);

        List<Predicate> restrictions = new ArrayList<>();
        if (characteristics.hasSetDates()) {
            restrictions.add(builder.and(
                    builder.lessThan(root.get(BookingConstants.Entity.START_TIME),
                            characteristics.getEndDateOfBookings()),
                    builder.greaterThan(root.get(BookingConstants.Entity.END_TIME),
                            characteristics.getStartDateOfBookings())
            ));
        }

        if (characteristics.hasSetOnlyStartDate()) {
            restrictions.add(builder.greaterThanOrEqualTo(root.get(BookingConstants.Entity.START_TIME),
                    characteristics.getStartDateOfBookings()));
        }

        if (characteristics.hasSetOnlyEndDate()) {
            restrictions.add(builder.lessThanOrEqualTo(root.get(BookingConstants.Entity.END_TIME),
                    characteristics.getEndDateOfBookings()));
        }

        if (characteristics.hasSetBookingsStates()) {
            restrictions.add(root.get(BookingConstants.Entity.STATE)
                    .in(characteristics.getBookingsStates()));
        }
        if (characteristics.hasSetUsers()) {
            restrictions.add(root.get(BookingConstants.Entity.USER)
                    .in(characteristics.getUsers()));
        }
        if (characteristics.hasSetRooms()) {
            restrictions.add(root.get(BookingConstants.Entity.ROOM)
                    .in(characteristics.getRooms()));
        }
        if (characteristics.hasSetChildren()) {
            restrictions.add(root.get(BookingConstants.Entity.CHILD)
                    .in(characteristics.getChildren()));
        }

        criteria.select(root).where(builder.and(
                restrictions.toArray(new Predicate[restrictions.size()])));

        criteria.orderBy(builder.asc(
                root.get(BookingConstants.Entity.START_TIME)));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public long getMaxRecurrentId() {
        Long result;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Booking> root = criteria.from(Booking.class);

        criteria.select(builder.max(root.get(BookingConstants.Entity.RECURRENT_ID)));

        result = entityManager.createQuery(criteria).getSingleResult();

        return (result != null) ? result : 0L;
    }

    @Override
    public List<Booking> getRecurrentBookingsByRecurrentId(Long recurrentId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> criteria = builder.createQuery(Booking.class);
        Root<Booking> root = criteria.from(Booking.class);

        criteria.select(root).where(builder.equal(
                root.get(BookingConstants.Entity.RECURRENT_ID), recurrentId))
                    .orderBy(builder.asc(
                        root.get(BookingConstants.Entity.START_TIME)));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public List<Booking> persistRecurrentBookings(List<Booking> bookings) {
        bookings.forEach(entityManager::persist);

        return bookings;

    }
}
