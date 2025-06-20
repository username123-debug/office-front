package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ScheduleRequestDto;
import com.example.demo.entity.Schedule;
import com.example.demo.entity.User;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Vue側と連携するため
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleRequestDto dto) {

        // 参加者のUserをIDから取得
        List<User> participants = userRepository.findAllById(dto.getParticipantIds());

        // Scheduleエンティティに詰める
        Schedule schedule = new Schedule();
        schedule.setTitle(dto.getTitle());
        schedule.setDescription(dto.getDescription());
        schedule.setStartDateTime(dto.getStartDateTime());
        schedule.setEndDateTime(dto.getEndDateTime());
        schedule.setParticipants(participants);

        // 保存
        Schedule saved = scheduleRepository.save(schedule);

        return ResponseEntity.ok(saved);
    }
    
//    @GetMapping
//    public ResponseEntity<List<Schedule>> getSchedules(@RequestParam(required = false) Long userId) {
//        List<Schedule> schedules;
//
//        if (userId != null) {
//            // 特定ユーザーの予定一覧（参加しているもの）
//            schedules = scheduleRepository.findByParticipants_Id(userId);
//        } else {
//            // 全スケジュール
//            schedules = scheduleRepository.findAll();
//        }
//
//        return ResponseEntity.ok(schedules);
}

