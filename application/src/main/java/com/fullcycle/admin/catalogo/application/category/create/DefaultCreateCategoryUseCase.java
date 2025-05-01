package com.fullcycle.admin.catalogo.application.category.create;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import com.fullcycle.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand command) {
        final var name = command.name();
        final var description = command.description();
        final var isActive = command.isActive();

        final var notification = Notification.create();

        final var category = Category.newCategory(name, description, isActive);
        category.validate(notification);


        return notification.hasErrors() ? API.Left(notification) : create(category);
    }

    private Either<Notification, CreateCategoryOutput> create(Category category) {
        return API.Try(() -> categoryGateway.create(category))
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::from);
    }

    // A mesma coisa que o método create porém menos elegante
    private Either<Notification, CreateCategoryOutput> create1(final Category category) {
        try {
           return Either.right(CreateCategoryOutput.from(this.categoryGateway.create(category)));
        }
        catch (Throwable t) {
            return Either.left(Notification.create(t));
        }
    }
}
