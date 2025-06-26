package MainApp.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MainApp.Entities.User;

@Repository

public interface UserRepository extends JpaRepository<User,Long>{

   public boolean findByUsername(String username);
   public Optional<User> findByEmail(String email);
   public Optional<User> findByPassword(String password);
}
