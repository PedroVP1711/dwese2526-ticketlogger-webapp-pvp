package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.UserProfile;
import org.springframework.stereotype.Repository;

/**
 * DAO para la entidad {@link UserProfile}.
 *
 * Pensado para la funcionalidad de gestión de perfil de usuario ("Mi perfil"),
 * donde normalmente se trabaja con un único perfil asociado a un usuario.
 *
 * Implementación usuario Hibernate/JPA a través de {@link EntityManager}
 */

@Repository
@Transactional
public class UserProfileDAOImpl implements UserProfileDAO {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Obtiene el perfil asociado a un usuario por su id.
     *
     * @param userId id del usuario (coincide con user_profiles.user_id).
     * @return el perfil del usuario, o null si no existe.
     */
    @Override
    @Transactional
    public UserProfile getUserProfileByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return entityManager.find(UserProfile.class, userId);
    }

    /**
     * Inserta o actualiza el perfil según exista ya en base de datos.
     * <p>
     * Puede implementarse con lógica "upsert" en el servicio o repositorio
     * subyacente (por ejemplo, usando save(...) de Spring Data JPA).
     *
     * @param userProfile entidad a guardar.
     */
    @Override
    public void saveOrUpdateUserProfile(UserProfile userProfile) {
        if (userProfile == null) {
            return;
        }
        if (userProfile.getId() == null || !existsUserProfileByUserId(userProfile.getId())) {
            entityManager.persist(userProfile);
        } else {
            entityManager.merge(userProfile);
        }
    }

    @Override
    @Transactional
    public boolean existsUserProfileByUserId(Long userId) {
        if (userId == null) {
            return false;
        }

        String jpql = "SELECT COUNT(up) FROM UserProfile up WHERE up.id = :userId";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("userId", userId);
        Long count = query.getSingleResult();

        return count != null && count > 0;
    }
}