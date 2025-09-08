package com.lucasboareto.todolist.utils;

import com.lucasboareto.todolist.task.TaskModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UtilsTest {

    @Test
    void copyNonNullPropertiesCopiaApenasPropriedadesNaoNulas() {
        TaskModel origem = new TaskModel();
        origem.setDescription("desc origem");
        origem.setPriority(null);

        TaskModel destino = new TaskModel();
        destino.setDescription("desc destino");
        destino.setPriority("alta");

        utils.copyNonNullProperties(origem, destino);

        assertEquals("desc origem", destino.getDescription());
        assertEquals("alta", destino.getPriority());
    }

    @Test
    void getNullPropertyNamesRetornaNomesCorretosQuandoHaPropriedadesNulas() {
        TaskModel origem = new TaskModel();
        origem.setDescription(null);
        origem.setPriority("media");

        String[] propriedadesNulas = utils.getNullPropertyNames(origem);

        assertTrue(java.util.Arrays.asList(propriedadesNulas).contains("description"));
    }
}