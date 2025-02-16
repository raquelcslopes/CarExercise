package exercise_vik_CarExercise.repository;

import exercise_vik_CarExercise.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByNif(String nif);

    List<UserEntity> findByIsActive(boolean active);

//    List<UserEntity> findByFirstNameAndLastNameAndIsActive(boolean active);

}
