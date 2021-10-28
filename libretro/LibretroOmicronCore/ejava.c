#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#if defined(_WIN32) && !defined(_XBOX)
#define M_PI 3.14159
#include <windows.h>
#else
#include <dlfcn.h>
#endif
#include <jni.h>
#include "bootstrap.h"
#include "libretro.h"

	static JavaVM         *vm;
	static JNIEnv         *env;
	static jclass          entryPointCls;
	static jmethodID 		callLoop;
	//static jmethodID 		callSetup;
	static jmethodID 		callLoadGame;	
	static jmethodID 		callUnloadGame;	
	static jmethodID 		callResetContext;
	static jmethodID 		callContextDestroy;
	static jmethodID 		callSysInfo;


#if defined(_WIN32)
HINSTANCE hinstLib; 
#else
void* handle;
#endif


static retro_log_printf_t log_cb = NULL;


// types for the two system call we're making

typedef jint (JNICALL *dynJNI_CreateJavaVM)(JavaVM **pvm, void **penv, void *args);
typedef jint (JNICALL *dynJNI_GetCreatedJavaVMs)(JavaVM ** vm, jsize nVMs, jsize *Size );

dynJNI_CreateJavaVM mydynJNI_CreateJavaVM;
dynJNI_GetCreatedJavaVMs mydynJNI_GetCreatedJavaVMs;


int fileExists(char* fname)
{
	if( access( fname, F_OK ) != -1 ) {
	    return -1;
	} else {
	    return 0;
	}
	return 0;
}

int testFolder(int state, char* buf, size_t size, const char* javaHome, const char* subpath, const char* file)
{
	if(state != -2) return state; // if already found or fatal error, propagate and 
	
	int s = snprintf(buf, size, "%s/%s%s", javaHome, subpath , file);
	if(s<0) {
		log_cb(RETRO_LOG_ERROR, "[JAVA] snprintf returned error %d \n", s);
		return -1; // fatal error
	}
	if(!fileExists(buf))
	{
		log_cb(RETRO_LOG_INFO, "[JAVA] File not exists: %s\n", buf);
		return -2; // all ok but file not found
	}
	log_cb(RETRO_LOG_INFO, "[JAVA] File Exists!: %s\n", buf);
	return 0; // file found
}

int locateJavaDLL(char* buf, size_t size, const char* file)
{
	log_cb(RETRO_LOG_INFO, "[JAVA] Getting JAVA_HOME\n");
	char* javaHome = getenv("JAVA_HOME");
	log_cb(RETRO_LOG_INFO, "[JAVA] JAVA_HOME: %s\n",(javaHome!=NULL)? javaHome : "NULL");
	if(!javaHome)
	{
		// we could search PATH variable for java paths and find some dll around
		log_cb(RETRO_LOG_ERROR, "[JAVA] JAVA_HOME is not set! Cannot start without it (for now)\n");
		return -1;
	}
	log_cb(RETRO_LOG_INFO, "[JAVA] Looking around for %s ...\n", file);
	
	// just some positions that i found around, totally empiric
	int s = -2;
	s = testFolder(s, buf, size, javaHome, "lib/server/", file);
	s = testFolder(s, buf, size, javaHome, "jre/bin/server/", file);
	s = testFolder(s, buf, size, javaHome, "jre/lib/amd64/server/", file);
	s = testFolder(s, buf, size, javaHome, "jre/lib/aarch64/server/", file);
	
	s = testFolder(s, buf, size, javaHome, "bin/server/", file);
	
	s = testFolder(s, buf, size, javaHome, "bin/client/", file);
	s = testFolder(s, buf, size, javaHome, "jre/bin/client/", file);
	
	if(s==-2) {
		log_cb(RETRO_LOG_ERROR, "[JAVA] Unable to locate %s inside JAVA_HOME \n", file);
		return -1;
	}
	if(s==-1) {
		log_cb(RETRO_LOG_ERROR, "[JAVA] Error locating %s inside JAVA_HOME \n", file);
		return -1;
	}
	
	log_cb(RETRO_LOG_INFO, "[JAVA] USING JVM: %s\n", buf);
	return 0;
}

