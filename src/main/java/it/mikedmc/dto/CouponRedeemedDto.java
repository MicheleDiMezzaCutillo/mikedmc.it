package it.mikedmc.dto;

import java.time.LocalDateTime;

public class CouponRedeemedDto {
	private long id;
	private boolean active;

    private LocalDateTime startingDate;

    private LocalDateTime finishDate;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDateTime getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(LocalDateTime startingDate) {
		this.startingDate = startingDate;
	}

	public LocalDateTime getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(LocalDateTime finishDate) {
		this.finishDate = finishDate;
	}

	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
}
