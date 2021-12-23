it("Create Task - Generate Project", () => {
  cy.visit("http://localhost:4200");

  cy.get(".mat-focus-indicator").click();
  cy.get(
    ":nth-child(1) > .mat-list-item-content > .mat-list-text > .title"
  ).click();

  cy.get(".step-container > :nth-child(1) > span").should(
    "have.text",
    "Do you want to run the task?"
  );
});

it("Create Task - Count type", () => {
  cy.visit("http://localhost:4200");

  cy.get(".mat-focus-indicator").click();
  cy.get(
    ":nth-child(2) > .mat-list-item-content > .mat-list-text > .title"
  ).click();

  cy.get("#mat-input-0").type("2");
  cy.get("#mat-input-1").type("22");

  cy.get(":nth-child(4) > :nth-child(2)").click();

  cy.get(".step-container > :nth-child(1) > span").should(
    "have.text",
    "Do you want to run the task?"
  );
});

it("Create Task - Count type - Fail", () => {
  cy.visit("http://localhost:4200");

  cy.get(".mat-focus-indicator").click();
  cy.get(
    ":nth-child(2) > .mat-list-item-content > .mat-list-text > .title"
  ).click();

  cy.get("#mat-input-0").type("222");
  cy.get("#mat-input-1").type("22");

  cy.get(":nth-child(4) > :nth-child(2)").click();

  cy.get(".mat-simple-snackbar > span").should(
    "have.text",
    "Validation failed for argument [0] in public com.celonis.challenge.model.ProjectGenerationTask com.celonis.challenge.controllers.TaskController.createTask(com.celonis.challenge.model.ProjectGenerationTask): [Error in object 'projectGenerationTask': codes [CountTaskValidation.projectGenerationTask,CountTaskValidation]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [projectGenerationTask.,]; arguments []; default message []]; default message [Invalid Range: must be an integer, start must be minor that end, start must be mayor or equal to 0]] "
  );
});
