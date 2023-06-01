package net.futureset.test.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import jakarta.annotation.PreDestroy;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class CaptureOutput {

    private PrintStream restore = null;
    private final ByteArrayOutputStream captured = new ByteArrayOutputStream();

    @Given("standard out is being captured")
    public void captureStdOut() {
        if (restore == null) {
            restore = System.out;
            System.setOut(new PrintStream(captured));
        }
    }

    @Then("the output should contain {string}")
    public void stdOutShouldContain(String text) throws IOException {
        captured.flush();
        assertThat(captured.toString(StandardCharsets.UTF_8)).contains(text);
    }

    @PreDestroy
    public void restore() {
        if (restore != null) {
            try {
                System.out.close();
            } finally {
                System.setOut(restore);
                restore = null;
            }
        }
    }
}
