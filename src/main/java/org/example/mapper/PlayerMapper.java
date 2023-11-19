package org.example.mapper;

import org.example.core.domain.Player;
import org.example.dto.PlayerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    /**
     * Mapping player entity to dto
     *
     * @param entity the player entity
     * @return mapped player dto
     */
    PlayerDTO toDto(Player entity);

}

