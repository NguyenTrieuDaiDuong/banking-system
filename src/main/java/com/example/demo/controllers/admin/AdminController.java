package com.example.demo.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.admin.DashboardStatsDTO;
import com.example.demo.service.admin.AdminServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {
	@Autowired
	public AdminServiceImpl adminServiceImpl;

	@GetMapping("/dashboard")
	public ResponseEntity<?> getDashboard() {
		try {
			DashboardStatsDTO dashboard = adminServiceImpl.getDashboardStats();
			return ResponseEntity.ok(dashboard);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
