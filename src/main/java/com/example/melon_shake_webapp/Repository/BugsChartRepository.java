package com.example.melon_shake_webapp.Repository;

import com.example.melon_shake_webapp.data.ChartBugs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BugsChartRepository extends JpaRepository<ChartBugs,Integer>{
    @Query(value = "select * from (select * from chart_bugs order by id desc limit 100) a order by id asc", nativeQuery = true)
    List<ChartBugs> findTop100ChartBugs();
}
