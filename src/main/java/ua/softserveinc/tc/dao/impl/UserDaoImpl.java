package ua.softserveinc.tc.dao.impl;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ua.softserveinc.tc.constants.UserConstants;
import ua.softserveinc.tc.dao.UserDao;
import ua.softserveinc.tc.entity.Role;
import ua.softserveinc.tc.entity.User;
import ua.softserveinc.tc.util.Log;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {


    @Log
    private static Logger log;

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public List<User> findAllUsersByRole(Role role) {
        return entityManager
                .createQuery("from User where role = " + role.ordinal())
                .getResultList();
    }

    @Override
    public void deleteUserById(Long id){
        entityManager.createQuery("DELETE FROM User where id = " + id).executeUpdate();
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            TypedQuery<User> query = getEntityManager().createNamedQuery(UserConstants.Entity.NQ_FIND_USER_BY_EMAIL, User.class);
            return query.setParameter(UserConstants.Entity.EMAIL, email).getSingleResult();
        } catch (NoResultException e) {
            log.error("no exist", e);
            return null;
        }
    }
}
