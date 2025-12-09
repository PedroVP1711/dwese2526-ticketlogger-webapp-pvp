package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery; // Necesaria para consultas con tipos
import jakarta.persistence.criteria.CriteriaBuilder; // Necesaria para la ordenación dinámica
import jakarta.persistence.criteria.CriteriaQuery; // Necesaria para la ordenación dinámica
import jakarta.persistence.criteria.Order; // Necesaria para la ordenación dinámica
import jakarta.persistence.criteria.Root; // Necesaria para la ordenación dinámica
import jakarta.transaction.Transactional;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class RegionDAOImpl implements RegionDAO {

    private static final Logger logger = LoggerFactory.getLogger(RegionDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Region> listAllRegions() {
        logger.info("Entrando en el metodo listAllRegions");
        String hql = "SELECT r FROM Region r ORDER BY r.name"; // Añadido ORDER BY para consistencia
        List<Region> regions = entityManager.createQuery(hql, Region.class).getResultList();
        logger.info("Saliendo del metodo listAllRegions con {} comunidades autonomas", regions.size());
        return regions;
    }

    @Override
    public void insertRegion(Region region) {
        logger.info("Entando en el metodo insertRegion");
        entityManager.persist(region);
        logger.info("Saliendo del metodo insertRegion");
    }

    @Override
    public void updateRegion(Region region) {
        logger.info("Entrando en el metodo updateRegion");
        entityManager.merge(region);
        logger.info("Region actualizada");
    }

    @Override
    public void deleteRegion(Long id) {
        logger.info("Eliminando la region con id {}", id);
        Region region = entityManager.find(Region.class, id);
        if (region != null){
            entityManager.remove(region);
            logger.info("Region con id {} eliminada.", id);
        }else {
            logger.warn("Region con id {} no encontrada", id);
        }
    }


    /**
     * Recupera una región por su ID.
     * @param id ID de la región a recuperar
     * @return Región encontrada o null si no existe
     */
    @Override
    public Region getRegionById(Long id) {
        logger.info("Retrieving region by id: {}", id);
        Region region = entityManager.find(Region.class, id);
        if (region != null) {
            logger.info("Region retrived: {} - {}",region.getCode(), region.getName());
        } else {
            logger.warn("No region found with id: {}", id);
        }
        return region;
    }


    /**
     * Verifica si una región con el código especificado ya existe en la base de datos.
     * @param code el código de la región a verificar.
     * @return true si una región con el código ya existe, false de lo contrario.
     */
    @Override
    public boolean existsRegionByCode(String code) {
        logger.info("Checking if region with code: {} exists", code);
        String hql = "SELECT COUNT(r) FROM Region r WHERE UPPER(r.code) = :code";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("code", code.toUpperCase())
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Region with code: {} exists: {}", code, exists);
        return exists;
    }

    /**
     * Verifica si una región con el código especificado ya existe en la base de datos,
     * excluyendo una región con un ID específico.
     * @param code el código de la región a verificar.
     * @param id el ID de la región a excluir de la verificación.
     * @return true si una región con el código ya existe (y no es la región con el ID dado),
     * false de lo contrario.
     */
    @Override
    public boolean existsRegionByCodeAndNotId(String code, Long id){
        logger.info("Checking if region with code: {} exists excluding id: {}", code, id);
        String hql = "SELECT COUNT(r) FROM Region r WHERE UPPER(r.code) = :code AND r.id != :id";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("code", code.toUpperCase())
                .setParameter("id",id)
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Region with code: {} exists excluding id {}: {}", code, id, exists);
        return exists;
    }

    /**
     * Lista una página de regiones, con paginación y ordenación dinámica.
     *
     * @param page página actual (0-based)
     * @param size número de elementos por página
     * @param sortField campo por el que ordenar (id, code, name)
     * @param sortDir dirección de la ordenación (asc o desc)
     * @return Lista de regiones para esa página
     */
    @Override
    public List<Region> listRegionsPage(int page, int size, String sortField, String sortDir) {
        logger.info("Listing regions page={}, size={}, sortField={}, sortDir={}", page, size, sortField, sortDir);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Region> cq = cb.createQuery(Region.class);
        Root<Region> root = cq.from(Region.class);
        cq.select(root);

        // 1. Ordenación dinámica
        if (sortField != null && !sortField.isEmpty()) {
            Order order = sortDir.equalsIgnoreCase("asc")
                    ? cb.asc(root.get(sortField))
                    : cb.desc(root.get(sortField));
            cq.orderBy(order);
        } else {
            // Orden por defecto si no se especifica campo de ordenación
            cq.orderBy(cb.asc(root.get("id")));
        }

        TypedQuery<Region> query = entityManager.createQuery(cq);

        // 2. Paginación
        query.setFirstResult(page * size); // Offset
        query.setMaxResults(size);         // Limit

        return query.getResultList();
    }

    /**
     * Devuelve el número total de regiones.
     */
    @Override
    public long countRegions() {
        String hql = "SELECT COUNT(r) FROM Region r";
        Long total = entityManager.createQuery(hql, Long.class).getSingleResult();
        return (total != null) ? total : 0L;
    }

    // Nota: Es posible que necesites añadir estos dos métodos a la interfaz RegionDAO
}