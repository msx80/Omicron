package org.github.msx80.crazyclassloader.otf;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.github.msx80.crazyclassloader.BytesLoader;

/**
 * A BytesLoader that tries to compile a set of java sources, compiled on the fly, to provide the required bytecode. 
 * 
 *
 */
public class CompilingLoader implements BytesLoader {

	Map<String, byte[]> classes = null;
	JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	Map<String, JavaFileObject> javaFileObjects = new HashMap<String, JavaFileObject>();
	
	public CompilingLoader( Map<String, String> sources)
	{
		//this.sources = sources;
		for (Entry<String, String> e : sources.entrySet()) {
			javaFileObjects.put(e.getKey(), new JavaSourceFromString(e.getKey(), e.getValue()));
		}
	}
		
	boolean errors;
	
	@Override
	public byte[] loadFile(String filePath) {
		
		return null;
	}

	@Override
	public byte[] loadClass(String className) {
		
		try {			
			if(classes == null)
			{
				classes = compileFromJava();
			}
			
			return classes.get(className);
		
		} catch (Exception e) {
			throw new RuntimeException("Unable to load class "+className+": "+e, e);
		}
		
	}

	
	private Map<String, byte[]> compileFromJava() {
        
		if(compiler==null) throw new RuntimeException("JavaCompiler not found. Not running on a jdk?");
		
		errors = false;
		                 
         Collection<JavaFileObject> compilationUnits = javaFileObjects.values();
        
         
         StandardJavaFileManager s_standardJavaFileManager = compiler.getStandardFileManager(null, null, null);
         MyJavaFileManager s_fileManager = new MyJavaFileManager(s_standardJavaFileManager);
         
        compiler.getTask(null, s_fileManager, new DiagnosticListener<JavaFileObject>() {
            @Override
            public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
                if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                    errors = true;
                    System.err.println(diagnostic);
                }
            }
        }, null, null, compilationUnits).call();
        
        Map<String, byte[]> result = s_fileManager.getAllBuffers();
        for (String cn : result.keySet()) {
			System.out.println("Compiled: "+cn);
		}
       
        return result;
    }
		
	
	
}
