package project.kombat_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.kombat_backend.dto.HexCellDTO;
import project.kombat_backend.model.game.HexCell;
import project.kombat_backend.repository.HexCellRepository;

import java.util.List;

@RestController
@RequestMapping("/api/hexcell")
public class HexCellController {

    @Autowired
    private HexCellRepository hexCellRepository;

    @GetMapping("/all")
    public ResponseEntity<List<HexCellDTO>> getAllHexCells() {
        List<HexCellDTO> hexCells = hexCellRepository.findAll()
                .stream()
                .map(HexCell::toDTO)
                .toList();
        return ResponseEntity.ok(hexCells);
    }

    @GetMapping("/empty")
    public ResponseEntity<List<HexCellDTO>> getEmptyCells() {
        List<HexCellDTO> hexCells = hexCellRepository.findByOwnerIsNull()
                .stream()
                .map(HexCell::toDTO)
                .toList();
        return ResponseEntity.ok(hexCells);
    }
}
