package fr.but3.controller.rest;

import fr.but3.dto.AppointmentDto;
import fr.but3.dto.AppointmentListResponse;
import fr.but3.dto.PersonDto;
import fr.but3.model.Reservation;
import fr.but3.model.Slot;
import fr.but3.model.User;
import fr.but3.repository.ReservationRepository;
import fr.but3.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
public class RestAppointmentsController {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public RestAppointmentsController(ReservationRepository reservationRepository,
                                      UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    /**
     * Organisateur:
     * GET /todayslist/{date}
     * - Renvoie la liste des RDV d'une journée + personnes concernées
     * - Erreur si date invalide
     * - Public (pas d'auth)
     */
    @GetMapping(
        value = "/todayslist/{date}",
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    public AppointmentListResponse todaysList(@PathVariable String date) {

        final LocalDate d = parseIsoDateOr400(date);

        List<Reservation> reservations = reservationRepository.findAllForDateWithSlotAndUser(d);

        Map<Integer, AppointmentDto> bySlot = new LinkedHashMap<>();
        for (Reservation r : reservations) {
            Slot s = r.getSlot();
            User u = r.getUser();

            AppointmentDto appt = bySlot.computeIfAbsent(
                s.getId(),
                sid -> new AppointmentDto(
                    s.getId(),
                    s.getDate(),
                    s.getStartTime(),
                    s.getEndTime(),
                    new ArrayList<>()
                )
            );

            appt.getPersons().add(new PersonDto(u.getName(), u.getEmail()));
        }

        return new AppointmentListResponse(new ArrayList<>(bySlot.values()));
    }

    /**
     * Client:
     * GET /myappointments/{name}
     * - Renvoie la liste des RDV futurs pour une personne (name)
     * - Erreur si plusieurs personnes ont le même nom
     * - Public (pas d'auth)
     */
    @GetMapping(
        value = "/myappointments/{name}",
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    public AppointmentListResponse myAppointments(@PathVariable String name) {

        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name missing");
        }

        List<User> users = userRepository.findAllByName(name);

        if (users.size() > 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate name");
        }
        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User user = users.get(0);

        List<Reservation> reservations = reservationRepository
            .findFutureForUser(user.getId(), LocalDate.now());

        List<AppointmentDto> out = new ArrayList<>();
        for (Reservation r : reservations) {
            Slot s = r.getSlot();
            out.add(new AppointmentDto(
                s.getId(),
                s.getDate(),
                s.getStartTime(),
                s.getEndTime(),
                List.of(new PersonDto(user.getName(), user.getEmail()))
            ));
        }

        return new AppointmentListResponse(out);
    }

    private static LocalDate parseIsoDateOr400(String raw) {
        try {
            return LocalDate.parse(raw);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad date format (expected yyyy-MM-dd)");
        }
    }
}