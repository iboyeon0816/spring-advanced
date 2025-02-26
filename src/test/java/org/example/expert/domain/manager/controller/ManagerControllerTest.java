package org.example.expert.domain.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.service.ManagerService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManagerController.class)
class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ManagerService managerService;

    @Test
    @DisplayName("매니저 저장 성공")
    void saveManagerSuccess() throws Exception {
        // given
        User currentUser = User.fromAuthUser(new AuthUser(1L, "user1@example.com", UserRole.USER));
        User managerUser = User.fromAuthUser(new AuthUser(2L, "user2@example.com", UserRole.USER));

        long todoId = 1L;
        ManagerSaveRequest request = new ManagerSaveRequest(managerUser.getId());
        ManagerSaveResponse response = new ManagerSaveResponse(1L, new UserResponse(managerUser.getId(), managerUser.getEmail()));

        given(managerService.saveManager(any(AuthUser.class), eq(todoId), any(ManagerSaveRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/todos/{todoId}/managers", todoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .requestAttr("userId", currentUser.getId())
                .requestAttr("email", currentUser.getEmail())
                .requestAttr("userRole", currentUser.getUserRole().name()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.user.id").value(response.getUser().getId()))
                .andExpect(jsonPath("$.user.email").value(response.getUser().getEmail()));
    }

    @Test
    @DisplayName("매니저 조회 성공")
    void getMembersSuccess() throws Exception {
        // given
        long todoId = 1L;
        List<ManagerResponse> responseList = LongStream.rangeClosed(1, 3)
                .mapToObj(id -> new ManagerResponse(id, new UserResponse(id, "user" + id + "@example.com")))
                .toList();

        given(managerService.getManagers(todoId)).willReturn(responseList);

        // when & then
        mockMvc.perform(get("/todos/{todoId}/managers", todoId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseList.size()))
                .andExpect(jsonPath("$[0].id").value(responseList.get(0).getId()))
                .andExpect(jsonPath("$[0].user.id").value(responseList.get(0).getUser().getId()))
                .andExpect(jsonPath("$[0].user.email").value(responseList.get(0).getUser().getEmail()));
    }

    @Test
    @DisplayName("매니저 삭제 성공")
    void deleteManager() throws Exception {
        //given
        long todoId = 1L, managerId = 1L;
        User currentUser = User.fromAuthUser(new AuthUser(1L, "user1@example.com", UserRole.USER));

        doNothing().when(managerService).deleteManager(currentUser.getId(), todoId, managerId);

        // when & then
        mockMvc.perform(delete("/todos/{todoId}/managers/{managerId}", todoId, managerId)
                .requestAttr("userId", currentUser.getId())
                .requestAttr("email", currentUser.getEmail())
                .requestAttr("userRole", currentUser.getUserRole().name()))
                .andExpect(status().isOk());

        verify(managerService, times(1)).deleteManager(currentUser.getId(), todoId, managerId);
    }
}