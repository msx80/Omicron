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
#include <string.h>
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


extern retro_log_printf_t log_cb;


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
	if(hinstLib && FreeLibrary(hinstLib)  == 0)
	{
		log_cb(RETRO_LOG_ERROR, "[JAVA] Could not free JAVA dll/so");
		return -1;
	}
#else
	if(handle && dlclose(handle)  != 0)
	{
		log_cb(RETRO_LOG_ERROR, "[JAVA] Could not free JAVA dll/so");
		return -1;
	}
	
#endif	
*/
	return 0;
}

int initJava(const char *omicronJarPath)
{
	JavaVMInitArgs  vm_args;
	jint            res;
	
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
			jclass          clazz;
			jmethodID       classForNameMethod;
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

			jstring classToLoad = (*env)->NewStringUTF(env, "com.github.msx80.omicron.libretro.entrypoint.EntryPoint");
			log_cb(RETRO_LOG_INFO, "[JAVA] Calling main\n");
			jmethodID urlConstructor = (*env)->GetMethodID(env, (*env)->FindClass(env, "java/net/URL"), "<init>", "(Ljava/lang/String;)V");
			size_t jarPathLen = sizeof("file://") + strlen(omicronJarPath);
			char *jarPath = malloc(jarPathLen);
			if (jarPath == NULL) {
				log_cb(RETRO_LOG_ERROR, "Malloc failed");
				return 3;
			}
			memcpy(jarPath, "file://", sizeof("file://") - 1);
			memcpy(jarPath + sizeof("file://") - 1, omicronJarPath, jarPathLen - sizeof("file://") + 1);
				
			jstring jarPathJString = (*env)->NewStringUTF(env, jarPath);
			jobject url = (*env)->NewObject(env, (*env)->FindClass(env, "java/net/URL"), urlConstructor, jarPathJString);
			(*env)->ReleaseStringUTFChars(env, jarPathJString, NULL);
			if(url == NULL){
				log_cb(RETRO_LOG_ERROR, "Couldn't create URL object");
				return 3;
			}
//			(*env)->CallStaticVoidMethod(env, bootstrapCls, mid, main_args);

			jobjectArray urls = (*env)->NewObjectArray(env, 1, (*env)->FindClass(env, "java/net/URL"), NULL);
			(*env)->SetObjectArrayElement(env, urls, 0, url);
			    
			jclass classLoaderClass = (*env)->FindClass(env,"java/net/URLClassLoader");
			jmethodID constructor = (*env)->GetMethodID(env, classLoaderClass, "<init>", "([Ljava/net/URL;)V");
			jobject classLoader = (*env)->NewObject(env, classLoaderClass, constructor, urls);
			log_cb(RETRO_LOG_INFO, "[JAVA] Classloader created\n");
	
			clazz = (*env)->FindClass(env, "java/lang/Class");
			classForNameMethod = (*env)->GetStaticMethodID(env, clazz, "forName", "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;");
			if(classForNameMethod == NULL){
				log_cb(RETRO_LOG_ERROR, "[JAVA] Failed to find Class.forName\n");
				return 3;
			}

			entryPointCls = (jclass) (*env)->CallStaticObjectMethod(env, clazz, classForNameMethod, classToLoad, true, classLoader);
			(*env)->ReleaseStringUTFChars(env, classToLoad, NULL);
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

		entryPointCls = (*env)->FindClass(env,"com/github/msx80/omicron/libretro/entrypoint/EntryPoint");
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






