package org.example.core.service;

import org.example.dto.AuditDTO;

import java.util.List;


public interface AuditService {

    List<AuditDTO> getAuditHistory();


}
