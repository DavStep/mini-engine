package engine;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public final class CodeUtils {

    private static final String SOURCE_ROOT = System.getProperty("project.sourceroot");

    private CodeUtils () {}

    public static void navigateToClass (final Class<?> clazz) {
        if (SOURCE_ROOT == null) {
            log.error("vm option -Dproject.sourceroot is not set");
			log.error("Clazz was: {}", clazz.getName());
            return;
        }

        try {
            String className = clazz.getName();
            final int nestedClassIndex = className.indexOf('$');
            if (nestedClassIndex != -1) {
                className = className.substring(0, nestedClassIndex);
            }

            final String classPath = className.replace('.', File.separatorChar) + ".java";
            final String fullPath = SOURCE_ROOT + File.separator + classPath;
            final File file = new File(fullPath);

            if (!file.exists()) {
                log.warn("Source file not found: {}", fullPath);
                return;
            }

            final String command = "idea " + file.getAbsolutePath();
            Runtime.getRuntime().exec(command);
            log.info("Opened source file: {}", fullPath);

        } catch (final IOException e) {
            log.error("Failed to execute navigation command", e);
        }
    }
}
