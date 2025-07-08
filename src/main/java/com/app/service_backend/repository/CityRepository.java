package com.app.service_backend.repository;

import com.app.service_backend.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City,String>, JpaSpecificationExecutor<City> {

    @Query(value = "SELECT * FROM tbl_city WHERE province_id=:provinceId",nativeQuery = true)
    List<City> cityByProvinceId(@Param("provinceId")String provinceId);
}
