package com.example.app;

import entity.HangHoa;
import entity.KhachHang;
import repository.HangHoaRepository;
import repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private HangHoaRepository hangHoaRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("=== DATA INITIALIZER DISABLED - USING EXISTING DATABASE DATA ONLY ===");
        System.out.println("Current HangHoa count: " + hangHoaRepository.count());
        System.out.println("Current KhachHang count: " + khachHangRepository.count());
        // No data initialization - always use existing database data
    }
}
