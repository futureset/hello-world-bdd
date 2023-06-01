package net.futureset.test.acceptance.step;

import io.cucumber.java.en.When;
import org.springframework.boot.CommandLineRunner;

public class ApplicationExecution {

    private final CommandLineRunner commandLineRunner;

    public ApplicationExecution(CommandLineRunner commandLineRunner) {
        this.commandLineRunner = commandLineRunner;
    }

    @When("the run method is invoked")
    public void runMethodInvoked() throws Exception {
        commandLineRunner.run();
    }
}
