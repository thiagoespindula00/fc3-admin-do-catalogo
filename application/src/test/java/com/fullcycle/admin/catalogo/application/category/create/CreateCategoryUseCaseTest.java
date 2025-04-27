package com.fullcycle.admin.catalogo.application.category.create;

import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

public class CreateCategoryUseCaseTest {
    // Teste do caminho feliz
    @Test
    public void givenAValidCommand_whenCallsCreateCaregory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Uma categoria legal";
        final var expectedIsActive = true;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final CategoryGateway categoryGateway = Mockito.mock(CategoryGateway.class);
        Mockito.when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var useCase = new DefaultCreateCategoryUseCase(categoryGateway);
        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(category ->
                        Objects.equals(expectedName, category.getName()) &&
                        Objects.equals(expectedDescription, category.getDescription()) &&
                        Objects.equals(expectedIsActive, category.isActive()) &&
                        Objects.nonNull(category.getId()) &&
                        Objects.nonNull(category.getCreatedAt()) &&
                        Objects.nonNull(category.getUpdatedAt()) &&
                        Objects.isNull(category.getDeletedAt())
        ));
    }
}
