package project.kombat_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.kombat_backend.dto.HexCellDTO;
import project.kombat_backend.model.game.HexCell;
import project.kombat_backend.repository.HexCellRepository;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/board")
public class GameBoardController {

    @Autowired
    private HexCellRepository hexCellRepository;

    // ดึง HexCell ทั้งหมดในกระดาน
    @GetMapping("/hexcells")
    public ResponseEntity<List<HexCellDTO>> getAllHexCells() {
        List<HexCellDTO> hexCells = hexCellRepository.findAll()
                .stream()
                .map(HexCell::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(hexCells);
    }

    // ดึงเฉพาะ HexCell ที่ว่าง
    @GetMapping("/empty-hexcells")
    public ResponseEntity<List<HexCellDTO>> getEmptyHexCells() {
        List<HexCellDTO> emptyCells = hexCellRepository.findByOwnerIsNull()
                .stream()
                .map(HexCell::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(emptyCells);
    }
}
