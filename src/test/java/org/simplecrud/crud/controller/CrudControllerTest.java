package org.simplecrud.crud.controller;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.schema.JsonSchema;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.simplecrud.crud.builder.CrudModelBuilder;
import org.simplecrud.crud.model.CrudModel;
import org.simplecrud.crud.service.CrudEntity;
import org.simplecrud.crud.service.CrudService;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class CrudControllerTest {

    public static final String ENTITY_NAME = "AlternateDiagnosis";
    CrudController crudController;

    @Mock
    CrudService crudService;

    ObjectMapper objectMapper;
    private CrudEntity crudEntity;

    @Before
    public void setUp() {
        initMocks(this);
        objectMapper = new ObjectMapper();
        crudController = new CrudController(crudService);
        crudEntity = mock(CrudEntity.class);
        CrudModel crudModel = CrudModelBuilder.couchDBCrudModel(AlternateDiagnosis.class)
                .displayFields("name", "code").filterFields("code").build();
        when(crudEntity.getModel()).thenReturn(crudModel);
        when(crudEntity.getEntityType()).thenReturn(AlternateDiagnosis.class);
        when(crudService.getCrudEntity(ENTITY_NAME)).thenReturn(crudEntity);
    }

    @Test
    public void shouldSaveEntity() throws Exception {
        standaloneSetup(crudController).build()
                .perform(post("/crud/AlternateDiagnosis/save").contentType(MediaType.APPLICATION_JSON).content("{\"code\":\"code\",\"name\":\"name\"}".getBytes()))
                .andExpect(status().isOk());

        AlternateDiagnosis expectedEntity = new AlternateDiagnosis("name", "code");
        verify(crudService).saveEntity(ENTITY_NAME, expectedEntity);
    }

    @Test
    public void shouldGetEntity() throws Exception {
        String entityId = "123";

        when(crudService.getEntity(ENTITY_NAME, entityId)).thenReturn(new AlternateDiagnosis("test", "code"));

        String expectedJson = "{\"name\":\"test\",\"code\":\"code\"}";

        standaloneSetup(crudController).build()
                .perform(get("/crud/AlternateDiagnosis/get/" + entityId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));
    }

    @Test
    public void shouldDeleteEntity() throws Exception {
        String entityId = "123";

        standaloneSetup(crudController).build()
                .perform(get("/crud/AlternateDiagnosis/delete/" + entityId))
                .andExpect(status().isOk());

        verify(crudService).deleteEntity(ENTITY_NAME, entityId);
    }

    @Test
    public void shouldReturnListView() throws Exception {
        standaloneSetup(crudController).build()
                .perform(get("/crud/AlternateDiagnosis/list"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("entity", ENTITY_NAME))
                .andExpect(model().attribute("model", crudEntity.getModel()))
                .andExpect(view().name("crud/list"));
    }

    @Test
    public void shouldReturnSchemaForGivenEntity() throws Exception {
        JsonSchema jsonSchema = objectMapper.generateJsonSchema(AlternateDiagnosis.class);
        String expectedJson = objectMapper.writer().writeValueAsString(jsonSchema);

        standaloneSetup(crudController).build()
                .perform(get("/crud/AlternateDiagnosis/schema/"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));
    }
}

@Data
class AlternateDiagnosis {

    private String name;
    private String code;

    public AlternateDiagnosis() {
    }

    public AlternateDiagnosis(String name, String code) {
        this.name = name;
        this.code = code;
    }
}