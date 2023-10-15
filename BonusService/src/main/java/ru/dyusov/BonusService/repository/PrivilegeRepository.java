package ru.dyusov.BonusService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dyusov.BonusService.domain.Privilege;

import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
    Optional<Privilege> findByUsername(String username);
}
