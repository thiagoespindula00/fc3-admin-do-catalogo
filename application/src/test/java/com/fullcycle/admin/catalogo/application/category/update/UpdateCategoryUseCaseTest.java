package com.fullcycle.admin.catalogo.application.category.update;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {
    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    // 1. Teste do caminho feliz
    // 2. Teste passando uma propriedade inválida (name)
    // 3. Teste atualizando uma categoria para inativa
    // 4. Teste simulando um erro generico vindo do gateway
    // 5. Teste atualizar categoria passando ID inválido

    // 1. Teste do caminho feliz
    @Test
    public void givenAValidCommand_whenCallsUpdateCaregory_shouldReturnCategoryId() {
        final var category = Category.newCategory("Film", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "Uma categoria legal";
        final var expectedIsActive = true;
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(category));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(expectedId);

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(eq(expectedId));
        Mockito.verify(categoryGateway, Mockito.times(1)).update(argThat(
                updatedCategory ->
                        Objects.equals(expectedName, updatedCategory.getName()) &&
                                Objects.equals(expectedDescription, updatedCategory.getDescription()) &&
                                Objects.equals(expectedIsActive, updatedCategory.isActive()) &&
                                Objects.equals(expectedId, updatedCategory.getId()) &&
                                Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt()) &&
                                Objects.equals(category.getUpdatedAt(), updatedCategory.getUpdatedAt()) &&
                                Objects.nonNull(updatedCategory.getUpdatedAt()) &&
                                Objects.isNull(updatedCategory.getDeletedAt())
        ));
    }
}
