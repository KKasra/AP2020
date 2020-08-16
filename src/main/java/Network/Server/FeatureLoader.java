package Network.Server;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FeatureLoader {

    public List<Class> load(String fileName) throws Exception {
        List list = new ArrayList();
        JarFile jarFile = new JarFile("out/artifacts/" + fileName + "/" + fileName + ".jar");
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + "out/artifacts/" + fileName + "/" + fileName + ".jar" +"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class") || je.getName().contains("$")){
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0,je.getName().length()-6);
            className = className.replace('/', '.');
            Class c = cl.loadClass("Game.GameStructure.features." + fileName + "." + className);
            list.add(c);

        }
        return list;
    }



    private static FeatureLoader instance;
    public static FeatureLoader getInstance() {
        if (instance == null)
            instance = new FeatureLoader();
        return instance;
    }
}
