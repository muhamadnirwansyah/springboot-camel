package com.app.service_backend.repository;

import com.app.service_backend.entity.SubDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubDistrictRepository extends JpaRepository<SubDistrict,String>, JpaSpecificationExecutor<SubDistrict> {

    @Query(value = "SELECT * FROM tbl_subdistrict WHERE city_id=:cityId",nativeQuery = true)
    List<SubDistrict> subDistrictByCityId(@Param("cityId") String cityId);
}
