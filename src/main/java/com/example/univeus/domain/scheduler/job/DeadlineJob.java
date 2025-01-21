package com.example.univeus.domain.scheduler.job;

import static com.example.univeus.common.response.ResponseMessage.MEETING_POST_NOT_FOUND;

import com.example.univeus.domain.meeting.exception.MeetingException;
import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.meeting.model.MeetingPostStatus;
import com.example.univeus.domain.meeting.repository.MeetingPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class DeadlineJob implements Job {

    private final MeetingPostRepository meetingPostRepository;

    // JOB 정의
    @Override
    @Transactional
    public void execute(JobExecutionContext context) {
        String postId = context.getJobDetail().getJobDataMap().getString("postId");

        log.info("마감일 이벤트 트리거 발생: 게시글 ID => {}", postId);

        // 마감일 시 처리할 로직 작성
        MeetingPost meetingPost = meetingPostRepository.findById(Long.valueOf(postId))
                .orElseThrow(() -> new MeetingException(MEETING_POST_NOT_FOUND));

        // close로 status를 변경한다.
        meetingPost.update(
                meetingPost.getTitle(),
                meetingPost.getBody(),
                meetingPost.getJoinLimit(),
                meetingPost.getGenderLimit(),
                meetingPost.getLocation(),
                meetingPost.getPostDeadLine(),
                meetingPost.getMeetingSchedule(),
                meetingPost.getMeetingCategory(),
                meetingPost.getImages(),
                MeetingPostStatus.CLOSED
        );
    }
}
