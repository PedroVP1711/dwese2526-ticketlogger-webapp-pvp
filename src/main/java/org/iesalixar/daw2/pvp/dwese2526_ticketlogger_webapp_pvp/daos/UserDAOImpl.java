package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UserDAOImpl implements UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    // --- Métodos de Listado y Conteo ---

    // UserDAOImpl.java
// UserDAOImpl.java

    @Override
    @Transactional(readOnly = true)
    public List<User> listAllUsers() {
        try {
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u ORDER BY u.id", User.class);
            return query.getResultList();
        } catch (jakarta.persistence.PersistenceException pe) {
            // Atrapa la excepción más específica de JPA.
            logger.error("Error de Persistencia (JPA) al listar usuarios. La causa raíz es:", pe);
            throw pe; // Volvemos a lanzar la excepción para que Spring la capture y haga rollback,
            // pero ahora la tenemos registrada.
        } catch (Exception e) {
            logger.error("Error GENÉRICO al listar usuarios.", e);
            throw e; // Volvemos a lanzar la excepción.
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> listUsersPage(int page, int size, String sortField, String sortDir) {
        if (page < 0 || size <= 0) {
            logger.warn("Parámetros de paginación inválidos: página={}, tamaño={}", page, size);
            return List.of();
        }

        String jpql = "SELECT u FROM User u ORDER BY u." + sortField + " " + sortDir;

        try {
            TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
            query.setFirstResult(page * size); // Índice de inicio = página * tamaño
            query.setMaxResults(size);         // Número máximo de resultados (tamaño de la página)
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error al listar usuarios paginados. Campo de ordenación: {}", sortField, e);
            return List.of();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsers() {
        try {
            TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(u) FROM User u", Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error al contar el número de usuarios.", e);
            return 0;
        }
    }

    // --- Métodos de Inserción ---

    @Override
    @Transactional
    public void insertUser(User user) {
        if (user == null) {
            logger.error("No se puede insertar un usuario nulo.");
            return;
        }
        try {
            entityManager.persist(user);
            logger.info("Usuario insertado correctamente con ID: {}", user.getId());
        } catch (Exception e) {
            logger.error("Error al insertar el usuario: {}", user.getEmail(), e);
        }
    }

    // --- Métodos de Actualización y Eliminación ---

    @Override
    @Transactional
    public void updateUser(User user) {
        if (user == null || user.getId() == null) {
            logger.error("No se puede actualizar un usuario nulo o sin ID.");
            return;
        }
        try {
            entityManager.merge(user);
            logger.info("Usuario con ID {} actualizado correctamente.", user.getId());
        } catch (Exception e) {
            logger.error("Error al actualizar el usuario con ID {}: {}", user.getId(), e);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (id == null) return;
        try {
            User user = entityManager.find(User.class, id);
            if (user != null) {
                entityManager.remove(user);
                logger.info("Usuario con ID {} eliminado correctamente.", id);
            } else {
                logger.warn("Intento de eliminar usuario con ID {} que no existe.", id);
            }
        } catch (Exception e) {
            logger.error("Error al eliminar el usuario con ID {}: {}", id, e);
        }
    }

    // --- Métodos de Búsqueda por ID y Email ---

    public User findById(Long id) {
        if (id == null) return null;
        return entityManager.find(User.class, id);
    }

    public User getUserById(Long id) {
        return findById(id);
    }

    public User getUserByEmail(String email) {
        if (email == null) return null;

        String jpql = "SELECT u FROM User u WHERE u.email = :email";
        return entityManager.createQuery(jpql, User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }


    @Override
    public boolean existsUserByEmail(String email) {
        logger.info("Checking if user with email: {} exits", email);
        String hql = "SELECT COUNT(u) FROM User u WHERE UPPER(u.email) = :email";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("email", email.toUpperCase())
                .getSingleResult();
        boolean exists = count !=null && count > 0;
        logger.info("User with email: {} exists: {}", email, exists);
        return exists;
        }



    @Override
    public boolean existsUserByEmailAndNotId(String email, Long id) {
        logger.info("Checking if user with email: {} exists excluding id: {}", email, id);
        String hql = "SELECT COUNT(u) FROM User U WHERE UPPER(u.email) = :email AND u.id != :id";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("email", email.toUpperCase())
                .setParameter("id", id)
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("User with email: {} exists excluding id {}: {}", email, id, exists);
        return exists;
        }
    }