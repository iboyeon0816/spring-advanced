package org.example.expert.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("댓글 저장 성공")
    void saveCommentsSuccess() throws Exception {
        // given
        long todoId = 1L;
        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.USER);
        CommentSaveRequest request = new CommentSaveRequest("contents");
        CommentSaveResponse response = new CommentSaveResponse(
                1L,
                request.getContents(),
                new UserResponse(authUser.getId(), authUser.getEmail())
        );

        given(commentService.saveComment(any(AuthUser.class), eq(todoId), any(CommentSaveRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(post("/todos/{todoId}/comments", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .requestAttr("userId", authUser.getId())
                        .requestAttr("email", authUser.getEmail())
                        .requestAttr("userRole", authUser.getUserRole().name()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.contents").value(response.getContents()))
                .andExpect(jsonPath("$.user.id").value(response.getUser().getId()))
                .andExpect(jsonPath("$.user.email").value(response.getUser().getEmail()));
    }

    @Test
    @DisplayName("댓글 조회 성공")
    void getCommentsSuccess() throws Exception {
        // given
        long todoId = 1L;
        List<CommentResponse> resultList = LongStream.rangeClosed(1, 3)
                .mapToObj(this::createCommentResponse)
                .toList();

        given(commentService.getComments(todoId)).willReturn(resultList);

        // when & then
        mockMvc.perform(get("/todos/{todoId}/comments", todoId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(resultList.size()))
                .andExpect(jsonPath("$[0].id").value(resultList.get(0).getId()))
                .andExpect(jsonPath("$[0].contents").value(resultList.get(0).getContents()))
                .andExpect(jsonPath("$[0].user.id").value(resultList.get(0).getUser().getId()))
                .andExpect(jsonPath("$[0].user.email").value(resultList.get(0).getUser().getEmail()));
    }

    private CommentResponse createCommentResponse(long id) {
        UserResponse userResponse = new UserResponse(id, "temp" + id + "@a.com");
        return new CommentResponse(id, "contents" + id, userResponse);
    }

}