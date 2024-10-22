package ducthinh.shop.clothershop.repository;


import ducthinh.shop.clothershop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByVerificationToken(String token);

    User findByResetPasswordToken(String token);
}