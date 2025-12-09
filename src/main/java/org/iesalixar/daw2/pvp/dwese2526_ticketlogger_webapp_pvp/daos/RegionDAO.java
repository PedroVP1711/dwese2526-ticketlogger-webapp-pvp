package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Region;

import java.sql.SQLException;
import java.util.List;

public interface RegionDAO {

    List<Region> listAllRegions();
    Region getRegionById(Long id);
    void insertRegion(Region region);
    void updateRegion(Region region);
    void deleteRegion(Long id) ;
    boolean existsRegionByCode(String code) ;
    boolean existsRegionByCodeAndNotId(String code, Long id);
    long countRegions();
    List<Region> listRegionsPage(int page, int size, String sortField, String sortDir);
}
