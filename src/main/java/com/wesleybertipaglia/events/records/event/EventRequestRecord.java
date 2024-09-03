package com.wesleybertipaglia.events.records.event;

import java.time.LocalDate;

public record EventRequestRecord(String title, String description, String location, LocalDate date, String image) {
}