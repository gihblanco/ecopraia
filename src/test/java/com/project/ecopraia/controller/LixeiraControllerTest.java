package com.project.ecopraia.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ecopraia.entity.Lixeira;
import com.project.ecopraia.entity.dtos.lixeira.CriarLixeiraDTO;
import com.project.ecopraia.service.AuthorizationService;
import com.project.ecopraia.service.JwtService;
import com.project.ecopraia.service.LixeiraService;

@WebMvcTest(LixeiraController.class)
@AutoConfigureMockMvc(addFilters = true) // ativa os filtros de segurança do Spring para interceptar a requisição feita no teste
@Import(LixeiraControllerTest.MethodSecurityConfig.class) // habilita @PreAuthorize dentro do slice do @WebMvcTest
public class LixeiraControllerTest {

    // O @WebMvcTest não importa o SecurityFilterConfig, então a segurança de método
    // (@PreAuthorize) fica desabilitada por padrão. Esta configuração reativa ela no teste.
    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSecurityConfig {
    }

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LixeiraService lixeiraService;

    @MockitoBean
    private JwtService jwtService;    

    @MockitoBean
    private AuthorizationService authorizationService;
    
    private CriarLixeiraDTO lixeiraDto;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        
        lixeiraDto = new CriarLixeiraDTO(-27.39, -27.90, List.of(1L, 2L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveConseguirCriarUmaLixeiraEREtornarSucesso() throws Exception {
        Lixeira lixeira = new Lixeira();
        lixeira.setId(5L);
        
        when(lixeiraService.criar(any(CriarLixeiraDTO.class))).thenReturn(lixeira);

        mockMvc.perform(post("/lixeiras")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(lixeiraDto)))
            .andExpect(status().isCreated())
            .andExpect(content().string("Lixeira criada com sucesso!"));
    }

    @Test
    @WithMockUser(roles = "BANHISTA") // 1. Simulamos um usuário comum (não-admin)
    void naoDeveConseguirCriarUmaLixeiraSeUsuarioNaoForAdmin() throws Exception {   
        mockMvc.perform(post("/lixeiras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lixeiraDto)))
                .andExpect(status().isForbidden()); 
    }
}
