package net.futureset.app;

import static org.mockito.BDDMockito.then;

import java.io.PrintStream;
import java.util.function.BiConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;

@ExtendWith(MockitoExtension.class)
class HelloWorldTest {

    @AfterEach
    void restoreDefaultInitFunction() {
        HelloWorld.setInitFunction(HelloWorld.DEFAULT_INIT_FUNCTION);
    }

    @Test
    void runOutputsHelloWorld(@Mock PrintStream mockOut) throws Exception {
        PrintStream restore = System.out;
        try {
            System.setOut(mockOut);
            CommandLineRunner sut = new HelloWorld();
            sut.run();
            then(mockOut).should().println("Hello World");
        } finally {
            System.setOut(restore);
        }
    }

    @Test
    void mainInvokesDefaultFunction(@Mock BiConsumer<Class<?>, String[]> initFunction) {
        HelloWorld.setInitFunction(initFunction);

        String[] args = {"anArg"};
        HelloWorld.main(args);

        then(initFunction).should().accept(HelloWorld.class, args);
    }
}
