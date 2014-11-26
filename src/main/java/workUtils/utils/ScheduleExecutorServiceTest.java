package workUtils.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleExecutorServiceTest {

    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        // 延迟1秒后执行一次
        executorService.schedule(new Runnable() {
            public void run() {
                // System.out.println(System.currentTimeMillis());
            }
        }, 1, TimeUnit.SECONDS);

        // 延迟1秒后每隔2秒执行一次
        executorService.scheduleAtFixedRate(new Runnable() {
            public void run() {
                System.out.println("scheduleAtFixedRate:" + System.currentTimeMillis());
            }
        }, 1, 2, TimeUnit.SECONDS);

        executorService.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                System.out.println("scheduleWithFixedDelay:" + System.currentTimeMillis());
            }
        }, 1, 2, TimeUnit.SECONDS);
    }
}