int loadJavaLib()
{

	
	log_cb(RETRO_LOG_INFO, "[JAVA] Loading JAVA dll\n");

	char dllpath[1024];
	
#if defined(_WIN32) 
	
	int s = locateJavaDLL(dllpath, 1024, "jvm.dll");
	if(s<0) {
		log_cb(RETRO_LOG_ERROR, "[JAVA] Unable to calculate java dll/so path \n");
		return -1;
	}
	
	
	log_cb(RETRO_LOG_INFO, "[JAVA] LOADING DLL/SO from : %s\n", dllpath);
	hinstLib = LoadLibrary(TEXT(dllpath)); 
	
	if (hinstLib == NULL)
	{
		log_cb(RETRO_LOG_ERROR, "[JAVA] Unable to load jvm.dll/so\n");
		return -1;
	 } 
	     
	mydynJNI_CreateJavaVM = (dynJNI_CreateJavaVM)GetProcAddress(hinstLib, "JNI_CreateJavaVM");
	if (NULL == mydynJNI_CreateJavaVM) 
    {
        log_cb(RETRO_LOG_ERROR, "[JAVA] Unable to locate function JNI_CreateJavaVM\n");
        return -1;
    }
    
    mydynJNI_GetCreatedJavaVMs = (dynJNI_GetCreatedJavaVMs) GetProcAddress(hinstLib, "JNI_GetCreatedJavaVMs");
	if (NULL == mydynJNI_GetCreatedJavaVMs) 
    {
        log_cb(RETRO_LOG_ERROR, "[JAVA] Unable to locate function JNI_GetCreatedJavaVMs\n");
        return -1;
    }
	

    return 0;
#else

	int s = locateJavaDLL(dllpath, 1024, "libjvm.so");
	if(s<0) {
		log_cb(RETRO_LOG_ERROR, "Unable to calculate java so path \n");
		return -1;
	}


	handle = dlopen(dllpath, RTLD_NOW); // or RTLD_LAZY, no difference.
	if (!handle){
		log_cb(RETRO_LOG_ERROR, "[JAVA] Unable to load jvm.so\n");
		return -1;
	}
	mydynJNI_CreateJavaVM = dlsym(handle, "JNI_CreateJavaVM");
	if (!mydynJNI_CreateJavaVM){
		log_cb(RETRO_LOG_ERROR, "[JAVA] Unable to find mydynJNI_CreateJavaVM\n");
		return -1;
	}
    mydynJNI_GetCreatedJavaVMs = dlsym(handle, "JNI_GetCreatedJavaVMs");
        if (NULL == mydynJNI_GetCreatedJavaVMs)
    {
        log_cb(RETRO_LOG_ERROR, "[JAVA] Unable to locate function JNI_GetCreatedJavaVMs\n");
        return -1;
    }


	/*mydynJNI_DestroyJavaVM = (dynJNI_DestroyJavaVM)dlsym(handle, "JNI_DestroyJavaVM");
	if (NULL == mydynJNI_DestroyJavaVM) 
    {
        logError("Unable to locate function JNI_DestroyJavaVM\n");
        return -1;
    }*/
	return 0;
 #endif
 

}


