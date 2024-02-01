package custom.dependency.injection.components;

import custom.dependency.injection.annotations.Autowired;
import custom.dependency.injection.annotations.Component;

@Component
public class MyService {
    @Autowired
    private MyDB myDB;

    public void saveToBD() {
        myDB.connect();
        System.out.println("save to BD");
        myDB.disconnect();
    }

}
