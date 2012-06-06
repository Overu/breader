package com.goodow.web.core.apt;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.EnumSet;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import com.goodow.web.core.apt.WebGenerator;

public class AbstractGeneratorTest extends TestCase {

  public void generate() throws IOException {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null) {
      // This test is being run without a full JDK
      return;
    }

    DiagnosticCollector<JavaFileObject> actualCollector = new DiagnosticCollector<JavaFileObject>();
    StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
    manager.setLocation(StandardLocation.SOURCE_PATH, Arrays.asList(new File("src/main/java")));
    manager.setLocation(StandardLocation.SOURCE_OUTPUT, Arrays.asList(new File(
        "target/generated-sources/apt")));

    Iterable<? extends JavaFileObject> units =
        manager.list(StandardLocation.SOURCE_PATH, "", EnumSet
            .of(javax.tools.JavaFileObject.Kind.SOURCE), true);

    Writer errorWriter = new OutputStreamWriter(System.out);

    WebGenerator rfValidator = new WebGenerator();

    CompilationTask actualTask =
        compiler.getTask(errorWriter, manager, actualCollector, Arrays.asList("-proc:only"), null,
            units);
    actualTask.setProcessors(Arrays.asList(rfValidator));
    actualTask.call();
  }
}
