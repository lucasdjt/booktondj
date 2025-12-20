package fr.but3.repository;

import fr.but3.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("""
        select r.slot.id, coalesce(sum(r.nbPersonnes), 0)
        from Reservation r
        where r.slot.date between :start and :end
        group by r.slot.id
    """)
    List<Object[]> usedBySlotBetween(LocalDate start, LocalDate end);

    @Query("""
        select r.slot.id, coalesce(sum(r.nbPersonnes), 0)
        from Reservation r
        where r.slot.date = :date
        group by r.slot.id
    """)
    List<Object[]> usedBySlotForDate(LocalDate date);

    List<Reservation> findByUserIdOrderBySlotDateAscSlotStartTimeAsc(Integer uid);

    long countByUserIdAndSlotId(Integer userId, Integer slotId);

    @Query("""
        select coalesce(sum(r.nbPersonnes), 0)
        from Reservation r
        where r.slot.id = :slotId
    """)
    long usedForSlot(Integer slotId);

    @Query("""
        select r from Reservation r
        join fetch r.slot s
        join fetch r.user u
        order by s.date, s.startTime
    """)
    List<Reservation> findAllWithSlotAndUser();

    @Query("SELECT MIN(r.slot.date) FROM Reservation r")
    LocalDate findMinReservedDate();
}