package com.fullcycle.admin.catalogo.application.category.create;

import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    // Teste do caminho feliz
    @Test
    public void givenAValidCommand_whenCallsCreateCaregory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Uma categoria legal";
        final var expectedIsActive = true;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());
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

    // Teste passando uma propriedade inválida
    @Test
    public void givenAInvalidName_whenCallsCreateCaregory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "Uma categoria legal";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().getFirst().message());

        Mockito.verify(categoryGateway, Mockito.times(0)).create(Mockito.any());
    }

    // Teste criando uma categoria inativa
    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCaregory_shouldReturnInactiveCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Uma categoria legal";
        final var expectedIsActive = false;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());
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
                                Objects.nonNull(category.getDeletedAt())
                ));
    }

    // Teste simulando um erro genérico vindo do gateway
    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Uma categoria legal";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.create(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var exception = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(command));


        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(category ->
                        Objects.equals(expectedName, category.getName()) &&
                                Objects.equals(expectedDescription, category.getDescription()) &&
                                Objects.equals(expectedIsActive, category.isActive()) &&
                                Objects.nonNull(category.getId()) &&
                                Objects.nonNull(category.getCreatedAt()) &&
                                Objects.nonNull(category.getUpdatedAt()) &&
                                Objects.nonNull(category.getDeletedAt())
                ));
    }
}
