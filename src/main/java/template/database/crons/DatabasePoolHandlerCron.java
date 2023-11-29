package template.database.crons;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DatabasePoolHandlerCron {
    @Scheduled(cron = "*/30 * * * * *")
    public void cleanTempDir() {
        /*
          Check all data sources to see what pools are idle and remove them
         */
//        DatabaseConnectionContext.getTargetDataSources().forEach((key, value) -> {
//            if (key.equals("main") || value.getDataSource() == null) return;
//
//            Instant timeDifference = Instant.now().minusSeconds(20);
//
//            if (value.getLastConnectionTime().isBefore(timeDifference)) {
//                value.getDataSource().close();
//            }
//        });
//
//        DatabaseConnectionContext.getTargetDataSources().entrySet()
//            .removeIf(item -> item.getValue().getDataSource().isClosed());
    }
}
