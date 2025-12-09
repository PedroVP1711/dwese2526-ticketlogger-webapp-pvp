package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import java.util.List;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Province;

public interface ProvinceDAO {
    List<Province> listAllProvinces();
    void insertProvince(Province province);
    void updateProvince(Province province);
    void deleteProvince(Long id);
    Province getProvinceById(Long id);
    boolean existsProvinceByCode(String code);
    boolean existsProvinceByCodeAndNotId(String code, Long id);
    long countProvinces();
    List<Province> listProvincesPage(int page, int size);
}