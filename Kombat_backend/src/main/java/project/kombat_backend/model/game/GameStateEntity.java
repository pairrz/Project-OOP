package project.kombat_backend.model.game;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "game_state")
public class GameStateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gameId;
    private String playerOne;
    private String playerTwo;
    private String winner;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}


