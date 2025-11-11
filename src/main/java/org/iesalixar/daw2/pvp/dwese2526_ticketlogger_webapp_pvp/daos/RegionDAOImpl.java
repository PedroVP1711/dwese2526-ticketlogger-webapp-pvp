package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RegionDAOImpl implements RegionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Region> listAllRegions() {
        String sql = "SELECT * FROM regions ORDER BY id ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Region.class));
    }

    @Override
    public Region getRegionById(Long id) {
        String sql = "SELECT * FROM regions WHERE id = ?";
        List<Region> regions = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Region.class), id);
        return regions.isEmpty() ? null : regions.get(0);
    }

    @Override
    public boolean insertRegion(Region region) {
        String sql = "INSERT INTO regions (code, name) VALUES (?, ?)";
        return jdbcTemplate.update(sql, region.getCode(), region.getName()) > 0;
    }

    @Override
    public boolean updateRegion(Region region) {
        String sql = "UPDATE regions SET code = ?, name = ? WHERE id = ?";
        return jdbcTemplate.update(sql, region.getCode(), region.getName(), region.getId()) > 0;
    }

    @Override
    public boolean deleteRegion(Long id) {
        String sql = "DELETE FROM regions WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    @Override
    public boolean existsRegionByCode(String code) {
        String sql = "SELECT COUNT(*) FROM regions WHERE UPPER(code) = UPPER(?)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, code);
        return count != null && count > 0;
    }

    @Override
    public boolean existsRegionByCodeAndNotId(String code, Long id) {
        String sql = "SELECT COUNT(*) FROM regions WHERE UPPER(code) = UPPER(?) AND id <> ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, code, id);
        return count != null && count > 0;
    }

}
