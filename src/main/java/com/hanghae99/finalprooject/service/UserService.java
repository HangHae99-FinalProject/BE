package com.hanghae99.finalprooject.service;

import com.hanghae99.finalprooject.dto.SignupDto;
import com.hanghae99.finalprooject.exception.ErrorCode;
import com.hanghae99.finalprooject.exception.PrivateException;
import com.hanghae99.finalprooject.model.User;
import com.hanghae99.finalprooject.repository.UserRepository;
import com.hanghae99.finalprooject.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(SignupDto.RequestDto requestDto) {

        // 회원 이메일 중복 확인
        String email = requestDto.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new PrivateException(ErrorCode.DUPLICATE_ERROR_SIGNUP_EMAIL);
        }

        // 회원 닉네임 중복 확인
        String nickname = requestDto.getNickname();
        if (userRepository.existsByNickname(nickname)) {
            throw new PrivateException(ErrorCode.DUPLICATE_ERROR_SIGNUP_NICKNAME);
        }
        // 회원 비밀번호 암호화
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 유효성 검사
        UserValidator.validateUserRegister(requestDto);

        User user = userRepository.save(
                User.builder()
                        .email(email)
                        .nickname(nickname)
                        .password(password)
                        .address(requestDto.getAddress())
//                        .intro("자시소개를 해주세요")
//                        .profileImg("dfdfdfdfdff.png")
                        .build()
        );
    }
}
