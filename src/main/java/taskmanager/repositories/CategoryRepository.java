package taskmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import taskmanager.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

	boolean existsByName(String name);

	
}
