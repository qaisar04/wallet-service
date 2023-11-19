package org.example.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {
    /**
     * The unique identifier of the player.
     */
    private Long id;
    /**
     * The username of the player.
     */
    private String username;
    /**
     * The current balance of the player, represented as a decimal value.
     */
    private BigDecimal balance;
}
