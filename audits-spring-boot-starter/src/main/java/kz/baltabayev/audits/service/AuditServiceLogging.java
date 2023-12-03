package kz.baltabayev.audits.service;

import kz.baltabayev.audits.domain.AuditDTO;

import java.util.List;

public interface AuditServiceLogging {

    List<AuditDTO> getAuditHistory();
}
