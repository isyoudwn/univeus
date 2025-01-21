package com.example.univeus.domain.scheduler.service;


import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.domain.meeting.exception.MeetingException;
import com.example.univeus.domain.scheduler.job.DeadlineJob;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuartzService {
    private final Scheduler scheduler;

    public void scheduleDeadlineEvent(String postId, Date deadline) {
        try {
            // Job 생성
            JobDetail jobDetail = JobBuilder.newJob(DeadlineJob.class)
                    .withIdentity(postId, "dealineJobs")
                    .usingJobData("postId", postId)
                    .build();

            // Trigger 생성
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(postId + "Trigger", "deadlineTriggers")
                    .startAt(deadline) // 마감일 설정
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            log.info("마감일 이벤트가 예약되었습니다 => {}", deadline);
        } catch (SchedulerException ex) {
            throw new MeetingException(ResponseMessage.MEETING_POST_NOT_FOUND);
        }
    }
}
