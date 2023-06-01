package net.futureset.app;

import java.util.function.BiConsumer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloWorld implements CommandLineRunner {

    public static final BiConsumer<Class<?>, String[]> DEFAULT_INIT_FUNCTION = SpringApplication::run;
    private static BiConsumer<Class<?>, String[]> initFunction = DEFAULT_INIT_FUNCTION;

    public static void main(String... args) {
        initFunction.accept(HelloWorld.class, args);
    }

    static void setInitFunction(BiConsumer<Class<?>, String[]> initFunction) {
        HelloWorld.initFunction = initFunction;
    }

    @Override
    public void run(String... args) {
        System.out.println("Hello World");
    }
}
