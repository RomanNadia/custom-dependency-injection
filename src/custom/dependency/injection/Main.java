package custom.dependency.injection;

import custom.dependency.injection.annotations.Autowired;
import custom.dependency.injection.components.MyService;
import custom.dependency.injection.annotations.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        String packageName = "custom.dependency.injection.components";

        try {
            ArrayList<Class<?>> allClasses = getClassesFromPackage(packageName);
            HashMap<Class<?>, ArrayList> allInstances = new HashMap<>();

            for (Class<?> c : allClasses) {
                ArrayList arrayList = new ArrayList<>();

                if(c.isAnnotationPresent(Component.class) && !allInstances.containsKey(c)) {
                    arrayList.add(c.newInstance());
                    allInstances.put(c, arrayList);
                }

                Field[] fields = c.getDeclaredFields();
                for(Field f: fields) {
                    if(f.isAnnotationPresent(Autowired.class)) {
                        Class<?> fieldType = f.getType();
                        f.setAccessible(true);

                        if (!allInstances.containsKey(c)) {
                            ArrayList fieldTypeArrayList = new ArrayList<>();
                            arrayList.add(fieldType.newInstance());
                            allInstances.put(fieldType, fieldTypeArrayList);
                        }

                        f.set(allInstances.get(c).get(0), allInstances.get(fieldType).get(0));
                    }
                }
            }

            MyService myService = (MyService) allInstances.get(MyService.class).get(0);
            myService.saveToBD();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static ArrayList<Class<?>> getClassesFromPackage(String packageName) {
        String path = packageName.replace('.', '/');
        ArrayList<Class<?>> classes = new ArrayList<>();

        try {
            File directory = new File(ClassLoader.getSystemResource(path).toURI());
            for (File file : directory.listFiles()) {
                if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().replace(".class", "");
                    Class<?> clas = Class.forName(className);
                    classes.add(clas);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
    }


}
