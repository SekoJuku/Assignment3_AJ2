package kz.edu.astanait.application.repositories;

import kz.edu.astanait.application.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);

    @Transactional
    @Modifying
    @Query(value = "update users set role_id = :roleId where id = :userId",nativeQuery = true)
    void giveRole(Long userId, Long roleId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update User  u set u.password = :newPassword where u.id = :id")
    void changePassword(String newPassword, Long id);
}
