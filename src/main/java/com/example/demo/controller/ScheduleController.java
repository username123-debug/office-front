package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    
    
    //更新
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto dto) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);

        if (optionalSchedule.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Schedule schedule = optionalSchedule.get();

        // 参加者をIDから取得
        List<User> participants = userRepository.findAllById(dto.getParticipantIds());

        // 更新内容をセット
        schedule.setTitle(dto.getTitle());
        schedule.setDescription(dto.getDescription());
        schedule.setStartDateTime(dto.getStartDateTime());
        schedule.setEndDateTime(dto.getEndDateTime());
        schedule.setParticipants(participants);

        return ResponseEntity.ok(scheduleRepository.save(schedule));
        
    }
    
    //削除
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long id) {
        if (!scheduleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        scheduleRepository.deleteById(id);
        return ResponseEntity.ok("削除しました");
    }


}

