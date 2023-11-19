package org.example.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.Audit;
import org.example.core.service.AuditService;
import org.example.dto.AuditDTO;
import org.example.mapper.AuditMapper;
import org.example.repository.AuditRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Override
    @Transactional
    public List<AuditDTO> getAuditHistory() {
        List<Audit> auditList = auditRepository.findAll();

        return  auditList.stream()
                .map(AuditMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }
}
