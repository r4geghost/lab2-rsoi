package ru.dyusov.BonusService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.dyusov.BonusService.domain.Privilege;
import ru.dyusov.BonusService.domain.PrivilegeHistory;
import ru.dyusov.BonusService.model.PrivilegeHistoryRecordRequest;
import ru.dyusov.BonusService.service.PrivilegeService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class PrivilegeController {
    private final PrivilegeService privilegeService;

    public PrivilegeController(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    @GetMapping("/privilege")
    public Privilege getPrivilegeStatus(@RequestHeader(name = "X-User-Name") String username) {
        return privilegeService.getPrivilege(username);
    }

    @GetMapping("/privilege/history")
    public List<PrivilegeHistory> getPrivilegeHistory(@RequestHeader(name = "X-User-Name") String username) {
        return privilegeService.getPrivilegeHistory(username);
    }

    @GetMapping("/privilege/history/{ticketUid}")
    public PrivilegeHistory getPrivilegeHistoryOfTicket(@PathVariable UUID ticketUid, @RequestHeader(name = "X-User-Name") String username) {
        return privilegeService.getPrivilegeHistoryOfTicket(username, ticketUid);
    }

    @GetMapping("/manage/health")
    public ResponseEntity<Void> status() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/privilege/history")
    public ResponseEntity<Void> addHistoryRecord(@RequestHeader(name = "X-User-Name") String username, @RequestBody PrivilegeHistoryRecordRequest request) {
        int id = privilegeService.addHistoryRecord(username, request);
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri()).build();
    }

    @PatchMapping("/privilege")
    public Privilege updateBonusBalance(@RequestHeader(name = "X-User-Name") String username, @RequestBody Map<String, Object> fields) {
        return privilegeService.updatePrivilege(username, fields);
    }

}
