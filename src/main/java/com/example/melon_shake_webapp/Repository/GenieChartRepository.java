package com.example.melon_shake_webapp.Repository;

import com.example.melon_shake_webapp.data.ChartBugs;
import com.example.melon_shake_webapp.data.ChartGenie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenieChartRepository extends JpaRepository<ChartGenie,Integer> {
    @Query(value = "select * from (select * from chart_genie order by id desc limit 100) a order by id asc", nativeQuery = true)
    List<ChartGenie> findTop100ChartGenie();
}
