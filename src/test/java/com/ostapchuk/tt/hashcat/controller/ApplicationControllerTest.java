package com.ostapchuk.tt.hashcat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.ostapchuk.tt.hashcat.dto.ApplicationDto;
import com.ostapchuk.tt.hashcat.entity.Hash;
import com.ostapchuk.tt.hashcat.service.ApplicationService;
import com.ostapchuk.tt.hashcat.service.sender.SenderService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import static com.ostapchuk.tt.hashcat.util.TestConstant.EMAIL;
import static com.ostapchuk.tt.hashcat.util.TestConstant.HASHES;
import static com.ostapchuk.tt.hashcat.util.TestConstant.HASH_3;
import static com.ostapchuk.tt.hashcat.util.TestConstant.POST_APPLICATIONS_ENDPOINT;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ApplicationController.class)
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @MockBean
    private SenderService senderService;

    private ObjectMapper mapper;

    private ApplicationDto correctDto;

    private ApplicationDto inCorrectDto;

    @BeforeEach
    void setUp() {
        mapper = new JsonMapper();

        correctDto = ApplicationDto
                .builder()
                .email(EMAIL)
                .hashes(HASHES)
                .build();

        inCorrectDto = ApplicationDto
                .builder()
                .email("some")
                .hashes(List.of(EMPTY, "   ", HASH_3))
                .build();
    }

    @Test
    void decrypt_ShouldAccept_WhenDtoIsCorrect() throws Exception {
        // setup
        List<CompletableFuture<Hash>> futures = List.of(CompletableFuture.completedFuture(Hash.builder().build()));

        // when
        Mockito.when(applicationService.decrypt(correctDto)).thenReturn(futures);

        // verify
        mockMvc.perform(createRequest(correctDto))
               .andExpect(status().isAccepted());
    }

    @Test
    void decrypt_ShouldNotAccept_WhenDtoIncorrect() throws Exception {
        // verify
        mockMvc.perform(createRequest(inCorrectDto))
                .andExpect(status().isBadRequest());
    }

    private RequestBuilder createRequest(final ApplicationDto dto) throws JsonProcessingException {
        return post(POST_APPLICATIONS_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto).getBytes(UTF_8))
                .accept(APPLICATION_JSON);
    }
}
