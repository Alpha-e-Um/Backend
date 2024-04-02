package com.example.eumserver.domain.team.invite;

import com.example.eumserver.domain.team.Team;
import com.example.eumserver.domain.team.TeamRepository;
import com.example.eumserver.domain.team.participant.Participant;
import com.example.eumserver.domain.team.participant.ParticipantRepository;
import com.example.eumserver.domain.user.User;
import com.example.eumserver.domain.user.UserRepository;
import com.example.eumserver.global.error.CustomException;
import com.example.eumserver.global.error.exception.EntityNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InviteService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final InviteRepository inviteRepository;
    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public Invite inviteUsers(Long teamId, List<String> emails) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found."));

        if (emails.isEmpty()) {
            throw new CustomException(400, "There are 0 e-mails requested.");
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
            messageHelper.setText(token);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomException(500, "이메일 전송 실패");
        }
        return inviteRepository.save(invite);
    }

    private String getSubjectForTeam(String teamName) {
        return "[이음] " + teamName + " 팀에서 초대";
    }

    public Participant acceptInvite(Long inviteId, Long userId) {
        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new EntityNotFoundException("Invite not found."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        if (isExpiredInvite(invite)) {
            throw new CustomException(400, "Invite is expired.");
        }

        Participant participant = Participant.builder()
                .team(invite.getTeam())
                .user(user)
                .build();
        return participantRepository.save(participant);
    }

    private boolean isExpiredInvite(Invite invite) {
        Date now = new Date();
        return now.after(invite.getExpiredDate());
    }

}
