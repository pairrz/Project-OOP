//package project.kombat_backend.model.game;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Transient;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.data.annotation.Id;
//import java.util.List;
//
//@Getter
//@Setter
//@Entity
//public class Game {
//    @Getter
//    @Setter
//    @jakarta.persistence.Id
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @Transient
//    private List<Integer> selectedMinions;  // ฟิลด์เก็บ ID ของมินเนียนที่เลือก
//}