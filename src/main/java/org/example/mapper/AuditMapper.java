package org.example.mapper;

import org.example.core.domain.Audit;
import org.example.core.domain.Player;
import org.example.dto.AuditDto;
import org.example.dto.PlayerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AuditMapper {
    AuditMapper INSTANCE = Mappers.getMapper(AuditMapper.class);

    @Mapping(source = "playerFullName", target = "playerFullName")
    @Mapping(source = "auditType", target = "auditType")
    @Mapping(source = "actionType", target = "actionType")
    AuditDto toDTO(Audit audit);
}

