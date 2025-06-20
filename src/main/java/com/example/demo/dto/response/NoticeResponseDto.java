package com.example.demo.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class NoticeResponseDto {
	private String title;
	private String boby;
	private LocalDateTime createdAt;
	private Long createdUserId;
}