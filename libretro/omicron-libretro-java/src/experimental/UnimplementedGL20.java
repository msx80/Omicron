package experimental;

import static org.lwjgl.system.Checks.CHECKS;
import static org.lwjgl.system.Checks.check;
import static org.lwjgl.system.MemoryStack.stackGet;
import static org.lwjgl.system.MemoryUtil.memASCII;
import static org.lwjgl.system.MemoryUtil.memAddress;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.APIUtil;
import org.lwjgl.system.JNI;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.NativeType;

@Deprecated
public class UnimplementedGL20 implements com.badlogic.gdx.graphics.GL20 {

	private ByteBuffer buffer = null;
	private FloatBuffer floatBuffer = null;
	private IntBuffer intBuffer = null;

	private void ensureBufferCapacity (int numBytes) {
		if (buffer == null || buffer.capacity() < numBytes) {
			buffer = com.badlogic.gdx.utils.BufferUtils.newByteBuffer(numBytes);
			floatBuffer = buffer.asFloatBuffer();
			intBuffer = buffer.asIntBuffer();
		}
	}

	private FloatBuffer toFloatBuffer (float v[], int offset, int count) {
		ensureBufferCapacity(count << 2);
		floatBuffer.clear();
		floatBuffer.limit(count);
		floatBuffer.put(v,  offset, count);
		floatBuffer.position(0);
		return floatBuffer;
	}

	private IntBuffer toIntBuffer (int v[], int offset, int count) {
		ensureBufferCapacity(count << 2);
		intBuffer.clear();
		intBuffer.limit(count);
		intBuffer.put(v, offset, count);
		intBuffer.position(0);
		return intBuffer;
	}
	
	
	@Override
	public void glActiveTexture(int texture) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglBindTexture = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glBindTexture");
	@Override
	public void glBindTexture(int target, int texture) {
		System.out.println("Binding texture "+target+" "+texture);
		JNI.callV(addrglBindTexture, target, texture);

	}

	@Override
	public void glBlendFunc(int sfactor, int dfactor) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglClear = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glClear");
	@Override
	public void glClear(int mask) {
		JNI.callV(addrglClear, mask);

	}

	long addrglClearColor = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glClearColor");
	@Override
	public void glClearColor(float red, float green, float blue, float alpha) {
		JNI.callV(addrglClearColor,red, green, blue, alpha);

	}

