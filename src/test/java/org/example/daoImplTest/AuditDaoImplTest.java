package org.example.daoImplTest;

import org.example.config.ContainersEnvironment;
import org.example.core.domain.Audit;
import org.example.core.domain.types.ActionType;
import org.example.core.domain.types.AuditType;
import org.example.dao.impl.AuditDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuditDaoImplTest extends ContainersEnvironment {

    private AuditDaoImpl auditDao;

    @BeforeEach
    public void setUp() {
        auditDao = AuditDaoImpl.getInstance();
        auditDao.deleteAll();
        Audit audit1 = Audit.builder()
                .playerFullName("Bob")
                .auditType(AuditType.SUCCESS)
                .actionType(ActionType.CREDIT_TRANSACTION)
                .build();
        Audit audit2 = Audit.builder()
                .playerFullName("Kate")
                .auditType(AuditType.FAIL)
                .actionType(ActionType.REGISTRATION)
                .build();
        Audit audit3 = Audit.builder()
                .playerFullName("Zaur")
                .auditType(AuditType.SUCCESS)
                .actionType(ActionType.DEBIT_TRANSACTION)
                .build();

        auditDao.save(audit1);
        auditDao.save(audit2);
        auditDao.save(audit3);
    }

    @Test
    public void testFindById() {
        Optional<Audit> foundAudit = auditDao.findByUsername("Bob");
        assertTrue(foundAudit.isPresent());
        Audit audit = Audit.builder()
                .playerFullName("Bob")
                .actionType(ActionType.CREDIT_TRANSACTION)
                .auditType(AuditType.SUCCESS)
                .build();

        assertEquals(audit.getPlayerFullName(), foundAudit.get().getPlayerFullName());
        Optional<Audit> notFoundAudit = auditDao.findById(999);
        assertFalse(notFoundAudit.isPresent());
    }

    @Test
    public void testFindByUsername() {
        Optional<Audit> foundAudit = auditDao.findByUsername("Bob");
        assertTrue(foundAudit.isPresent());

        Audit audit = Audit.builder()
                .playerFullName("Bob")
                .auditType(AuditType.SUCCESS)
                .actionType(ActionType.CREDIT_TRANSACTION)
                .build();
        assertEquals(audit.getPlayerFullName(), foundAudit.get().getPlayerFullName());

        // Попробуйте найти аудиторскую запись для пользователя, которого нет в базе данных
        Optional<Audit> notFoundAudit = auditDao.findByUsername("NonExistentUser");
        assertFalse(notFoundAudit.isPresent());
        assertTrue(notFoundAudit.isEmpty());

    }

    @Test
    public void testFindAll() {
        List<Audit> allAudits = auditDao.findAll();

        assertTrue(!allAudits.isEmpty());

        assertTrue(allAudits.contains(auditDao.findByUsername("Bob").get()));
        assertTrue(allAudits.contains(auditDao.findByUsername("Kate").get()));
        assertTrue(allAudits.contains(auditDao.findByUsername("Zaur").get()));

        assertEquals(3, allAudits.size());
    }

    @Test
    public void testSave() {
        Audit auditToSave = Audit.builder()
                .playerFullName("Alice")
                .auditType(AuditType.SUCCESS)
                .actionType(ActionType.CREDIT_TRANSACTION)
                .build();

        Audit savedAudit = auditDao.save(auditToSave);

        assertNotNull(savedAudit.getId());

        assertEquals(auditToSave.getPlayerFullName(), savedAudit.getPlayerFullName());
        assertEquals(auditToSave.getAuditType(), savedAudit.getAuditType());
        assertEquals(auditToSave.getActionType(), savedAudit.getActionType());
    }

    @Test
    public void testUpdate() {
        Audit auditToSave = Audit.builder()
                .playerFullName("Alice")
                .auditType(AuditType.SUCCESS)
                .actionType(ActionType.CREDIT_TRANSACTION)
                .build();
        Audit savedAudit = auditDao.save(auditToSave);

        savedAudit.setAuditType(AuditType.FAIL);
        savedAudit.setActionType(ActionType.DEBIT_TRANSACTION);

        auditDao.update(savedAudit);
        Audit updatedAudit = auditDao.findById(savedAudit.getId()).orElse(null);

        assertNotEquals(savedAudit.getAuditType(), updatedAudit.getAuditType());
        assertNotEquals(savedAudit.getActionType(), updatedAudit.getActionType());
    }


    @Test
    public void testDelete() {
        Optional<Audit> audit1 = auditDao.findByUsername("Bob");
        Optional<Audit> audit2 = auditDao.findByUsername("Kate");
        Optional<Audit> audit3 = auditDao.findByUsername("Qaisar");

        assertTrue(audit1.isPresent());
        assertTrue(audit2.isPresent());
        assertTrue(audit3.isEmpty());

        boolean deleted = auditDao.delete(audit1.get().getId());
        assertTrue(deleted);

        Optional<Audit> deletedAudit = auditDao.findById(audit1.get().getId());
        assertFalse(deletedAudit.isPresent());

        Optional<Audit> auditAfterDelete = auditDao.findByUsername("Kate");
        assertTrue(auditAfterDelete.isPresent());
    }

    @Test
    public void testDeleteAll() {
        List<Audit> auditsBeforeDelete = auditDao.findAll();
        assertFalse(auditsBeforeDelete.isEmpty());

        boolean deleted = auditDao.deleteAll();
        assertTrue(deleted);

        List<Audit> auditsAfterDelete = auditDao.findAll();
        assertTrue(auditsAfterDelete.isEmpty());
    }
}