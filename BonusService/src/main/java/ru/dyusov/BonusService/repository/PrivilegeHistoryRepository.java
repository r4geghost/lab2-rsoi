package ru.dyusov.BonusService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dyusov.BonusService.domain.Privilege;
import ru.dyusov.BonusService.domain.PrivilegeHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PrivilegeHistoryRepository extends JpaRepository<PrivilegeHistory, Integer> {
    List<PrivilegeHistory> findAllByPrivilege(Privilege privilege);

    Optional<PrivilegeHistory> findTopByPrivilegeAndTicketUidOrderByDate(Privilege privilege, UUID ticketUid);
}
