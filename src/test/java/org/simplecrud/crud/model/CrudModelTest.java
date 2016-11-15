package org.simplecrud.crud.model;

import org.junit.Test;
import org.simplecrud.crud.builder.CrudModelBuilder;
import org.simplecrud.crud.model.CrudModel;
import org.simplecrud.crud.service.CrudActions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CrudModelTest {

    @Test
    public void shouldCheckIfUserHasPermissions() {
        CrudModel crudModelAllPermissions = new CrudModelBuilder()
                .allowActions(CrudActions.Create, CrudActions.Delete, CrudActions.Update)
                .build();

        CrudModel crudModelNoPermissions = new CrudModelBuilder()
                .allowActions()
                .build();

        assertTrue(crudModelAllPermissions.allowCreate());
        assertTrue(crudModelAllPermissions.allowDelete());
        assertTrue(crudModelAllPermissions.allowUpdate());

        assertFalse(crudModelNoPermissions.allowCreate());
        assertFalse(crudModelNoPermissions.allowDelete());
        assertFalse(crudModelNoPermissions.allowUpdate());
    }
}
