package fr.but3.repository;

import fr.but3.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Integer> {

    List<Slot> findByDateOrderByStartTime(LocalDate date);

    List<Slot> findByDateBetweenOrderByDateAscStartTimeAsc(LocalDate start, LocalDate end);

    boolean existsByDateAndStartTime(LocalDate date, LocalTime startTime);
}