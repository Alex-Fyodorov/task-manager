package ru.gb.task.manager.converters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.gb.task.manager.dtos.StatusDto;
import ru.gb.task.manager.entities.Status;

@SpringBootTest(classes = StatusConverter.class)
public class StatusConverterTest {
    @Autowired
    private StatusConverter statusConverter;

    @Test
    @DisplayName("Преобразование dto в сущность")
    public void dtoToEntityTest() {
        StatusDto statusDto = new StatusDto(1L, "status");
        Status status = statusConverter.dtoToEntity(statusDto);
        Assertions.assertEquals(status.getId(), statusDto.getId());
        Assertions.assertEquals(status.getTitle(), statusDto.getTitle());
    }

    @Test
    @DisplayName("Преобразование сущности в dto")
    public void entityToDtoTest() {
        Status status = new Status();
        status.setId(1L);
        status.setTitle("status");
        StatusDto statusDto = statusConverter.entityToDto(status);
        Assertions.assertEquals(status.getId(), statusDto.getId());
        Assertions.assertEquals(status.getTitle(), statusDto.getTitle());
    }
}
