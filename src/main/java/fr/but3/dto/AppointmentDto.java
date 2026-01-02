package fr.but3.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDto {

    private Integer slotId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @JacksonXmlProperty(localName = "person")
    @JacksonXmlElementWrapper(localName = "persons")
    private List<PersonDto> persons = new ArrayList<>();

    public AppointmentDto() {}

    public AppointmentDto(Integer slotId, LocalDate date, LocalTime startTime, LocalTime endTime, List<PersonDto> persons) {
        this.slotId = slotId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.persons = persons;
    }

    public Integer getSlotId() { return slotId; }
    public void setSlotId(Integer slotId) { this.slotId = slotId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public List<PersonDto> getPersons() { return persons; }
    public void setPersons(List<PersonDto> persons) { this.persons = persons; }
}