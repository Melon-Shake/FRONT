package com.example.melon_shake_webapp.Repository;

import com.example.melon_shake_webapp.data.ChartFlo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FloChartRepository extends JpaRepository<ChartFlo,Integer> {
    @Query(value = "select * from (select * from chart_flo order by id desc limit 100) a order by id asc", nativeQuery = true)
    List<ChartFlo> findTop100ChartFlo();
}
