it("Execute Task - Generate Project", () => {
  cy.visit("http://localhost:4200");

  cy.get(".mat-focus-indicator").click();
  cy.get(
    ":nth-child(1) > .mat-list-item-content > .mat-list-text > .title"
  ).click();

  cy.get(":nth-child(2) > .mat-focus-indicator > .mat-button-wrapper").click();

  cy.get(".step-container > span").should(
    "have.text",
    "Task finished execution successfuly"
  );
});

it("Execute Task - Count type", () => {
  cy.visit("http://localhost:4200");

  cy.get(".mat-focus-indicator").click();
  cy.get(
    ":nth-child(2) > .mat-list-item-content > .mat-list-text > .title"
  ).click();

  cy.get("#mat-input-0").type("2");
  cy.get("#mat-input-1").type("3");

  cy.get(":nth-child(2) > .mat-button-wrapper").click();

  cy.get(":nth-child(2) > .mat-focus-indicator > .mat-button-wrapper").click();

  cy.wait(2000);

  cy.get(".step-container > span").should(
    "have.text",
    "Task finished execution successfulyCounted from 2 to 3 "
  );
});
