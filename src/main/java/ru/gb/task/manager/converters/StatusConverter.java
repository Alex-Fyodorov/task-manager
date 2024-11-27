package ru.gb.task.manager.converters;

import org.springframework.stereotype.Component;
import ru.gb.task.manager.dtos.StatusDto;
import ru.gb.task.manager.entities.Status;

@Component
public class StatusConverter {

    public Status dtoToEntity(StatusDto statusDto) {
        Status status = new Status();
        status.setId(statusDto.getId());
        status.setTitle(statusDto.getTitle());
        return status;
    }

    public StatusDto entityToDto(Status status) {
        return new StatusDto(status.getId(), status.getTitle());
    }
}
