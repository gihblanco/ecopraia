package com.project.ecopraia.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
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
@AutoConfigureMockMvc(addFilters=true) // tentei usar esse para ativar os filtros de segurança do spring, e interseptar a requisição feita no teste
/* não funciona
@ImportAutoConfiguration(classes = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
*/
public class LixeiraControllerTest {

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
