package ru.gb.task.manager.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.task.manager.entities.Priority;
import ru.gb.task.manager.repositories.PriorityRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriorityService {
    private final PriorityRepository priorityRepository;

    public Optional<Priority> findById(Long id) {
        return priorityRepository.findById(id);
    }

    public Optional<Priority> findByTitle(String title) {
        return priorityRepository.findByTitle(title);
    }

    public List<Priority> findAll() {
        return (List<Priority>) priorityRepository.findAll();
    }
}
