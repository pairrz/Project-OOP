package project.kombat_backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MinionDTO {
    private Long id;
    private int hp;
    private int def;
    private int x;
    private int y;
    private String strategy;
    private String ownerName;
}
