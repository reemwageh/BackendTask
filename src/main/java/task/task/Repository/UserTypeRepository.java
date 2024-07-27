package task.task.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import task.task.Entity.UserType;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Integer> {
}
