package ru.gb.task.manager.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.task.manager.entities.Status;
import ru.gb.task.manager.repositories.StatusRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;

    public Optional<Status> findById(Long id) {
        return statusRepository.findById(id);
    }

    public Optional<Status> findByTitle(String title) {
        return statusRepository.findByTitle(title);
    }

    public List<Status> findAll() {
        return (List<Status>) statusRepository.findAll();
    }
}
