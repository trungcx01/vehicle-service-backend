package com.example.vehicleService;

import com.example.vehicleService.entity.Role;
import com.example.vehicleService.entity.User;
import com.example.vehicleService.repository.RoleRepository;
import com.example.vehicleService.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class VehicleServiceApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(VehicleServiceApplication.class, args);

		// Lấy các bean cần thiết
		RoleRepository roleRepository = context.getBean(RoleRepository.class);
		UserRepository userRepository = context.getBean(UserRepository.class);
		PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

		// Gọi hàm khởi tạo role và admin
		createRolesAndAdmin(roleRepository, userRepository, passwordEncoder);
	}

	private static void createRolesAndAdmin(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		// Tạo các role nếu chưa tồn tại
		createRoleIfNotExists(roleRepository, "ADMIN");
		createRoleIfNotExists(roleRepository, "CUSTOMER");
		createRoleIfNotExists(roleRepository, "SHOP");


		if (userRepository.findByUsername("admin").isEmpty()) {
			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setEmail("trungcx01@gmail.com");
			admin.setLocked(false);
			userRepository.save(admin);
			Optional<Role> adminRole = roleRepository.findByName("ADMIN");
			admin.getRoles().add(adminRole.get());
			userRepository.save(admin);
			System.out.println("Tài khoản admin mặc định đã được tạo.");
		} else {
			System.out.println("Tài khoản admin đã tồn tại.");
		}
	}

	private static void createRoleIfNotExists(RoleRepository roleRepository, String roleName) {
		if (roleRepository.findByName(roleName).isEmpty()) {
			Role role = new Role();
			role.setName(roleName);
			roleRepository.save(role);
			System.out.println("Đã tạo role: " + roleName);
		}
	}
}

