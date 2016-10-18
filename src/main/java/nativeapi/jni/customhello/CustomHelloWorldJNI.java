package nativeapi.jni.customhello;

/**
 * @author elvis
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 10/17/16 2:54 PM
 */
public class CustomHelloWorldJNI {
    static {

        System.out.println(System.getProperty("sun.arch.data.model"));
        System.load("/home/elvis/dev/projects/java-native-examples/src/main/resources/hello.o");
//        System.load(getResource("hello.o"));
    }

//    public native void hello();

    public static void main(String[] args) {



        final CustomHelloWorldJNI jni = new CustomHelloWorldJNI();
//        jni.hello();
    }


    public static String getResource(String name){
        final String currentPath = CustomHelloWorldJNI.class.getClassLoader()
                .getResource(CustomHelloWorldJNI.class.getPackage().getName().replaceAll("\\.", "/")).getPath();
        return currentPath + "/" + name;
    }

}
