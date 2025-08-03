package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
	@NotNull
	@Positive
	private Long itemId;

//	@NotNull
	@FutureOrPresent
	private LocalDateTime start;

//	@NotNull
	@Future
	private LocalDateTime end;

	@AssertTrue(message = "Дата окончания должна быть позже даты начала")
	boolean isStartBeforeEnd() {
		return start != null &&
				end != null &&
				start.isBefore(end);
	}
}
