Feature: Hello World

  As a developer
  I want to be able to execute simple tests for a Hello World project using BDD
  So that I can use a BDD approach for any new project features

  Scenario: Should Print Hello World
    Given standard out is being captured
    When the run method is invoked
    Then the output should contain "Hello World"
