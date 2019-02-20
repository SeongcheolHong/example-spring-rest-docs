package com.raegon.example.springrestdocs.car;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CarControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private CarService service;

    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    private Car car;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation).uris()
                        .withScheme("https")
                        .withHost("www.example.com")
                        .withPort(8080))
                .build();

        this.car = Car.builder().id(1).name("Car 1").company("Company 1").build();
    }

    @Test
    public void createCar() throws Exception {
        // Given
        given(service.read(1)).willReturn(car);

        // When
        ResultActions result = mockMvc.perform(
                post("/car")
                        .content(mapper.writeValueAsString(car))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isCreated())
                .andDo(document("create-car",
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

    @Test
    public void getCar() throws Exception {
        // Given
        given(service.read(1)).willReturn(car);

        // When
        ResultActions result = mockMvc.perform(get("/car/{id}", 1).accept(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andDo(document("get-car",
                        pathParameters(
                                parameterWithName("id").description("ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("company").type(JsonFieldType.STRING).description("제조사")
                        )
                ));
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

    @Test
    public void deleteCar() throws Exception {
        // When
        ResultActions result = mockMvc.perform(
                delete("/car/{id}", car.getId()));

        // Then
        result.andExpect(status().isNoContent())
                .andDo(document("delete-car",
                        pathParameters(
                                parameterWithName("id").description("ID")
                        )
                ));
    }
}
