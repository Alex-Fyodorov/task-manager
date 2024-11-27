package ru.gb.task.manager.converters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.gb.task.manager.dtos.PriorityDto;
import ru.gb.task.manager.entities.Priority;

@SpringBootTest(classes = PriorityConverter.class)
public class PriorityConverterTest {
    @Autowired
    private PriorityConverter priorityConverter;

    @Test
    @DisplayName("Преобразование dto в сущность")
    public void dtoToEntityTest() {
        PriorityDto priorityDto = new PriorityDto(1L, "priority");
        Priority priority = priorityConverter.dtoToEntity(priorityDto);
        Assertions.assertEquals(priority.getId(), priorityDto.getId());
        Assertions.assertEquals(priority.getTitle(), priorityDto.getTitle());
    }

    @Test
    @DisplayName("Преобразование сущности в dto")
    public void entityToDtoTest() {
        Priority priority = new Priority();
        priority.setId(1L);
        priority.setTitle("status");
        PriorityDto priorityDto = priorityConverter.entityToDto(priority);
        Assertions.assertEquals(priority.getId(), priorityDto.getId());
        Assertions.assertEquals(priority.getTitle(), priorityDto.getTitle());
    }
}
