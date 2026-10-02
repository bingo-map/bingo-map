package com.bingomap.bingo_map.notice;

import com.bingomap.bingo_map.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import com.bingomap.bingo_map.user.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class NoticeController {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    private final NoticeRepository noticeRepository;

    @Autowired
    private NotificationService notificationService;

    public NoticeController(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @GetMapping("/notices")
    public String notices() {
        return "forward:/notices/notices.html";
    }

    @GetMapping("/notices/view/{id}")
    public String noticeDetail() {
        return "forward:/notices/detail.html";
    }

    // =========================
    // 공개 API
    // =========================

    @GetMapping("/api/notices")
    @ResponseBody
    public List<NoticeResponseDto> list() {
        return noticeRepository.findAllByOrderByPinnedDescCreatedAtDesc()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/api/notices/{id}")
    @ResponseBody
    public ResponseEntity<?> detail(@PathVariable Long id) {

        Notice notice = noticeRepository.findById(id).orElse(null);

        if (notice == null) {
            return ResponseEntity
                    .status(404)
                    .body(Map.of("message", "존재하지 않는 공지사항입니다."));
        }

        notice.increaseViewCount();
        noticeRepository.save(notice);

        return ResponseEntity.ok(toDto(notice));
    }

    // =========================
    // 관리자 전용 API
    // =========================

    @PostMapping("/api/admin/notices")
    @ResponseBody
    public ResponseEntity<?> create(
            @Valid @RequestBody NoticeRequestDto dto,
            BindingResult bindingResult,
            HttpServletRequest request
    ) {
        if (!canManageConsole(request)) {
            return ResponseEntity
                    .status(403)
                    .body(Map.of("message", "관리자만 접근할 수 있습니다."));
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            bindingResult.getFieldErrors().get(0).getDefaultMessage()
                    ));
        }

        Long authorId = getLoginUserId(request);

        if (authorId == null) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of("message", "로그인이 필요합니다."));
        }

        Notice notice = new Notice(
                authorId,
                dto.getTitle(),
                dto.getContent()
        );
        if (dto.getPinned() != null) notice.setPinned(dto.getPinned() ? 1 : 0);

        noticeRepository.save(notice);
        notificationService.onNoticeCreated(authorId, dto.getTitle());

        return ResponseEntity.ok(toDto(notice));
    }

    @PutMapping("/api/admin/notices/{id}")
    @ResponseBody
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody NoticeRequestDto dto,
            BindingResult bindingResult,
            HttpServletRequest request
    ) {
        if (!canManageConsole(request)) {
            return ResponseEntity
                    .status(403)
                    .body(Map.of("message", "관리자만 접근할 수 있습니다."));
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            bindingResult.getFieldErrors().get(0).getDefaultMessage()
                    ));
        }

        Notice notice = noticeRepository.findById(id).orElse(null);

        if (notice == null) {
            return ResponseEntity
                    .status(404)
                    .body(Map.of("message", "존재하지 않는 공지사항입니다."));
        }

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setPinned(Boolean.TRUE.equals(dto.getPinned()) ? 1 : 0);

        noticeRepository.save(notice);

        return ResponseEntity.ok(toDto(notice));
    }

    @PatchMapping("/api/admin/notices/{id}/pin")
    @ResponseBody
    public ResponseEntity<?> setPinned(@PathVariable Long id, @RequestBody Map<String, Boolean> body, HttpServletRequest request) {
        if (!canManageConsole(request)) {
            return ResponseEntity.status(403).body(Map.of("message", "관리자만 접근할 수 있습니다."));
        }
        Notice notice = noticeRepository.findById(id).orElse(null);
        if (notice == null) return ResponseEntity.status(404).body(Map.of("message", "존재하지 않는 공지사항입니다."));
        notice.setPinned(Boolean.TRUE.equals(body.get("pinned")) ? 1 : 0);
        return ResponseEntity.ok(toDto(noticeRepository.save(notice)));
    }

    @DeleteMapping("/api/admin/notices/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        if (!canManageConsole(request)) {
            return ResponseEntity
                    .status(403)
                    .body(Map.of("message", "관리자만 접근할 수 있습니다."));
        }

        if (!noticeRepository.existsById(id)) {
            return ResponseEntity
                    .status(404)
                    .body(Map.of("message", "존재하지 않는 공지사항입니다."));
        }

        noticeRepository.deleteById(id);

        return ResponseEntity.ok(
                Map.of("message", "삭제되었습니다.")
        );
    }

    // =========================
    // 세션 확인
    // =========================

    private boolean canManageConsole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        Object role = session.getAttribute(LoginController.SESSION_USER_ROLE);
        return "ADMIN".equals(role);
    }

    private Long getLoginUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object value = session.getAttribute(
                LoginController.SESSION_USER_ID
        );

        if (value instanceof Long) {
            return (Long) value;
        }

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        return null;
    }

    // =========================
    // Entity → DTO
    // =========================

    private NoticeResponseDto toDto(Notice notice) {

        String createdAt = notice.getCreatedAt() != null
                ? notice.getCreatedAt().format(DATE_FORMAT)
                : "-";

        String updatedAt = notice.getUpdatedAt() != null
                ? notice.getUpdatedAt().format(DATE_FORMAT)
                : null;

        return new NoticeResponseDto(
                notice.getNoticeId(),
                notice.getAuthorId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getViewCount(),
                notice.getPinned(),
                createdAt,
                updatedAt
        );
    }
}
