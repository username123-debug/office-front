package com.example.demo.service;

import com.example.demo.dto.request.InformationRequestDto;
import com.example.demo.dto.response.InformationResponseDto;
import com.example.demo.entity.User;
import com.example.demo.mapper.InformationMapper;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InformationService {

    private final UserRepository userRepository;
    private final InformationMapper mapper;

    public InformationResponseDto getInfo(User user) {
        return mapper.toDto(user);
    }

    public InformationResponseDto updateInfo(User user, InformationRequestDto dto) {
        mapper.updateUser(user, dto);
        return mapper.toDto(userRepository.save(user));
    }
}