int deinitJava()
{
	log_cb(RETRO_LOG_INFO, "[JAVA] Deinitin java\n");

/*	
	// turns out you can get only one VM per process, even if you destroy it, you cannot create any more.
	// so we keep the same one around and carry on.
#if defined(_WIN32) 
	if( FreeLibrary(hinstLib)  == 0)
	{
		log_cb(RETRO_LOG_ERROR, "[JAVA] Could not free JAVA dll/so");
		return -1;
	}
#else
	if( dlclose(handle)  != 0)
	{
		log_cb(RETRO_LOG_ERROR, "[JAVA] Could not free JAVA dll/so");
		return -1;
	}
	
#endif	
*/
	return 0;
}
int initJava(retro_log_printf_t logger, const char *omicronJarPath)
{
	log_cb = logger;
	JavaVMInitArgs  vm_args;
	jint            res;
	jclass          bootstrapCls;
	//jobject			obj;
	jmethodID       mid;
	jmethodID       mid2;
	jstring         jstr;
	jobjectArray    main_args;
	
	log_cb(RETRO_LOG_INFO, "[JAVA] INITIALIZING JAVA VM\n");

	if(loadJavaLib() != 0)
	{
		log_cb(RETRO_LOG_ERROR, "[JAVA] Could not dynamically load jvm dll/so\n");
		return 4;
	}
	
	log_cb(RETRO_LOG_INFO, "[JAVA] Querying already running JVMs\n");
	jint nVMs = 5; // just need one as java is only able to run a single JVM per process even if it's destroyed, you can't create another one
	JavaVM* buffer[5];
	res = mydynJNI_GetCreatedJavaVMs(buffer, nVMs, &nVMs); 
	if(res != JNI_OK )
	{
		log_cb(RETRO_LOG_ERROR, "[JAVA] Unable to query previously created VMs\n");
		return -1;
	}
	log_cb(RETRO_LOG_INFO, "[JAVA] Number of already created JVMs: %d\n", nVMs);


	if(nVMs == 0)
	{
		// let's setup everything
	
			log_cb(RETRO_LOG_INFO, "[JAVA] Initializing new VM\n");
			memset(&vm_args, 0, sizeof(vm_args));
			vm_args.version  = JNI_VERSION_1_8;
			vm_args.nOptions = 0;
			vm_args.options = NULL;
		
			res = mydynJNI_CreateJavaVM(&vm, (void **)&env, &vm_args);
			if (res != JNI_OK) {
				log_cb(RETRO_LOG_ERROR, "[JAVA] Failed to create Java VM: %d\n", res);
				return 4;
			}
			bootstrapCls = (*env)->DefineClass(env, "Bootstrap", NULL, BOOTSTRAP_CLASS, BOOTSTRAP_CLASS_SIZE);
	
			if (bootstrapCls == NULL) {
				log_cb(RETRO_LOG_ERROR, "[JAVA] Failed to initialize Bootstrap class\n");
				return 3;
			}
			
			mid = (*env)->GetStaticMethodID(env, bootstrapCls, "main", "([Ljava/lang/String;)V");
			if (mid == NULL) {
				log_cb(RETRO_LOG_ERROR, "[JAVA] Failed to find main functionn\n");
				return 2;
			}
		
			jstr      = (*env)->NewStringUTF(env, "");
			main_args = (*env)->NewObjectArray(env, 2, (*env)->FindClass(env, "java/lang/String"), jstr);
			
			(*env)->SetObjectArrayElement(env, main_args, 0, (*env)->NewStringUTF(env, "com.github.msx80.omicron.libretro.entrypoint.EntryPoint"));
		#if defined(_WIN32) 	
			(*env)->SetObjectArrayElement(env, main_args, 1, (*env)->NewStringUTF(env, 
			//"system/omicron/omicron.jar")); 
			omicronJarPath));
			
			//"C:\\Users\\niclugat\\dev\\LibretroOmicron\\omicron-libretro-java\\target\\omicron-libretro-java-0.0.1-jar-with-dependencies.jar"));
		#else
		    (*env)->SetObjectArrayElement(env, main_args, 1, (*env)->NewStringUTF(env, 
		     	omicronJarPath));
			 //"/home/nicola/LibretroOmicron/omicron-libretro-java-0.0.1-jar-with-dependencies.jar"));
		#endif	
			log_cb(RETRO_LOG_INFO, "[JAVA] Calling main\n");
			(*env)->CallStaticVoidMethod(env, bootstrapCls, mid, main_args);
		

	}
	else
	{
		// use already made JVM. Bootstrap is already there and loaded, just get the instance to class
//		log_cb(RETRO_LOG_INFO, "[JAVA] Attaching thread\n");
//		(*vm)->AttachCurrentThread(vm, env, NULL);
		

		log_cb(RETRO_LOG_INFO, "[JAVA] Reusing previously started JVM");
		vm = buffer[0];

		
		int result = (*vm)->GetEnv(vm,  (void**) &env, JNI_VERSION_1_8);
		if(result != 0)
		{
			log_cb(RETRO_LOG_ERROR, "[JAVA] Unable to regain VM: %d\n", result);
			return result;
		}

		bootstrapCls = (*env)->FindClass(env, "Bootstrap");
		if (bootstrapCls == NULL) {
				log_cb(RETRO_LOG_ERROR, "[JAVA] Failed to initialize Bootstrap class\n");
				return 3;
		}
	}

	
	
	mid2 = (*env)->GetStaticMethodID(env, bootstrapCls, "getMainClass", "()Ljava/lang/Class;");
	if (mid2 == NULL) {
		log_cb(RETRO_LOG_ERROR, "[JAVA] Failed to find getRunnable functionn");
		return 2;
	}
	


	entryPointCls = (*env)->CallStaticObjectMethod(env, bootstrapCls, mid2, NULL);
	if (entryPointCls == NULL) {
		log_cb(RETRO_LOG_ERROR, "[JAVA] Failed to get entryPointCls\n");
		return 3;
	}
	
	callLoop = (*env)->GetStaticMethodID(env, entryPointCls, "callLoop", "(III)V");
	if (callLoop == NULL) {
		log_cb(RETRO_LOG_ERROR, "[JAVA] Failed to find callLoop functionn");
		return 2;
	}
	
/*	callSetup = (*env)->GetStaticMethodID(env, entryPointCls, "callSetup", "()V");
	
	if (callSetup == NULL) {
		log_cb(RETRO_LOG_ERROR, "[JAVA] Failed to find callSetup functionn");
		return 2;
	}
*/
	callResetContext = (*env)->GetStaticMethodID(env, entryPointCls, "callResetContext", "()V");
	
	if (callResetContext == NULL) {
		log_cb(RETRO_LOG_ERROR, "[JAVA] Failed to find callResetContext functionn");
		return 2;
	}
	callSysInfo = (*env)->GetStaticMethodID(env, entryPointCls, "sysInfo", "(I)I");
	
	if (callSysInfo == NULL) {
		log_cb(RETRO_LOG_ERROR, "[JAVA] Failed to find sysInfo functionn");
		return 2;
	}

	callContextDestroy = (*env)->GetStaticMethodID(env, entryPointCls, "callContextDestroy", "()V");
	
	if (callContextDestroy == NULL) {
		log_cb(RETRO_LOG_ERROR, "[JAVA] Failed to find callContextDestroy functionn");
		return 2;
	}
	callLoadGame = (*env)->GetStaticMethodID(env, entryPointCls, "callLoadGame", "(Ljava/lang/String;)V");
	if (callLoadGame == NULL) {
		log_cb(RETRO_LOG_ERROR, "[JAVA] Failed to find callLoadGame functionn");
		return 2;
	}
	
	callUnloadGame = (*env)->GetStaticMethodID(env, entryPointCls, "callUnloadGame", "()V");
	if (callUnloadGame == NULL) {
		log_cb(RETRO_LOG_ERROR, "[JAVA] Failed to find callUnloadGame functionn");
		return 2;
	}
	
	log_cb(RETRO_LOG_INFO, "[JAVA] All JAVA setup done\n");

	return 0;	
}

