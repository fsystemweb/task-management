it("Delete Task - Generate Project", () => {
  cy.visit("http://localhost:4200");

  cy.get(".mat-focus-indicator").click();
  cy.get(
    ":nth-child(1) > .mat-list-item-content > .mat-list-text > .title"
  ).click();

  cy.get(".button-container > .mat-focus-indicator").click();

  cy.get(
    ":nth-child(1) > .mat-list-item-content > :nth-child(3) > app-button > .mat-focus-indicator"
  ).click();
});

it("Delete Task - Count type", () => {
  cy.visit("http://localhost:4200");

  cy.get(".mat-focus-indicator").click();
  cy.get(
    ":nth-child(2) > .mat-list-item-content > .mat-list-text > .title"
  ).click();

  cy.get("#mat-input-0").type("2");
  cy.get("#mat-input-1").type("22");

  cy.get(":nth-child(4) > :nth-child(2)").click();

  cy.get(".button-container > .mat-focus-indicator").click();

  cy.get(
    ":nth-child(1) > .mat-list-item-content > :nth-child(3) > app-button > .mat-focus-indicator"
  ).click();
});
