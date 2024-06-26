package com.inhahackathon.foodmarket.controller;

import com.inhahackathon.foodmarket.auth.util.AuthUtil;
import com.inhahackathon.foodmarket.exception.PermissionDeniedException;
import com.inhahackathon.foodmarket.service.LikesService;
import com.inhahackathon.foodmarket.type.dto.BoardResponseDto;
import com.inhahackathon.foodmarket.type.dto.ResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
@Tag(name = "Like")
@Slf4j
public class LikesController {

    private final LikesService likesService;

    @Operation(summary = "게시글 좋아요", description = "관심 등록")
//    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/{boardId}")
    public ResponseModel createLikeBoard(
            @PathVariable("boardId") Long boardId
    ) {
        Long userId = AuthUtil.getAuthenticationInfoUserId();
        likesService.createLikeBoard(userId, boardId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "게시글 좋아요 취소", description = "관심 등록 취소")
    @DeleteMapping("/{boardId}")
    public ResponseModel deleteLikeBoard(
            @PathVariable("boardId") Long boardId
    ) {
        Long userId = AuthUtil.getAuthenticationInfoUserId();
        likesService.deleteLikeBoard(userId, boardId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "유저 좋아요 목록 조회", description = "관심목록 조회")
    @GetMapping("/list/{userId}")
    public ResponseModel getLikeBoardList(
            @PathVariable("userId") Long userId
    ) throws PermissionDeniedException {
        if (!userId.equals(AuthUtil.getAuthenticationInfoUserId())) {
            throw new PermissionDeniedException();
        }
        List<BoardResponseDto> likesList = likesService.getLikeBoard(userId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("likesList", likesList);
        return responseModel;
    }

}
