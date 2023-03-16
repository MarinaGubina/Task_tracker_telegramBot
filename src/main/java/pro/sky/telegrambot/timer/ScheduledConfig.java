package pro.sky.telegrambot.timer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pro.sky.telegrambot.entity.Task;
import pro.sky.telegrambot.repository.TaskRepository;
import pro.sky.telegrambot.service.TaskService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Configuration
@EnableScheduling
@EnableAsync
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class ScheduledConfig {

    private final TaskService taskService;
    private final TelegramBot telegramBot;

    public ScheduledConfig(TaskService taskService, TelegramBot telegramBot) {
        this.taskService = taskService;
        this.telegramBot = telegramBot;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void run() {
        taskService.findTaskForSend().forEach(task -> {
            telegramBot.execute(
                    new SendMessage(task.getUserId(),
                            "Вы просили напомнить об этом: " + task.getDescription())
            );
            taskService.deleteTask(task);
        });
        }

}
