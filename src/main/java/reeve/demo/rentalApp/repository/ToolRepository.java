package reeve.demo.rentalApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reeve.demo.rentalApp.model.Tool;

public interface ToolRepository extends JpaRepository<Tool, String> {
}