package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.entity.Task;
import pro.sky.telegrambot.repository.TaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void addTask(LocalDateTime localDateTime,
                                    String description,
                                    Long userId) {
        Task task = new Task();
        task.setDateTime(localDateTime);
        task.setDescription(description);
        task.setUserId(userId);
        taskRepository.save(task);
    }

    public List<Task> findTaskForSend() {
        return taskRepository.findTasksByDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    }

    @Transactional
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

}

