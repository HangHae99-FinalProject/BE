package com.hanghae99.finalproject.user.controller;

import com.hanghae99.finalproject.user.dto.LoginDto;
import com.hanghae99.finalproject.user.dto.SignOutDto;
import com.hanghae99.finalproject.user.dto.SignupDto;
import com.hanghae99.finalproject.exception.ErrorCode;
import com.hanghae99.finalproject.exception.ExceptionResponse;
import com.hanghae99.finalproject.user.repository.UserRepository;
import com.hanghae99.finalproject.security.UserDetailsImpl;
import com.hanghae99.finalproject.security.jwt.TokenDto;
import com.hanghae99.finalproject.security.jwt.TokenRequestDto;
import com.hanghae99.finalproject.user.service.UserService;
import com.hanghae99.finalproject.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {
//
    private final UserService userService;
    private final UserRepository userRepository;

    // 회원가입 API
    @PostMapping("/user/signup")
    public ResponseEntity<ExceptionResponse> registerUser(@RequestBody SignupDto.RequestDto requestDto) {
        userService.registerUser(requestDto);
        return new ResponseEntity<>(new ExceptionResponse(ErrorCode.OK), HttpStatus.OK);
    }
//
    // 이메일 중복검사 API
    @PostMapping("/user/emailCheck")
    public ResponseEntity<ExceptionResponse> emailCheck(@RequestBody SignupDto.RequestDto requestDto){
        UserValidator.validateInputEmail(requestDto);
        if(userRepository.existsByEmail(requestDto.getEmail())) {
            return new ResponseEntity<>(new ExceptionResponse(ErrorCode.SIGNUP_EMAIL_DUPLICATE), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ExceptionResponse(ErrorCode.SIGNUP_EMAIL_CORRECT), HttpStatus.OK);
        }
    }

    // 닉네임 중복검사 API
    @PostMapping("/user/nicknameCheck")
    public ResponseEntity<ExceptionResponse> nicknameCheck(@RequestBody SignupDto.RequestDto requestDto){
        UserValidator.validateInputNickname(requestDto);
        if(userRepository.existsByNickname(requestDto.getNickname())) {
            return new ResponseEntity<>(new ExceptionResponse(ErrorCode.SIGNUP_NICKNAME_DUPLICATE), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ExceptionResponse(ErrorCode.SIGNUP_NICKNAME_CORRECT), HttpStatus.OK);
        }
    }

    // 로그인 API
    @PostMapping("/user/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    // 토큰 재발행 API
    @PostMapping("/user/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(userService.reissue(tokenRequestDto));
    }

    // 회원 탈퇴 API
    @DeleteMapping("/user/remove")
    public ResponseEntity<ExceptionResponse> deleteUser(@RequestBody SignOutDto signOutDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(signOutDto, userDetails);
        return new ResponseEntity<>(new ExceptionResponse(ErrorCode.OK), HttpStatus.OK);
    }

    // 로그아웃 API
    @PostMapping("/user/logout")
    public ResponseEntity<ExceptionResponse> logout(@RequestBody TokenRequestDto tokenRequestDto) {
        userService.deleteRefreshToken(tokenRequestDto);
        return new ResponseEntity<>(new ExceptionResponse(ErrorCode.OK), HttpStatus.OK);
    }








}