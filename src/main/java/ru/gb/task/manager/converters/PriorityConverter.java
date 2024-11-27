package ru.gb.task.manager.converters;

import org.springframework.stereotype.Component;
import ru.gb.task.manager.dtos.PriorityDto;
import ru.gb.task.manager.entities.Priority;

@Component
public class PriorityConverter {

    public Priority dtoToEntity(PriorityDto priorityDto) {
        Priority priority = new Priority();
        priority.setId(priorityDto.getId());
        priority.setTitle(priorityDto.getTitle());
        return priority;
    }

    public PriorityDto entityToDto(Priority priority) {
        return new PriorityDto(priority.getId(), priority.getTitle());
    }
}
