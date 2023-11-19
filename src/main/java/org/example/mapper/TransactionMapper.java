package org.example.mapper;

import org.example.core.domain.Player;
import org.example.core.domain.Transaction;
import org.example.dto.PlayerDTO;
import org.example.dto.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    /**
     * Mapping transactions list entity to dto list
     *
     * @param entities the transactions entities
     * @return mapped transaction dto list
     */
    List<TransactionResponse> toDTOList(List<Transaction> entities);
}

