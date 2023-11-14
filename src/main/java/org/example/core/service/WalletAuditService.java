package org.example.core.service;

import org.example.core.domain.Audit;
import org.example.dao.impl.AuditDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletAuditService implements WalletService<Integer, Audit> {

    private AuditDaoImpl auditDaoImpl;

    @Autowired
    public WalletAuditService(AuditDaoImpl auditDaoImpl) {
        this.auditDaoImpl = auditDaoImpl;
    }

    @Override
    public Optional<Audit> findById(Integer id) {
        return auditDaoImpl.findById(id);
    }

    @Override
    public List<Audit> findAll() {
         return auditDaoImpl.findAll();
    }

    public Optional<Audit> findByUsername(String username) {
        return auditDaoImpl.findByUsername(username);
    }

    @Override
    public void save(Audit audit) {
         auditDaoImpl.save(audit);
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

    private WalletAuditService() {}
}
