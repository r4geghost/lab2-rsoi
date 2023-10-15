package ru.dyusov.BonusService.service;

import org.springframework.stereotype.Service;
import ru.dyusov.BonusService.domain.Privilege;
import ru.dyusov.BonusService.domain.PrivilegeHistory;
import ru.dyusov.BonusService.model.PrivilegeHistoryRecordRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface PrivilegeService {

    public Privilege getPrivilege(String username);

    public List<PrivilegeHistory> getPrivilegeHistory(String username);

    public int addHistoryRecord(String username, PrivilegeHistoryRecordRequest request);

    public Privilege updatePrivilege(String username, Map<String, Object> fields);

    public PrivilegeHistory getPrivilegeHistoryOfTicket(String username, UUID ticketUid);
}
