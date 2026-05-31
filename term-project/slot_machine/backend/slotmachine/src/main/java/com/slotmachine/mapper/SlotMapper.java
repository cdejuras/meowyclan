package com.slotmachine.mapper;

import com.slotmachine.dto.SlotDto;
import com.slotmachine.entity.Slot;
import org.springframework.stereotype.Component;

// Abstraction: hides the conversion details behind a simple method
@Component
public class SlotMapper {

    public SlotDto toDto(Slot slot) {
        return new SlotDto(
                slot.getId(),
                slot.getPlayerName(),
                slot.getBalance(),
                slot.getSpinCount(),
                slot.getWinCount(),
                slot.getWinRate()
        );
    }

    public Slot toEntity(SlotDto dto) {
        Slot slot = new Slot(dto.getPlayerName(), dto.getBalance());
        return slot;
    }
}
