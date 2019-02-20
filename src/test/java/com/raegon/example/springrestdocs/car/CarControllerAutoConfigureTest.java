package com.raegon.example.springrestdocs.car;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CarController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "www.example.com")
public class CarControllerAutoConfigureTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService service;

    private ObjectMapper mapper = new ObjectMapper();

    private Car car;

    @Before
    public void setUp() {
        this.car = Car.builder().id(1).name("Car 1").company("Company 1").build();
    }

    @Test
    public void updateCar() throws Exception {
        // Given
        given(service.read(1)).willReturn(car);

        // When
        ResultActions result = mockMvc.perform(
                put("/car/{id}", car.getId())
                        .content(mapper.writeValueAsString(car))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andDo(document("update-car",
                        pathParameters(
                                parameterWithName("id").description("ID")
                        ),
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("company").type(JsonFieldType.STRING).description("제조사")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("company").type(JsonFieldType.STRING).description("제조사")
                        )
                ));
    }

}
