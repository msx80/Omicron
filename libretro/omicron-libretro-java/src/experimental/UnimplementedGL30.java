package experimental;

import static org.lwjgl.system.MemoryStack.stackGet;
import static org.lwjgl.system.MemoryUtil.memAddress;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

import org.lwjgl.opengl.GL;
import org.lwjgl.system.APIUtil;
import org.lwjgl.system.JNI;
import org.lwjgl.system.MemoryStack;

import com.badlogic.gdx.graphics.GL30;

@Deprecated
public class UnimplementedGL30 implements GL30 {

	@Override
	public void glActiveTexture(int texture) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glBindTexture(int target, int texture) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glBlendFunc(int sfactor, int dfactor) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glClear(int mask) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glClearColor(float red, float green, float blue, float alpha) {
		throw new RuntimeException("Unimplemented method");

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

	@Override
	public void glDepthMask(boolean flag) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDepthRangef(float zNear, float zFar) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDisable(int cap) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDrawArrays(int mode, int first, int count) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDrawElements(int mode, int count, int type, Buffer indices) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glEnable(int cap) {
		throw new RuntimeException("Unimplemented method");

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

	@Override
	public int glGenTexture() {
		throw new RuntimeException("Unimplemented method");
		//return 0;
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

	@Override
	public void glPixelStorei(int pname, int param) {
		throw new RuntimeException("Unimplemented method");

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

	@Override
	public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format,
			int type, Buffer pixels) {
		throw new RuntimeException("Unimplemented method");

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

	@Override
	public void glAttachShader(int program, int shader) {
		throw new RuntimeException("Unimplemented method");

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

	@Override
	public void glBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
		throw new RuntimeException("Unimplemented method");

	}

	
	long addrglBufferData = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glBufferData");
	@Override
	public void glBufferData(int target, int size, Buffer data, int usage) {
		JNI.callPV(addrglBufferData, target, size, memAddress(data), usage);

	}

	@Override
	public void glBufferSubData(int target, int offset, int size, Buffer data) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public int glCheckFramebufferStatus(int target) {
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	@Override
	public void glCompileShader(int shader) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public int glCreateProgram() {
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	@Override
	public int glCreateShader(int type) {
		throw new RuntimeException("Unimplemented method");
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

	@Override
	public void glDrawElements(int mode, int count, int type, int indices) {
		throw new RuntimeException("Unimplemented method");

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
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	@Override
	public void glGenBuffers(int n, IntBuffer buffers) {
		throw new RuntimeException("Unimplemented method");

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

	@Override
	public String glGetActiveAttrib(int program, int index, IntBuffer size, Buffer type) {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public String glGetActiveUniform(int program, int index, IntBuffer size, Buffer type) {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public void glGetAttachedShaders(int program, int maxcount, Buffer count, IntBuffer shaders) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public int glGetAttribLocation(int program, String name) {
		throw new RuntimeException("Unimplemented method");
		//return 0;
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

	@Override
	public void glGetProgramiv(int program, int pname, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

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

	@Override
	public void glGetShaderiv(int shader, int pname, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

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

	@Override
	public int glGetUniformLocation(int program, String name) {
		throw new RuntimeException("Unimplemented method");
		//return 0;
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

	@Override
	public void glLinkProgram(int program) {
		throw new RuntimeException("Unimplemented method");

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

	@Override
	public void glShaderSource(int shader, String string) {
		throw new RuntimeException("Unimplemented method");

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

	@Override
	public void glTexParameteri(int target, int pname, int param) {
		throw new RuntimeException("Unimplemented method");

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

	@Override
	public void glUniform1i(int location, int x) {
		throw new RuntimeException("Unimplemented method");

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

	@Override
	public void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUseProgram(int program) {
		throw new RuntimeException("Unimplemented method");

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
	public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, int ptr) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glReadBuffer(int mode) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDrawRangeElements(int mode, int start, int end, int count, int type, Buffer indices) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDrawRangeElements(int mode, int start, int end, int count, int type, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border,
			int format, int type, Buffer pixels) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border,
			int format, int type, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height,
			int depth, int format, int type, Buffer pixels) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height,
			int depth, int format, int type, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glCopyTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int x, int y,
			int width, int height) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGenQueries(int n, int[] ids, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGenQueries(int n, IntBuffer ids) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteQueries(int n, int[] ids, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteQueries(int n, IntBuffer ids) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public boolean glIsQuery(int id) {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public void glBeginQuery(int target, int id) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glEndQuery(int target) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetQueryiv(int target, int pname, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetQueryObjectuiv(int id, int pname, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public boolean glUnmapBuffer(int target) {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public Buffer glGetBufferPointerv(int target, int pname) {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public void glDrawBuffers(int n, IntBuffer bufs) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniformMatrix2x3fv(int location, int count, boolean transpose, FloatBuffer value) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniformMatrix3x2fv(int location, int count, boolean transpose, FloatBuffer value) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniformMatrix2x4fv(int location, int count, boolean transpose, FloatBuffer value) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniformMatrix4x2fv(int location, int count, boolean transpose, FloatBuffer value) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniformMatrix3x4fv(int location, int count, boolean transpose, FloatBuffer value) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniformMatrix4x3fv(int location, int count, boolean transpose, FloatBuffer value) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1,
			int dstY1, int mask, int filter) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glRenderbufferStorageMultisample(int target, int samples, int internalformat, int width, int height) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glFramebufferTextureLayer(int target, int attachment, int texture, int level, int layer) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glFlushMappedBufferRange(int target, int offset, int length) {
		throw new RuntimeException("Unimplemented method");

	}

	long addrglBindVertexArray = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glBindVertexArray");
	@Override
	public void glBindVertexArray(int array) {
		JNI.callV(addrglBindVertexArray, array);

	}

	@Override
	public void glDeleteVertexArrays(int n, int[] arrays, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteVertexArrays(int n, IntBuffer arrays) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGenVertexArrays(int n, int[] arrays, int offset) {
		for(int i = offset; i < offset + n; i++) {
			arrays[i] = glGenVertexArrays();
		}
		

	}

	private int glGenVertexArrays() {
		MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            IntBuffer arrays = stack.callocInt(1);
            glGenVertexArrays(1, arrays);
            //nglGenVertexArrays(1, memAddress(arrays));
            return arrays.get(0);
        } finally {
            stack.setPointer(stackPointer);
        }
	}

	long addrglGenVertexArrays = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glGenVertexArrays");
	@Override
	public void glGenVertexArrays(int n, IntBuffer arrays) {
		JNI.callPV(addrglGenVertexArrays, 1, memAddress(arrays));

	}

	@Override
	public boolean glIsVertexArray(int array) {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public void glBeginTransformFeedback(int primitiveMode) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glEndTransformFeedback() {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glBindBufferRange(int target, int index, int buffer, int offset, int size) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glBindBufferBase(int target, int index, int buffer) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glTransformFeedbackVaryings(int program, String[] varyings, int bufferMode) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glVertexAttribIPointer(int index, int size, int type, int stride, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetVertexAttribIiv(int index, int pname, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetVertexAttribIuiv(int index, int pname, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glVertexAttribI4i(int index, int x, int y, int z, int w) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glVertexAttribI4ui(int index, int x, int y, int z, int w) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetUniformuiv(int program, int location, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public int glGetFragDataLocation(int program, String name) {
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	@Override
	public void glUniform1uiv(int location, int count, IntBuffer value) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform3uiv(int location, int count, IntBuffer value) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glUniform4uiv(int location, int count, IntBuffer value) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glClearBufferiv(int buffer, int drawbuffer, IntBuffer value) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glClearBufferuiv(int buffer, int drawbuffer, IntBuffer value) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glClearBufferfv(int buffer, int drawbuffer, FloatBuffer value) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glClearBufferfi(int buffer, int drawbuffer, float depth, int stencil) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public String glGetStringi(int name, int index) {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public void glCopyBufferSubData(int readTarget, int writeTarget, int readOffset, int writeOffset, int size) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetUniformIndices(int program, String[] uniformNames, IntBuffer uniformIndices) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetActiveUniformsiv(int program, int uniformCount, IntBuffer uniformIndices, int pname,
			IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public int glGetUniformBlockIndex(int program, String uniformBlockName) {
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	@Override
	public void glGetActiveUniformBlockiv(int program, int uniformBlockIndex, int pname, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetActiveUniformBlockName(int program, int uniformBlockIndex, Buffer length,
			Buffer uniformBlockName) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public String glGetActiveUniformBlockName(int program, int uniformBlockIndex) {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public void glUniformBlockBinding(int program, int uniformBlockIndex, int uniformBlockBinding) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDrawArraysInstanced(int mode, int first, int count, int instanceCount) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDrawElementsInstanced(int mode, int count, int type, int indicesOffset, int instanceCount) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetInteger64v(int pname, LongBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetBufferParameteri64v(int target, int pname, LongBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGenSamplers(int count, int[] samplers, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGenSamplers(int count, IntBuffer samplers) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteSamplers(int count, int[] samplers, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteSamplers(int count, IntBuffer samplers) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public boolean glIsSampler(int sampler) {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public void glBindSampler(int unit, int sampler) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glSamplerParameteri(int sampler, int pname, int param) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glSamplerParameteriv(int sampler, int pname, IntBuffer param) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glSamplerParameterf(int sampler, int pname, float param) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glSamplerParameterfv(int sampler, int pname, FloatBuffer param) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetSamplerParameteriv(int sampler, int pname, IntBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGetSamplerParameterfv(int sampler, int pname, FloatBuffer params) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glVertexAttribDivisor(int index, int divisor) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glBindTransformFeedback(int target, int id) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteTransformFeedbacks(int n, int[] ids, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glDeleteTransformFeedbacks(int n, IntBuffer ids) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGenTransformFeedbacks(int n, int[] ids, int offset) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glGenTransformFeedbacks(int n, IntBuffer ids) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public boolean glIsTransformFeedback(int id) {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public void glPauseTransformFeedback() {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glResumeTransformFeedback() {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glProgramParameteri(int program, int pname, int value) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glInvalidateFramebuffer(int target, int numAttachments, IntBuffer attachments) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glInvalidateSubFramebuffer(int target, int numAttachments, IntBuffer attachments, int x, int y,
			int width, int height) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, Buffer ptr) {
		throw new RuntimeException("Unimplemented method");

	}

}
