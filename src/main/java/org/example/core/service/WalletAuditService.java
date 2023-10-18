package org.example.core.service;

import org.example.core.domain.Audit;
import org.example.dao.impl.AuditDaoImpl;

import java.util.List;
import java.util.Optional;

public class WalletAuditService implements Service<Integer, Audit> {

    private static AuditDaoImpl auditDaoImpl = AuditDaoImpl.getInstance();

    private static WalletAuditService walletAuditService = new WalletAuditService();

    @Override
    public Optional<Audit> findById(Integer id) {
        return auditDaoImpl.findById(id);
    }

    @Override
    public List<Audit> findAll() {
        return auditDaoImpl.findAll();
    }

    @Override
    public Audit save(Audit audit) {
        return auditDaoImpl.save(audit);
    }

    @Override
    public void update(Audit audit) {
        auditDaoImpl.update(audit);
    }

    @Override
    public boolean delete(Integer id) {
        return auditDaoImpl.delete(id);
    }

    @Override
    public boolean deleteAll() {
        return auditDaoImpl.deleteAll();
    }

    public Optional<Audit> findByUsername(String username) {
        return auditDaoImpl.findByUsername(username);
    }

    public static WalletAuditService getInstance(){
        return walletAuditService;
    }
}
