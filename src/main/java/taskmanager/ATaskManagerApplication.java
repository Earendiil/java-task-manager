package taskmanager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import taskmanager.entity.AppRole;
import taskmanager.entity.Category;
import taskmanager.entity.Role;
import taskmanager.entity.Task;
import taskmanager.entity.User;
import taskmanager.repositories.CategoryRepository;
import taskmanager.repositories.RoleRepository;
import taskmanager.repositories.TaskRepository;
import taskmanager.repositories.UserRepository;

@SpringBootApplication
public class ATaskManagerApplication {
 
	public static void main(String[] args) {
		SpringApplication.run(ATaskManagerApplication.class, args);
	}

    @Bean
    CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, TaskRepository taskRepository, CategoryRepository categoryRepository) {
        return args -> {
            // Initialize roles
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> adminRoles = Set.of(adminRole);

            // Create users
            if (!userRepository.existsByUsername("user1")) {
                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
                userRepository.save(user1);
            }

            if (!userRepository.existsByUsername("admin")) {
                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                userRepository.save(admin);
            }

            // Assign roles to users
            userRepository.findByUsername("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUsername("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });

            // Initialize categories
            Category category1 = new Category("Backend");
            Category category2 = new Category("Frontend");
            Category category3 = new Category("DevOps");
            categoryRepository.saveAll(List.of(category1, category2, category3));

            // Helper method to create future date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date futureDate1 = sdf.parse("2025-10-10");
            Date futureDate2 = sdf.parse("2025-11-01");
            Date futureDate3 = sdf.parse("2025-12-25");

            // Create tasks
            Task task1 = new Task("New project", "Backend task", "This is a description for the backend task", futureDate1, false, category1);
            Task task2 = new Task("Frontend design", "Frontend task", "This is a description for the frontend task", futureDate2, false, category2);
            Task task3 = new Task("Deploy app", "DevOps task", "This is a description for the DevOps task", futureDate3, false, category3);

            
            taskRepository.saveAll(List.of(task1, task2, task3));
            
        };
    }
	
}
 