int javaSysInfo(int width)
{
	int size = (*env)->CallStaticIntMethod(env, entryPointCls, callSysInfo, width);
	return size;
}
void javaLoop(int ctrlStat, int mx, int my)
{
	jint c2 = ctrlStat; // not sure what i'm doing
    jint mx2 = mx;
    jint my2 = my;
    (*env)->CallStaticVoidMethod(env, entryPointCls, callLoop, c2, mx2, my2);	
	
	if ( (*env)->ExceptionOccurred(env)  ) {
		log_cb(RETRO_LOG_INFO, "[JAVA] EXCEPTION IN LOOP\n");
        (*env) -> ExceptionDescribe(env);
            // return NS_ERROR_FAILURE;
    }
    
	//ExceptionCheck
 //We introduce a convenience function to check for pending exceptions without creating a local reference to the exception object.
  /* if ((*env)->ExceptionCheck()) {
   	  log_cb(RETRO_LOG_INFO, "[JAVA] EXCEPTION TRUE!\n");
   	  (*env)->ExceptionDescribe();
   }*/


}

/*
void javaSetup()
{
	(*env)->CallStaticVoidMethod(env, entryPointCls, callSetup, NULL);
}
*/
void javaResetContext()
{
	(*env)->CallStaticVoidMethod(env, entryPointCls, callResetContext, NULL);
}

void javaLoadGame(const char* path)
{
	(*env)->CallStaticVoidMethod(env, entryPointCls, callLoadGame, (*env)->NewStringUTF(env, path));
}

void javaUnloadGame()
{
	log_cb(RETRO_LOG_INFO, "[JAVA] CALLING UNLOAD GAME\n");
	(*env)->CallStaticVoidMethod(env, entryPointCls, callUnloadGame);
}

void javaContextDestroy()
{
	log_cb(RETRO_LOG_INFO, "[JAVA] CALLING CONTEXTDESTROY\n");
	(*env)->CallStaticVoidMethod(env, entryPointCls, callContextDestroy, NULL);
//	log_cb(RETRO_LOG_INFO, "[JAVA] Detaching thread\n");
//	(*vm)->DetachCurrentThread(vm);
}






