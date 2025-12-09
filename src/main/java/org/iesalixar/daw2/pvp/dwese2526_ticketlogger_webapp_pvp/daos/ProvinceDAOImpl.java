package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Transactional
public class ProvinceDAOImpl implements ProvinceDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Province> listAllProvinces() {
        logger.info("Listando todas las provincias con sus regiones");
        String hql = "SELECT p FROM Province p JOIN FETCH p.region ORDER BY p.id ASC";
        List<Province> provinces = entityManager.createQuery(hql, Province.class).getResultList();
        logger.info("Retrived {} provinces from the database", provinces.size());
        return provinces;
    }
    /**
     * Verifica si existe una provincia con el código especificado.
     * @param code código de la provincia a verificar.
     * @return true si existe, false en caso contrario.
     */
    @Override
    public boolean existsProvinceByCode(String code) {
        logger.info("Checking if province with code: {} exists", code);
        String hql = "SELECT COUNT(p) FROM Province p WHERE UPPER(p.code) = :code";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("code", code.toUpperCase())
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Province with code: {} exists: {}", code, exists);
        return exists;
    }

    /**
     * Inserta una nueva provincia en la base de datos.
     * @param province Provincia a insertar (debe incluir región con id).
     */
    @Override
    public void insertProvince(Province province) {
        logger.info("Inserting province with code: {}, name: {}, regionId: {}",
                province.getCode(),
                province.getName(),
                province.getRegion() != null ? province.getRegion().getId() : null
        );
        entityManager.persist(province);
        logger.info("Inserted province with id: {}", province.getId());
    }

    /**
     * Recupera una provincia por su ID (incluyendo su región).
     * @param id ID de la provincia a recuperar
     * @return Provincia encontrada (con su región) o null si no existe
     */
    @Override
    public Province getProvinceById(Long id) {
        logger.info("Retrieving province by id: {}", id);
        Province province = entityManager.find(Province.class, id);
        if (province != null) {
            logger.info("Provinces retrived: {} - {}", province.getCode(), province.getName());
        } else {
            logger.warn("No provinces found with id: {}", id);
        }
        return province;
    }

    /**
     * Verifica si existe una provincia con el código especificado,
     * excluyendo una provincia con un ID específico.
     * @param code código de la provincia a verificar.
     * @param id ID de la provincia a excluir.
     * @return true si existe otra provincia con ese código, false en caso contrario.
     */
    @Override
    public boolean existsProvinceByCodeAndNotId(String code, Long id) {
        logger.info("Checking if province with code: {} exists excluding id: {}", code, id);
        String hql = "SELECT COUNT(p) FROM Province p WHERE UPPER(p.code) = :code AND p.id != :id";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("code", code.toUpperCase())
                .setParameter("id",id)
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Province with code: {} exists excluding id {}: {}", code, id, exists);
        return exists;
    }

    /**
     * Actualiza una provincia existente en la base de datos.
     * @param province Provincia a actualizar (debe incluir id y región con id).
     */
    @Override
    public void updateProvince(Province province) {
        logger.info("Updating province with id: {}", province.getId());
        entityManager.merge(province);
        logger.info("Updated province with id: {}", province.getId());
    }

    /**
     * Elimina una provincia de labase de datos
     * @param id ID de la provincia a eliminar
     */
    @Override
    public void deleteProvince(Long id) {
        logger.info("Deleting province with id: {}", id);
        Province province = entityManager.find(Province.class, id);
        if (province != null) {
            entityManager.remove(province);
            logger.info("Deleted province with id: {}",id);
        } else {
            logger.warn("Province with id {} not found", id);
        }
    }

    /**
     * Lista una página de provincias con su región asociada.
     *
     * @param page página actual (0-based)
     * @param size número de elementos por página
     * @return lista de provincias para esa página
     */
    public List<Province> listProvincesPage(int page, int size) {
        logger.info("Listing provinces page={}, size={} from the database.", page, size);

        int offset = page * size;

        String hql = "SELECT p FROM Province p JOIN FETCH p.region ORDER BY p.name";
        return entityManager.createQuery(hql, Province.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    /**
     * Devuelve el número total de provincias.
     */
    public long countProvinces() {
        String hql = "SELECT COUNT(p) FROM Province p";
        Long total = entityManager.createQuery(hql, Long.class).getSingleResult();
        return (total != null) ? total : 0L;
    }
}

