package fr.but3.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "appointments")
public class AppointmentListResponse {

    @JacksonXmlProperty(localName = "appointment")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<AppointmentDto> appointments = new ArrayList<>();

    public AppointmentListResponse() {}

    public AppointmentListResponse(List<AppointmentDto> appointments) {
        this.appointments = appointments;
    }

    public List<AppointmentDto> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentDto> appointments) {
        this.appointments = appointments;
    }
}