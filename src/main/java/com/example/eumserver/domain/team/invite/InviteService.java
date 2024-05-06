package com.example.eumserver.domain.team.invite;

import com.example.eumserver.domain.team.Team;
import com.example.eumserver.domain.team.TeamRepository;
import com.example.eumserver.domain.team.participant.Participant;
import com.example.eumserver.domain.team.participant.ParticipantRepository;
import com.example.eumserver.domain.user.User;
import com.example.eumserver.domain.user.UserRepository;
import com.example.eumserver.global.error.exception.CustomException;
import com.example.eumserver.global.error.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InviteService {

    private static final String CONTEXT_TEAM_NAME = "team_name";
    private static final String CONTEXT_INVITE_TOKEN = "token";

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final UserRepository userRepository;
    private final InviteRepository inviteRepository;
    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;

    /**
     * 임시 토큰을 생성하여 주어진 이메일 모두에게 해당 토큰을 담은 이메일을 전송합니다.
     * @param teamId 초대하려는 team ID
     * @param emails 초대받는 사용자들의 email
     * @return {@link Invite} 생성된 Invite
     */
    @Transactional
    public Invite inviteUsers(Long teamId, List<String> emails) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        if (emails.isEmpty()) {
            throw new CustomException("There are 0 e-mails requested.", ErrorCode.INVALID_INPUT_VALUE);
        }

        String token = UUID.randomUUID().toString();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);

        Invite invite = new Invite(team, token, calendar.getTime());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setTo(emails.toArray(String[]::new));
            messageHelper.setSubject(getSubjectForTeam(team.getName()));

            Context context = new Context();
            context.setVariable(CONTEXT_TEAM_NAME, team.getName());
            context.setVariable(CONTEXT_INVITE_TOKEN, token);

            String html = templateEngine.process("invite", context);
            messageHelper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomException("There was a problem while sending email.", ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return inviteRepository.save(invite);
    }

    /**
     * team 이름을 포함한 이메일 제목 만드는 함수
     * @param teamName 포함하고 싶은 team 이름
     * @return team 이름이 포함된 이메일 제목
     */
    private String getSubjectForTeam(String teamName) {
        return "[이음] " + teamName + " 팀에서 초대";
    }

    /**
     * 초대된 사용자가 해당 초대를 수락하는 함수
     * @param inviteId Invite ID
     * @param userId User ID
     * @return {@link Participant} 생성된 Participant
     */
    public Participant acceptInvite(Long inviteId, Long userId) {
        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVITE_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (isExpiredInvite(invite)) {
            throw new CustomException(ErrorCode.EXPIRED_INVITE);
        }

        Participant participant = Participant.builder()
                .team(invite.getTeam())
                .user(user)
                .build();
        return participantRepository.save(participant);
    }

    /**
     * Invite가 만료되었는지 확인하는 함수
     * @param invite
     * @return true if invite is expired.
     */
    private boolean isExpiredInvite(Invite invite) {
        Date now = new Date();
        return now.after(invite.getExpiredDate());
    }

}
