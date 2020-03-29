package org.github.msx80.omicron.fantasyconsole.crazyclassloader.otf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.github.msx80.omicron.fantasyconsole.crazyclassloader.bytesloader.BytesLoader;

/**
 * A BytesLoader that tries to compile a set of java sources, compiled on the fly, to provide the required bytecode. 
 * 
 *
 */
public class CompilingLoader implements BytesLoader {

	Map<String, byte[]> classes = null;
	JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	Map<String, JavaFileObject> javaFileObjects = new HashMap<String, JavaFileObject>();
	private Map<String, byte[]> files;
	
	public CompilingLoader( Map<String, String> sources, Map<String, byte[]> files)
	{
		this.files = files;
		//this.sources = sources;
		for (Entry<String, String> e : sources.entrySet()) {
			javaFileObjects.put(e.getKey(), new JavaSourceFromString(e.getKey(), e.getValue()));
		}
	}
		
	boolean errors;
	
	@Override
	public byte[] loadFile(String filePath) {
		
		return files.get(filePath);
	}

	@Override
	public byte[] loadClass(String className) {
		
		try {
			if (classes == null) {
				classes = compileFromJava();
			}

			return classes.get(className);

		} catch (CompilationError e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Unable to load class " + className + ": " + e, e);
		}
		
	}

	
	private Map<String, byte[]> compileFromJava() {
        
		if(compiler==null) throw new CompilationError("JavaCompiler not found. Not running on a jdk?");
		
		List<Diagnostic<? extends JavaFileObject>> errors = new ArrayList<Diagnostic<? extends JavaFileObject>>();
		                 
         Collection<JavaFileObject> compilationUnits = javaFileObjects.values();
        
         
         StandardJavaFileManager s_standardJavaFileManager = compiler.getStandardFileManager(null, null, null);
         MyJavaFileManager s_fileManager = new MyJavaFileManager(s_standardJavaFileManager);
         
        compiler.getTask(null, s_fileManager, new DiagnosticListener<JavaFileObject>() {
            @Override
            public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
                if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                    errors.add(diagnostic);
                }
            }
        }, null, null, compilationUnits).call();
        
        Map<String, byte[]> result = s_fileManager.getAllBuffers();
        for (String cn : result.keySet()) {
			System.out.println("Compiled: "+cn);
		}
        if (!errors.isEmpty()) {
			throw new CompilationError(errors.get(0).toString());
		}
        return result;
    }

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
		
	
	
}
