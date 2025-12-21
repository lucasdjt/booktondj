package fr.but3.service;

import fr.but3.model.Slot;

public record DaySlotView(
        Slot slot,
        int used,
        boolean complet,
        boolean reservable
) {}