package com.example.staffratings.controller;

import com.example.staffratings.enums.RoleType;
import com.example.staffratings.model.StaffRating;
import com.example.staffratings.repository.StaffRatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(StaffRatingController.class)
class StaffRatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StaffRatingRepository repository;

    private StaffRating sampleRating(Long id) {
        StaffRating rating = new StaffRating();
        rating.setId(id);
        rating.setName("Ada Lovelace");
        rating.setEmail("ada@sfu.ca");
        rating.setRoleType(RoleType.PROF);
        rating.setClarity(8);
        rating.setNiceness(9);
        rating.setKnowledgeableScore(10);
        rating.setComment("Great lecturer");
        return rating;
    }

    @Test
    void listReturnsOkWithRatingsModel() throws Exception {
        when(repository.findAll()).thenReturn(List.of(sampleRating(1L)));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("ratings"));
    }

    @Test
    void newFormReturnsOkWithBlankRating() throws Exception {
        mockMvc.perform(get("/staff/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attributeExists("staffRating"))
                .andExpect(model().attributeExists("roleTypes"));
    }

    @Test
    void detailReturnsOkWithDisplayTitle() throws Exception {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleRating(1L)));

        mockMvc.perform(get("/staff/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("detail"))
                .andExpect(model().attributeExists("staffRating"))
                .andExpect(model().attribute("displayTitle", "Prof. Ada Lovelace"));
    }

    @Test
    void createWithValidDataRedirectsToList() throws Exception {
        when(repository.save(any(StaffRating.class))).thenReturn(sampleRating(1L));

        mockMvc.perform(post("/staff")
                        .param("name", "Ada Lovelace")
                        .param("email", "ada@sfu.ca")
                        .param("roleType", "PROF")
                        .param("clarity", "8")
                        .param("niceness", "9")
                        .param("knowledgeableScore", "10")
                        .param("comment", "Great lecturer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(repository).save(any(StaffRating.class));
    }

    @Test
    void createWithInvalidDataRedisplaysFormWithErrors() throws Exception {
        mockMvc.perform(post("/staff")
                        .param("name", "")
                        .param("email", "not-an-email")
                        .param("roleType", "PROF")
                        .param("clarity", "99")
                        .param("niceness", "9")
                        .param("knowledgeableScore", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attributeHasFieldErrors("staffRating", "name", "email", "clarity"));

        verify(repository, never()).save(any(StaffRating.class));
    }

    @Test
    void createWithDuplicateEmailRedisplaysFormWithEmailError() throws Exception {
        when(repository.save(any(StaffRating.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate email"));

        mockMvc.perform(post("/staff")
                        .param("name", "Ada Lovelace")
                        .param("email", "ada@sfu.ca")
                        .param("roleType", "PROF")
                        .param("clarity", "8")
                        .param("niceness", "9")
                        .param("knowledgeableScore", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attributeHasFieldErrors("staffRating", "email"));
    }

    @Test
    void updateWithValidDataRedirectsToDetail() throws Exception {
        when(repository.save(any(StaffRating.class))).thenReturn(sampleRating(1L));

        mockMvc.perform(post("/staff/1/edit")
                        .param("name", "Ada Lovelace")
                        .param("email", "ada@sfu.ca")
                        .param("roleType", "PROF")
                        .param("clarity", "8")
                        .param("niceness", "9")
                        .param("knowledgeableScore", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/staff/1"));

        verify(repository).save(any(StaffRating.class));
    }

    @Test
    void deleteRemovesRatingAndRedirectsToList() throws Exception {
        mockMvc.perform(post("/staff/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(repository).deleteById(eq(1L));
    }
}
