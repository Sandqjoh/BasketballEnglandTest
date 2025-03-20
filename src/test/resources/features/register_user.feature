Feature: Registrering av ny användare

  Scenario: Lyckad registrering av ny användare
    Given att jag är på registreringssidan för nya användare
    When jag fyller i alla nödvändiga fält korrekt
    And jag accepterar användarvillkoren och samtycker till alla nödvändiga checkboxar
    And jag klickar på registreringsknappen
    Then ska en bekräftelse på lyckad registrering visas

  Scenario: Försök att registrera en användare utan efternamn
    Given att jag är på registreringssidan för nya användare
    When jag fyller i alla nödvändiga fält korrekt, men lämnar efternamn tomt
    And jag accepterar användarvillkoren och samtycker till alla nödvändiga checkboxar
    And jag klickar på registreringsknappen
    Then ska jag se ett felmeddelande

  Scenario: Användaren försöker registrera sig med olika lösenord och bekräftelselösenord
    Given att jag är på registreringssidan för nya användare
    When jag fyller i alla nödvändiga fält korrekt, men lösenorden matchar inte
    And jag accepterar användarvillkoren och samtycker till alla nödvändiga checkboxar
    And jag klickar på registreringsknappen
    Then ska jag se ett felmeddelande om att lösenorden inte matchar


  Scenario: Jag fyller i alla nödvändiga fält korrekt men terms and conditions är inte ibockad
    Given att jag är på registreringssidan för nya användare
    When jag fyller i alla nödvändiga fält korrekt, men terms and conditions är inte ibockad
    And jag klickar på registreringsknappen
    Then ska jag se ett felmeddelande om att användarvillkoren inte är accepterade



