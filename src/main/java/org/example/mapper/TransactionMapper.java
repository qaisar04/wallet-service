package org.example.mapper;

import org.example.core.domain.Player;
import org.example.core.domain.Transaction;
import org.example.dto.PlayerDto;
import org.example.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(source = "transactionId", target = "transactionId")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "amount", target = "amount")
    TransactionDto toDTO(Transaction transaction);
}

