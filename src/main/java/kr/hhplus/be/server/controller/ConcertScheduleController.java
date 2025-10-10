package kr.hhplus.be.server.controller;

import kr.hhplus.be.server.dto.request.ConcertScheduleCreateRequestDto;
import kr.hhplus.be.server.dto.response.ConcertScheduleResponseDto;
import kr.hhplus.be.server.service.ConcertScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class ConcertScheduleController {
    private final ConcertScheduleService concertScheduleService;

    @PostMapping
    public ResponseEntity<ConcertScheduleResponseDto> createSchedule(
        @RequestBody ConcertScheduleCreateRequestDto requestDto) {
        ConcertScheduleResponseDto response = concertScheduleService.createSchedule(requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 예약 가능한 공연 일정 조회
     * - concertName: 콘서트 이름(부분검색 가능)
     * - 페이징: 기본 10개씩, startTime 오름차순
     */
    @GetMapping("/available")
    public ResponseEntity<Page<ConcertScheduleResponseDto>> getAvailableSchedules(
        @RequestParam(required = false) String concertName,
        @PageableDefault(page = 0, size = 10, sort = "startTime", direction = Sort.Direction.ASC)
        Pageable pageable
    ) {
        Page<ConcertScheduleResponseDto> result =
            concertScheduleService.getAvailableConcertSchedules(concertName, pageable);
        return ResponseEntity.ok(result);
    }

}
