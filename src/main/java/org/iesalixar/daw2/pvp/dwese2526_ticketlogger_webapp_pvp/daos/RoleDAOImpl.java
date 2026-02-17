package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Role;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@Transactional
public class RoleDAOImpl implements RoleDAO {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(RoleDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    public List<Role> listAllRoles() {
        logger.info("Listing all roles form the database");
        String hql = "SELECT r FROM Role r ORDER BY r.name";
        List<Role> roles = entityManager.createQuery(hql, Role.class).getResultList();
        logger.info("Retrieved {} role from the database.");
        return roles;
    }

    @Override
    public List<Role> findAllByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            logger.info("findAllByIds called with null or empty ids. Returning empty list.");
            return List.of();
        }
        logger.info("Finding roles by ids: {}");
        String hql = "SELECT r FROM Role r WHERE r.id IN :ids";
        List<Role> roles = entityManager.createQuery(hql, Role.class)
                .setParameter("ids",ids)
                .getResultList();
        logger.info("Found {} roles matching the given ids.");
        return roles;
    }
}