	@Override
	public void glClearDepthf(float depth) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glClearStencil(int s) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border,
			int imageSize, Buffer data) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height,
			int format, int imageSize, Buffer data) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height,
			int border) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width,
			int height) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glCullFace(int mode) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteTextures(int n, IntBuffer textures) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteTexture(int texture) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDepthFunc(int func) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglDepthMask = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glDepthMask");
	@Override
	public void glDepthMask(boolean flag) {
		JNI.callP(addrglDepthMask, flag ? 1: 0);

	}

	@Override
	public void glDepthRangef(float zNear, float zFar) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglDisable = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glDisable");
	@Override
	public void glDisable(int cap) {
		JNI.callV(addrglDisable,cap);

	}

	@Override
	public void glDrawArrays(int mode, int first, int count) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDrawElements(int mode, int count, int type, Buffer indices) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglEnable = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glEnable");
	@Override
	public void glEnable(int cap) {
		JNI.callV(addrglEnable, cap);

	}

	@Override
	public void glFinish() {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glFlush() {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glFrontFace(int mode) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGenTextures(int n, IntBuffer textures) {
		throw new RuntimeException("Unimplemented method");

	}
	
	long addrglGenTextures = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glGenTextures");
	@NativeType("void")
    public int glGenTextures() {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            IntBuffer textures = stack.callocInt(1);
            
            JNI.callPV(addrglGenTextures, 1, memAddress(textures));
            return textures.get(0);
        } finally {
            stack.setPointer(stackPointer);
        }
    }

	
	@Override
	public int glGenTexture() {
		
		return glGenTextures();
		
		
	}

	@Override
	public int glGetError() {
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	@Override
	public void glGetIntegerv(int pname, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public String glGetString(int name) {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public void glHint(int target, int mode) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glLineWidth(float width) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglPixelStorei = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glPixelStorei");
	@Override
	public void glPixelStorei(int pname, int param) {
		JNI.callV(addrglPixelStorei, pname, param);

	}

	@Override
	public void glPolygonOffset(float factor, float units) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glReadPixels(int x, int y, int width, int height, int format, int type, Buffer pixels) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glScissor(int x, int y, int width, int height) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glStencilFunc(int func, int ref, int mask) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glStencilMask(int mask) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glStencilOp(int fail, int zfail, int zpass) {
		throw new RuntimeException("Unimplemented method");

	}

	
	long addrglTexImage2D = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glTexImage2D");

	@Override
	public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format,
			int type, Buffer pixels) {

		JNI.callPV(addrglTexImage2D, target, level, internalformat, width, height, border, format, type, pixels==null ? 0 : memAddress(pixels));

	}

	@Override
	public void glTexParameterf(int target, int pname, float param) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format,
			int type, Buffer pixels) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glViewport(int x, int y, int width, int height) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglAttachShader = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glAttachShader");
	@Override
	public void glAttachShader(int program, int shader) {
		JNI.callV(addrglAttachShader, program, shader);

	}

	@Override
	public void glBindAttribLocation(int program, int index, String name) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglBindBuffer = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glBindBuffer");
	@Override
	public void glBindBuffer(int target, int buffer) {
		System.out.println("Binding buffer "+target+" "+buffer);
		JNI.callV(addrglBindBuffer, target, buffer);

	}

	@Override
	public void glBindFramebuffer(int target, int framebuffer) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glBindRenderbuffer(int target, int renderbuffer) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glBlendColor(float red, float green, float blue, float alpha) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glBlendEquation(int mode) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glBlendEquationSeparate(int modeRGB, int modeAlpha) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglBlendFuncSeparate = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glBlendFuncSeparate");
	@Override
	public void glBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
		JNI.callV(addrglBlendFuncSeparate, srcRGB, dstRGB, srcAlpha, dstAlpha);

	}

	long addrglBufferData = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glBufferData");
	@Override
	public void glBufferData(int target, int size, Buffer data, int usage) {
		JNI.callPV(addrglBufferData, target, size, data==null ? 0 : memAddress(data), usage);
		//throw new RuntimeException("Unimplemented method");

	}

	long addrglBufferSubData = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glBufferSubData");
	@Override
	public void glBufferSubData(int target, int offset, int size, Buffer data) {
		JNI.callPV(addrglBufferSubData, target, offset, size, data==null ? 0 : memAddress(data));

	}

	@Override
	public int glCheckFramebufferStatus(int target) {
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	long addrglCompileShader = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glCompileShader");
	@Override
	public void glCompileShader(int shader) {
		JNI.callV(addrglCompileShader, shader);

	}

	long addrglCreateProgram = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glCreateProgram");
	@Override
	public int glCreateProgram() {
		return JNI.callI(addrglCreateProgram);
	}

	long addrglCreateShader = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glCreateShader");
	@Override
	public int glCreateShader(int type) {
		return JNI.callI(addrglCreateShader, type);
		//return 0;
	}

	@Override
	public void glDeleteBuffer(int buffer) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteBuffers(int n, IntBuffer buffers) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteFramebuffer(int framebuffer) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteFramebuffers(int n, IntBuffer framebuffers) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteProgram(int program) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteRenderbuffer(int renderbuffer) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteRenderbuffers(int n, IntBuffer renderbuffers) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteShader(int shader) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDetachShader(int program, int shader) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDisableVertexAttribArray(int index) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglDrawElements = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glDrawElements");
	@Override
	public void glDrawElements(int mode, int count, int type, int indices) {
		JNI.callV(addrglDrawElements, mode, count, type, indices);

	}

	@Override
	public void glEnableVertexAttribArray(int index) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public int glGenBuffer() {
		MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            IntBuffer buffers = stack.callocInt(1);
            glGenBuffers(1, buffers);
            return buffers.get(0);
        } finally {
            stack.setPointer(stackPointer);
        }
	}

	long addrglGenBuffers = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glGenBuffers");
	@Override
	public void glGenBuffers(int n, IntBuffer buffers) {
		System.out.println("Pointger is "+addrglGenBuffers );
		JNI.callPV(addrglGenBuffers, n, memAddress(buffers));
		System.out.println("Called ?");
		
	}

	@Override
	public void glGenerateMipmap(int target) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public int glGenFramebuffer() {
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	@Override
	public void glGenFramebuffers(int n, IntBuffer framebuffers) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public int glGenRenderbuffer() {
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	@Override
	public void glGenRenderbuffers(int n, IntBuffer renderbuffers) {
		throw new RuntimeException("Unimplemented method");

	}
	
	
	 public String glGetActiveAttrib(@NativeType("GLuint") int program, @NativeType("GLuint") int index, @NativeType("GLsizei") int maxLength, @NativeType("GLint *") IntBuffer size, @NativeType("GLenum *") IntBuffer type) {
	        if (CHECKS) {
	            check(size, 1);
	            check(type, 1);
	        }
	        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
	        try {
	            IntBuffer length = stack.ints(0);
	            ByteBuffer name = stack.malloc(maxLength);
	            
	            
	            JNI.callPPPPV(addrglGetActiveAttrib, program, index, maxLength, memAddress(length), memAddress(size), memAddress(type), memAddress(name));
	            
	            return memASCII(name, length.get(0));
	        } finally {
	            stack.setPointer(stackPointer);
	        }
	    }

	long addrglGetActiveAttrib = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glGetActiveAttrib");
	@Override
	public String glGetActiveAttrib(int program, int index, IntBuffer size, Buffer type) {
		IntBuffer typeTmp = BufferUtils.createIntBuffer(2);
		
		String name = glGetActiveAttrib(program, index, 256, size, typeTmp);
		
		size.put(typeTmp.get(0));
		if (type instanceof IntBuffer) ((IntBuffer)type).put(typeTmp.get(1));
		return name;
		
		
		
	}

	long glGetActiveUniform = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glGetActiveUniform");
	public String glGetActiveUniform(@NativeType("GLuint") int program, @NativeType("GLuint") int index, @NativeType("GLsizei") int maxLength, @NativeType("GLint *") IntBuffer size, @NativeType("GLenum *") IntBuffer type) {
        if (CHECKS) {
            check(size, 1);
            check(type, 1);
        }
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            IntBuffer length = stack.ints(0);
            ByteBuffer name = stack.malloc(maxLength);
            
            JNI.callPPPPV(glGetActiveUniform, program, index, maxLength, memAddress(length), memAddress(size), memAddress(type), memAddress(name));
            
            return memASCII(name, length.get(0));
        } finally {
            stack.setPointer(stackPointer);
        }
    }

	@Override
	public String glGetActiveUniform(int program, int index, IntBuffer size, Buffer type) {
		IntBuffer typeTmp = BufferUtils.createIntBuffer(2);
		String name = glGetActiveUniform(program, index, 256, size, typeTmp);
		size.put(typeTmp.get(0));
		if (type instanceof IntBuffer) ((IntBuffer)type).put(typeTmp.get(1));
		return name;

	}

	@Override
	public void glGetAttachedShaders(int program, int maxcount, Buffer count, IntBuffer shaders) {
		throw new RuntimeException("Unimplemented method");

	}

	
	long addrglGetAttribLocation = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glGetAttribLocation");
	@Override
	public int glGetAttribLocation(int program, String name) {
		 MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
	        try {
	            long stringsAddress = org.lwjgl.system.APIUtil.apiArrayi(stack, MemoryUtil::memUTF8, name);
	            
	            // TODO capire
	            int res =  JNI.callPI(addrglGetAttribLocation, program, stringsAddress);

	            org.lwjgl.system.APIUtil.apiArrayFree(stringsAddress, 1);
	            return res;
	        } finally {
	            stack.setPointer(stackPointer);
	        }
	}

	@Override
	public void glGetBooleanv(int pname, Buffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetBufferParameteriv(int target, int pname, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetFloatv(int pname, FloatBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetFramebufferAttachmentParameteriv(int target, int attachment, int pname, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	
	long addrglGetProgramiv = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glGetProgramiv");
	@Override
	public void glGetProgramiv(int program, int pname, IntBuffer params) {
		JNI.callPV(addrglGetProgramiv, program, pname, params==null ? 0 : memAddress(params));

	}

	@Override
	public String glGetProgramInfoLog(int program) {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public void glGetRenderbufferParameteriv(int target, int pname, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglGetShaderiv = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glGetShaderiv");
	@Override
	public void glGetShaderiv(int shader, int pname, IntBuffer params) {
		JNI.callPV(addrglGetShaderiv, shader, pname, memAddress(params));

	}

	@Override
	public String glGetShaderInfoLog(int shader) {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public void glGetShaderPrecisionFormat(int shadertype, int precisiontype, IntBuffer range, IntBuffer precision) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetTexParameterfv(int target, int pname, FloatBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetTexParameteriv(int target, int pname, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetUniformfv(int program, int location, FloatBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetUniformiv(int program, int location, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglGetUniformLocation = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glGetUniformLocation");
	@Override
	public int glGetUniformLocation(int program, String name) {
		 MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
	        try {
	            long stringsAddress = org.lwjgl.system.APIUtil.apiArrayi(stack, MemoryUtil::memUTF8, name);
	            
	            // TODO capire
	            int res =  JNI.callPI(addrglGetUniformLocation, program, stringsAddress);

	            org.lwjgl.system.APIUtil.apiArrayFree(stringsAddress, 1);
	            return res;
	        } finally {
	            stack.setPointer(stackPointer);
	        }


	}

	@Override
	public void glGetVertexAttribfv(int index, int pname, FloatBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetVertexAttribiv(int index, int pname, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetVertexAttribPointerv(int index, int pname, Buffer pointer) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public boolean glIsBuffer(int buffer) {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public boolean glIsEnabled(int cap) {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public boolean glIsFramebuffer(int framebuffer) {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public boolean glIsProgram(int program) {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public boolean glIsRenderbuffer(int renderbuffer) {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public boolean glIsShader(int shader) {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public boolean glIsTexture(int texture) {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	long addrglLinkProgram = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glLinkProgram");
	@Override
	public void glLinkProgram(int program) {
		JNI.callV(addrglLinkProgram, program);

	}

	@Override
	public void glReleaseShaderCompiler() {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glRenderbufferStorage(int target, int internalformat, int width, int height) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glSampleCoverage(float value, boolean invert) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glShaderBinary(int n, IntBuffer shaders, int binaryformat, Buffer binary, int length) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglShaderSource = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glShaderSource");
	@Override
	public void glShaderSource(int shader, String string) {
		 MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
	        try {
	            long stringsAddress = org.lwjgl.system.APIUtil.apiArrayi(stack, MemoryUtil::memUTF8, string);
	            JNI.callPPV(addrglShaderSource, shader, 1, stringsAddress, stringsAddress - 4);
	            org.lwjgl.system.APIUtil.apiArrayFree(stringsAddress, 1);
	        } finally {
	            stack.setPointer(stackPointer);
	        }

	}

	@Override
	public void glStencilFuncSeparate(int face, int func, int ref, int mask) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glStencilMaskSeparate(int face, int mask) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glStencilOpSeparate(int face, int fail, int zfail, int zpass) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glTexParameterfv(int target, int pname, FloatBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglTexParameteri = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glTexParameteri");
	@Override
	public void glTexParameteri(int target, int pname, int param) {
		JNI.callV(addrglTexParameteri, target, pname, param);

	}

	@Override
	public void glTexParameteriv(int target, int pname, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform1f(int location, float x) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform1fv(int location, int count, FloatBuffer v) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform1fv(int location, int count, float[] v, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglUniform1i = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glUniform1i");
	@Override
	public void glUniform1i(int location, int x) {
		JNI.callV(addrglUniform1i, location, x);

	}

	@Override
	public void glUniform1iv(int location, int count, IntBuffer v) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform1iv(int location, int count, int[] v, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform2f(int location, float x, float y) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform2fv(int location, int count, FloatBuffer v) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform2fv(int location, int count, float[] v, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform2i(int location, int x, int y) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform2iv(int location, int count, IntBuffer v) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform2iv(int location, int count, int[] v, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform3f(int location, float x, float y, float z) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform3fv(int location, int count, FloatBuffer v) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform3fv(int location, int count, float[] v, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform3i(int location, int x, int y, int z) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform3iv(int location, int count, IntBuffer v) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform3iv(int location, int count, int[] v, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform4f(int location, float x, float y, float z, float w) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform4fv(int location, int count, FloatBuffer v) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform4fv(int location, int count, float[] v, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform4i(int location, int x, int y, int z, int w) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform4iv(int location, int count, IntBuffer v) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform4iv(int location, int count, int[] v, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniformMatrix2fv(int location, int count, boolean transpose, FloatBuffer value) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniformMatrix2fv(int location, int count, boolean transpose, float[] value, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniformMatrix3fv(int location, int count, boolean transpose, FloatBuffer value) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniformMatrix3fv(int location, int count, boolean transpose, float[] value, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniformMatrix4fv(int location, int count, boolean transpose, FloatBuffer value) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglUniformMatrix4fv = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glUniformMatrix4fv");
	 public void glUniformMatrix4fv(@NativeType("GLint") int location, @NativeType("GLboolean") boolean transpose, @NativeType("GLfloat const *") FloatBuffer value) {
		 JNI.callPV(addrglUniformMatrix4fv, location, value.remaining() >> 4, transpose, memAddress(value));
	 }

	
	@Override
	public void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset) {
		glUniformMatrix4fv(location, transpose, toFloatBuffer(value, offset, count << 4));

	}

	long addrglUseProgram = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glUseProgram");
	@Override
	public void glUseProgram(int program) {
		JNI.callP(addrglUseProgram, program);

	}

	@Override
	public void glValidateProgram(int program) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glVertexAttrib1f(int indx, float x) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glVertexAttrib1fv(int indx, FloatBuffer values) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glVertexAttrib2f(int indx, float x, float y) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glVertexAttrib2fv(int indx, FloatBuffer values) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glVertexAttrib3f(int indx, float x, float y, float z) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glVertexAttrib3fv(int indx, FloatBuffer values) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glVertexAttrib4f(int indx, float x, float y, float z, float w) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glVertexAttrib4fv(int indx, FloatBuffer values) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, Buffer ptr) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, int ptr) {
		throw new RuntimeException("Unimplemented method");

	}

}
