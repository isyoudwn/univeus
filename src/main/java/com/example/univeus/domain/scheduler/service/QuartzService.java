package com.example.univeus.domain.scheduler.service;


import static com.example.univeus.common.response.ResponseMessage.SCHEDULER_EXCEPTION;

import com.example.univeus.domain.meeting.exception.MeetingException;
import com.example.univeus.domain.scheduler.exception.SchedulingException;
import com.example.univeus.domain.scheduler.job.DeadlineJob;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuartzService {
    private final Scheduler scheduler;
    private static final String JOB_GROUP = "dealineJobs";
    private static final String TRIGGER_GROUP = "deadlineTriggers";


    public void scheduleDeadlineEvent(String postId, Date deadline) {
        try {
            // Job 생성
            JobDetail jobDetail = JobBuilder.newJob(DeadlineJob.class)
                    .withIdentity(postId, JOB_GROUP)
                    .usingJobData("postId", postId)
                    .build();

            // Trigger 생성
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(postId, TRIGGER_GROUP)
                    .startAt(deadline) // 마감일 설정
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            log.info("마감일 이벤트가 예약되었습니다 => {}", deadline);

        } catch (SchedulerException ex) {
            throw new SchedulingException(SCHEDULER_EXCEPTION);
        }
    }

    // 게시글 수정 시, 기존 Trigger를 rescheduleJob을 통해 업데이트.
    // 게시글 삭제 시, 해당 Job 및 Trigger를 deleteJob을 통해 제거.
    public void updateSchedule(String postId, Date newStartTime) {
        try {
            // 기존 Trigger의 Key를 생성
            TriggerKey triggerKey = new TriggerKey(postId, TRIGGER_GROUP);

            // 기존 Trigger 가져오기
            Trigger oldTrigger = scheduler.getTrigger(triggerKey);

            if (oldTrigger == null) {
                throw new SchedulingException(SCHEDULER_EXCEPTION);
            }

            Trigger newTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .startAt(newStartTime) // 마감일 설정
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                    .build();

            // 기존 Trigger를 새로운 Trigger로 교체
            scheduler.rescheduleJob(triggerKey, newTrigger);
            log.info("스케줄이 수정되었습니다: {}, Trigger: {}", newStartTime, newTrigger);
        } catch (SchedulerException ex) {
            throw new MeetingException(SCHEDULER_EXCEPTION);
        }
    }

    public void deleteJob(String postId) {
        try {
            // JobKey로 Job 삭제
            JobKey jobKey = new JobKey(postId, JOB_GROUP);
            boolean result = scheduler.deleteJob(jobKey);

            if (result) {
                log.info("Job이 삭제되었습니다 => {}", jobKey);
            } else {
                log.warn("Job 삭제 실패 => {}", jobKey);
            }
        } catch (SchedulerException ex) {
            throw new SchedulingException(SCHEDULER_EXCEPTION);
        }
    }
}
