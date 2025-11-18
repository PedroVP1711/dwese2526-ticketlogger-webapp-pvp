package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Region;
import java.util.List;

public interface RegionDAO {

    List<Region> listAllRegions();
    Region getRegionById(Long id);
    boolean insertRegion(Region region);
    boolean updateRegion(Region region);
    boolean deleteRegion(Long id);

    // Añadimos estos dos métodos 👇
    boolean existsRegionByCode(String code);
    boolean existsRegionByCodeAndNotId(String code, Long id);;
}
