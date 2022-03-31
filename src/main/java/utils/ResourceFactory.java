package utils;

import org.apache.commons.io.FileUtils;
import ru.lanit.at.exceptions.FrameworkRuntimeException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;


public class ResourceFactory {

    public synchronized static String stringFromFileBy(String path, String... params) {
        String scriptString;
        File scriptFile;
        try {
            scriptFile = new File(ResourceFactory.class.getClassLoader().getResource(path).toURI());
        } catch (Exception e) {
            throw new FrameworkRuntimeException("Can not locate File for resource path = " + path);
        }
        try {
            scriptString = FileUtils.readFileToString(scriptFile, Charset.defaultCharset());
            if (scriptString != null && !scriptString.trim().isEmpty()) {
                if (params != null && params.length > 0) {
                    scriptString = String.format(scriptString, (Object[]) params);
                }
            } else {
                throw new FrameworkRuntimeException("File template is empty. Path = " + path);
            }
        } catch (IOException e) {
            throw new FrameworkRuntimeException(path, e);
        }
        return scriptString;
    }
}