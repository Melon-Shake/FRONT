package com.example.melon_shake_webapp.Repository;

import com.example.melon_shake_webapp.data.ChartVibe;
import com.example.melon_shake_webapp.data.TotalChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TotalChartRepository extends JpaRepository<TotalChart, Integer> {
    @Query(value = "select * from total_chart order by index asc limit 100", nativeQuery = true)
    List<TotalChart> findTop100TotalChart();
}
