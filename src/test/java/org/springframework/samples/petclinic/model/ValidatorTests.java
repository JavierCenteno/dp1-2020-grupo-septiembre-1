package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class ValidatorTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Actor

	@Test
	void actorShouldNotValidateWhenNameEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Actor actor = new Actor();
		actor.setName("");
		actor.setEmail("actor@actor.com");
		actor.setAddress("c/Actor");
		Validator validator = createValidator();
		Set<ConstraintViolation<Actor>> constraintViolations = validator.validate(actor);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Actor> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		assertThat(violation.getMessage()).isEqualTo("must not be blank");
	}

	@Test
	void actorShouldNotValidateWhenEmailEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Actor actor = new Actor();
		actor.setName("Actor");
		actor.setEmail("");
		actor.setAddress("c/Actor");
		Validator validator = createValidator();
		Set<ConstraintViolation<Actor>> constraintViolations = validator.validate(actor);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Actor> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
		assertThat(violation.getMessage()).isEqualTo("must not be blank");
	}

	@Test
	void actorShouldNotValidateWhenEmailInvalid() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Actor actor = new Actor();
		actor.setName("Actor");
		actor.setEmail("actor.actor");
		actor.setAddress("c/Actor");
		Validator validator = createValidator();
		Set<ConstraintViolation<Actor>> constraintViolations = validator.validate(actor);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Actor> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
		assertThat(violation.getMessage()).isEqualTo("must be a well-formed email address");
	}

	@Test
	void actorShouldNotValidateWhenAddressEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Actor actor = new Actor();
		actor.setName("Actor");
		actor.setEmail("actor@actor.com");
		actor.setAddress("");
		Validator validator = createValidator();
		Set<ConstraintViolation<Actor>> constraintViolations = validator.validate(actor);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Actor> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("address");
		assertThat(violation.getMessage()).isEqualTo("must not be blank");
	}

	////////////////////////////////////////////////////////////////////////////////
	// Building

	@Test
	void buildingShouldNotValidateWhenNameEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Building building = new Building();
		building.setName("");
		building.setAddress("c/Building");
		building.setIncome(0);
		Validator validator = createValidator();
		Set<ConstraintViolation<Building>> constraintViolations = validator.validate(building);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Building> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		assertThat(violation.getMessage()).isEqualTo("must not be blank");
	}

	@Test
	void buildingShouldNotValidateWhenAddressEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Building building = new Building();
		building.setName("Building");
		building.setAddress("");
		building.setIncome(0);
		Validator validator = createValidator();
		Set<ConstraintViolation<Building>> constraintViolations = validator.validate(building);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Building> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("address");
		assertThat(violation.getMessage()).isEqualTo("must not be blank");
	}

	@Test
	void buildingShouldNotValidateWhenIncomeNegative() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Building building = new Building();
		building.setName("Building");
		building.setAddress("c/Building");
		building.setIncome(-1);
		Validator validator = createValidator();
		Set<ConstraintViolation<Building>> constraintViolations = validator.validate(building);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Building> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("income");
		assertThat(violation.getMessage()).isEqualTo("must be greater than or equal to 0");
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tool

	@Test
	void toolShouldNotValidateWhenNameEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Tool tool = new Tool();
		tool.setName("");
		Validator validator = createValidator();
		Set<ConstraintViolation<Tool>> constraintViolations = validator.validate(tool);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Tool> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		assertThat(violation.getMessage()).isEqualTo("must not be blank");
	}

	////////////////////////////////////////////////////////////////////////////////
	// Task

	@Test
	void taskShouldNotValidateWhenNameEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Task task = new Task();
		task.setName("");
		task.setIncome(0);
		Validator validator = createValidator();
		Set<ConstraintViolation<Task>> constraintViolations = validator.validate(task);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Task> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		assertThat(violation.getMessage()).isEqualTo("must not be blank");
	}

	@Test
	void taskShouldNotValidateWhenIncomeNegative() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Task task = new Task();
		task.setName("Task");
		task.setIncome(-1);
		Validator validator = createValidator();
		Set<ConstraintViolation<Task>> constraintViolations = validator.validate(task);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Task> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("income");
		assertThat(violation.getMessage()).isEqualTo("must be greater than or equal to 0");
	}

	////////////////////////////////////////////////////////////////////////////////
	// WorkLog

	@Test
	void workLogShouldNotValidateWhenHoursNegative() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		WorkLog workLog = new WorkLog();
		workLog.setHours(-1);
		Validator validator = createValidator();
		Set<ConstraintViolation<WorkLog>> constraintViolations = validator.validate(workLog);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<WorkLog> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("hours");
		assertThat(violation.getMessage()).isEqualTo("must be greater than or equal to 0");
	}

	@Test
	void workLogShouldNotValidateWhenHoursAboveEight() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		WorkLog workLog = new WorkLog();
		workLog.setHours(9);
		Validator validator = createValidator();
		Set<ConstraintViolation<WorkLog>> constraintViolations = validator.validate(workLog);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<WorkLog> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("hours");
		assertThat(violation.getMessage()).isEqualTo("must be less than or equal to 8");
	}

}
