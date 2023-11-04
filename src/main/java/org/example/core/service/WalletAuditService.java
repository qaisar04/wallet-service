package org.example.core.service;

import org.example.core.domain.Audit;
import org.example.dao.impl.AuditDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Optional<Audit> findById(Integer id) {
        return auditDaoImpl.findById(id);
    }

    @Override
    @Transactional
    public List<Audit> findAll() {
         return auditDaoImpl.findAll();
    }

    @Transactional
    public Optional<Audit> findByUsername(String username) {
        return auditDaoImpl.findByUsername(username);
    }

    @Override
    @Transactional
    public void save(Audit audit) {
         auditDaoImpl.save(audit);
    }

    @Override
    @Transactional
    public void update(Audit audit) {
        auditDaoImpl.update(audit);
    }

    @Override
    @Transactional
    public boolean delete(Integer id) {
        return auditDaoImpl.delete(id);
    }

    @Override
    @Transactional
    public boolean deleteAll() {
        return auditDaoImpl.deleteAll();
    }

    private WalletAuditService() {}
}
