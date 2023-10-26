package org.example.mappers;

import org.example.core.domain.Audit;
import org.example.core.domain.Player;
import org.example.core.domain.Transaction;
import org.example.dto.AuditDto;
import org.example.dto.PlayerDto;
import org.example.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface toDtoMapper {

    toDtoMapper INSTANCE = Mappers.getMapper(toDtoMapper.class);

    PlayerDto toDTO(Player player);

    AuditDto toDTO(Audit audit);

    TransactionDto toDTO(Transaction transaction);

}
