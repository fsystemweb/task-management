it("Cancel Task - Count type", () => {
  cy.visit("http://localhost:4200");

  cy.get(".mat-focus-indicator").click();
  cy.get(
    ":nth-child(2) > .mat-list-item-content > .mat-list-text > .title"
  ).click();

  cy.get("#mat-input-0").type("2");
  cy.get("#mat-input-1").type("80");

  cy.get(":nth-child(2) > .mat-button-wrapper").click();

  cy.get(":nth-child(2) > .mat-focus-indicator > .mat-button-wrapper").click();

  cy.get(":nth-child(4) > .button-container > .mat-focus-indicator").click();

  cy.contains(".mat-list", "CANCELLED");
});
