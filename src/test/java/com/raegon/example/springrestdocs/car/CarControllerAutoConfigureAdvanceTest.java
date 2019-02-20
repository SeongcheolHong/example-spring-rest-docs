package com.raegon.example.springrestdocs.car;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raegon.example.springrestdocs.config.CustomizationConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.StringUtils;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@RunWith(SpringRunner.class)
@WebMvcTest(CarController.class)
@Import(CustomizationConfiguration.class)
public class CarControllerAutoConfigureAdvanceTest {

    @MockBean
    private CarService service;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    private Car car;

    @Autowired
    private RestDocumentationResultHandler handler;

    @Before
    public void setUp() {
        this.car = Car.builder().id(1).name("Car 1").company("Company 1").build();
    }

    @Test
    public void updateCar() throws Exception {
        // Given
        given(service.read(1)).willReturn(car);
        ConstrainedFields fields = new ConstrainedFields(Car.class);

        // When
        ResultActions result = mockMvc.perform(
                put("/car/{id}", car.getId())
                        .content(mapper.writeValueAsString(car))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andDo(handler.document(
                        pathParameters(
                                parameterWithName("id").description("ID")
                        ),
                        requestFields(
                                attributes(key("title").value("Field2 for user creation")),
                                fields.withPath("id").type(JsonFieldType.NUMBER).description("ID"),
                                fields.withPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("company").attributes(key("custom").value("커스텀값")).type(JsonFieldType.STRING).description("제조사").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("company").type(JsonFieldType.STRING).description("제조사")
                        )
                ));
    }

    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints")
                    .value(StringUtils.collectionToDelimitedString(
                            this.constraintDescriptions.descriptionsForProperty(path),
                            " +" + System.lineSeparator())
                    ));
        }
    }

}
