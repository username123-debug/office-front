package com.example.demo.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ScheduleResponseDto {
	private Long id;
	private String title;
	private LocalDateTime endDateTime;
	private LocalDateTime startDateTime;
	private String body;
	private int createdUserId; // 作成者id
}