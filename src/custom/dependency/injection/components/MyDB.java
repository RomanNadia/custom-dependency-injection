package custom.dependency.injection.components;

import custom.dependency.injection.annotations.Component;

@Component
public class MyDB {

    public void connect() {
        System.out.println("connect DB");
    }

    public void disconnect() {
        System.out.println("disconnect DB");
    }
}
