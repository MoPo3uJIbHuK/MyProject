package app.converter.repositories;

import app.converter.models.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationsRepository extends JpaRepository<Operation,Integer> {
}
