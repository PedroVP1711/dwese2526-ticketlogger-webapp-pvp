package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository; // <-- CRÍTICO
import org.springframework.transaction.annotation.Transactional; // <-- CRÍTICO

@Repository // <--- ESTA LÍNEA DEBE EXISTIR
@Transactional
public class UserProfileDAOImpl implements UserProfileDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public UserProfile findByUserId(Long userId) {
        try {
            TypedQuery<UserProfile> query = entityManager.createQuery(
                    "SELECT up FROM UserProfile up WHERE up.user.id = :userId", UserProfile.class);
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            logger.error("Error al buscar perfil por userId {}: {}", userId, e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfile findByProfileId(Long id) {
        if (id == null) return null;
        return entityManager.find(UserProfile.class, id);
    }

    @Override
    @Transactional
    public void save(UserProfile profile) {
        if (profile != null) {
            entityManager.persist(profile);
        }
    }

    @Override
    @Transactional
    public UserProfile update(UserProfile profile) {
        if (profile == null) return null;
        return entityManager.merge(profile);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        UserProfile profile = findByProfileId(id);
        if (profile != null) {
            entityManager.remove(profile);
        }
    }
}