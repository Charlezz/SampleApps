//
// Created by Charles on 05/06/2017.
//


#include <jni.h>
#include <android/log.h>
#include <GLES2/gl2.h>
#include <math.h>
#include <stdlib.h>
#include "SampleShaderCode.h"
#include <GLES2/gl2ext.h>

#define I(_i, _j) ((_j)+ 4*(_i))
#define LOG_TAG    "SampleRenderer"
#define LOG(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" {


int mVideoWidth, mVideoHeight;
int mSurfaceWidth, mSurfaceHeight;

float mModelMatrix[16];
float mViewMatrix[16];
float mProjectionMatrix[16];
float mMVPMatrix[16];

int mDestTextureId;
int mShaderId = 0;
int mVertexPositionHandle = 0;
int mTextureCoordHandle = 0;
int mMVPMatrixHandle = 0;
int mTextureHandle = 0;

GLuint mVertexBuffer = 0;
GLuint mUVBuffer = 0;

float quadVertices[] = {-0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, -0.5f, 0.5f, 0.0f, -0.5f, 0.5f, 0.0f, 0.5f, -0.5f, 0.0f, 0.5f, 0.5f, 0.0f};

float uvCoords[] = {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};


GLuint mediaPlayerTextureId;
float mModelMatrix_FBO[16];
float mViewMatrix_FBO[16];
float mProjectionMatrix_FBO[16];
float mMVPMatrix_FBO[16];

int mShaderId_OES = -1;
int mVertexPositionHandleOES = 0;
int mTextureCoordHandleOES = 0;
int mMVPMatrixHandleOES = 0;
int mTextureHandleOES = 0;

GLuint mVertexBufferOES = 0;
GLuint mUVBufferOES = 0;
GLuint mFrameBuffer = 0;

void setIdentityM(float *sm, int smOffset) {
    for (int i = 0; i < 16; i++) {
        sm[smOffset + i] = 0;
    }
    for (int i = 0; i < 16; i += 5) {
        sm[smOffset + i] = 1.0f;
    }
}

void multiplyMM(float *r, const float *lhs, const float *rhs) {
    for (int i = 0; i < 4; i++) {
        register const float rhs_i0 = rhs[I(i, 0)];
        register float ri0 = lhs[I(0, 0)] * rhs_i0;
        register float ri1 = lhs[I(0, 1)] * rhs_i0;
        register float ri2 = lhs[I(0, 2)] * rhs_i0;
        register float ri3 = lhs[I(0, 3)] * rhs_i0;
        for (int j = 1; j < 4; j++) {
            register const float rhs_ij = rhs[I(i, j)];
            ri0 += lhs[I(j, 0)] * rhs_ij;
            ri1 += lhs[I(j, 1)] * rhs_ij;
            ri2 += lhs[I(j, 2)] * rhs_ij;
            ri3 += lhs[I(j, 3)] * rhs_ij;
        }
        r[I(i, 0)] = ri0;
        r[I(i, 1)] = ri1;
        r[I(i, 2)] = ri2;
        r[I(i, 3)] = ri3;
    }
}


void translateM(float *m, int mOffset, float x, float y, float z) {
    for (int i = 0; i < 4; i++) {
        int mi = mOffset + i;
        m[12 + mi] += m[mi] * x + m[4 + mi] * y + m[8 + mi] * z;
    }
}

void orthoM(float *m, int mOffset, float left, float right, float bottom, float top, float near, float far) {
    if (left == right) {
        return;
    }
    if (bottom == top) {
        return;
    }
    if (near == far) {
        return;
    }

    float r_width = 1.0f / (right - left);
    float r_height = 1.0f / (top - bottom);
    float r_depth = 1.0f / (far - near);
    float x = 2.0f * (r_width);
    float y = 2.0f * (r_height);
    float z = -2.0f * (r_depth);
    float tx = -(right + left) * r_width;
    float ty = -(top + bottom) * r_height;
    float tz = -(far + near) * r_depth;
    m[mOffset + 0] = x;
    m[mOffset + 5] = y;
    m[mOffset + 10] = z;
    m[mOffset + 12] = tx;
    m[mOffset + 13] = ty;
    m[mOffset + 14] = tz;
    m[mOffset + 15] = 1.0f;
    m[mOffset + 1] = 0.0f;
    m[mOffset + 2] = 0.0f;
    m[mOffset + 3] = 0.0f;
    m[mOffset + 4] = 0.0f;
    m[mOffset + 6] = 0.0f;
    m[mOffset + 7] = 0.0f;
    m[mOffset + 8] = 0.0f;
    m[mOffset + 9] = 0.0f;
    m[mOffset + 11] = 0.0f;
}

void setLookAtM(float *rm, int rmOffset, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {

    float fx = centerX - eyeX;
    float fy = centerY - eyeY;
    float fz = centerZ - eyeZ;

    // Normalize f
    float rlf = 1.0f / sqrtf(fx * fx + fy * fy + fz * fz);
    fx *= rlf;
    fy *= rlf;
    fz *= rlf;

    // compute s = f x up (x means "cross product")
    float sx = fy * upZ - fz * upY;
    float sy = fz * upX - fx * upZ;
    float sz = fx * upY - fy * upX;

    // and normalize s
    float rls = 1.0f / sqrtf(sx * sx + sy * sy + sz * sz);
    sx *= rls;
    sy *= rls;
    sz *= rls;

    // compute u = s x f
    float ux = sy * fz - sz * fy;
    float uy = sz * fx - sx * fz;
    float uz = sx * fy - sy * fx;

    rm[rmOffset + 0] = sx;
    rm[rmOffset + 1] = ux;
    rm[rmOffset + 2] = -fx;
    rm[rmOffset + 3] = 0.0f;

    rm[rmOffset + 4] = sy;
    rm[rmOffset + 5] = uy;
    rm[rmOffset + 6] = -fy;
    rm[rmOffset + 7] = 0.0f;

    rm[rmOffset + 8] = sz;
    rm[rmOffset + 9] = uz;
    rm[rmOffset + 10] = -fz;
    rm[rmOffset + 11] = 0.0f;

    rm[rmOffset + 12] = 0.0f;
    rm[rmOffset + 13] = 0.0f;
    rm[rmOffset + 14] = 0.0f;
    rm[rmOffset + 15] = 1.0f;

    translateM(rm, rmOffset, -eyeX, -eyeY, -eyeZ);
}

void scaleM(float *m, int mOffset, float x, float y, float z) {
    for (int i = 0; i < 4; i++) {
        int mi = mOffset + i;
        m[mi] *= x;
        m[4 + mi] *= y;
        m[8 + mi] *= z;
    }
}
unsigned int initShader(unsigned int shaderType, const char *source) {

    GLuint shader = glCreateShader((GLenum) shaderType);
    if (shader) {
        glShaderSource(shader, 1, &source, NULL);
        glCompileShader(shader);
        GLint compiled = 0;
        glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);

        if (!compiled) {
            GLint infoLen = 0;
            glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen) {
                char *buf = (char *) malloc(infoLen);
                if (buf) {
                    glGetShaderInfoLog(shader, infoLen, NULL, buf);
                    free(buf);
                }
                glDeleteShader(shader);
                shader = 0;
            }
        }
    }
    return shader;
}
void checkGlError(const char *operation) {
    for (GLint error = glGetError(); error; error = glGetError()) {
        LOG("after %s() glError (0x%x)", operation, error);
    }

}
unsigned int createProgramFromBuffer(const char *vertexShaderBuffer, const char *fragmentShaderBuffer) {

    GLuint vertexShader = initShader(GL_VERTEX_SHADER, vertexShaderBuffer);
    if (!vertexShader)
        return 0;

    GLuint fragmentShader = initShader(GL_FRAGMENT_SHADER, fragmentShaderBuffer);
    if (!fragmentShader)
        return 0;

    GLuint program = glCreateProgram();
    if (program) {
        glAttachShader(program, vertexShader);
        checkGlError("glAttachShader");

        glAttachShader(program, fragmentShader);
        checkGlError("glAttachShader");

        glLinkProgram(program);
        GLint linkStatus = GL_FALSE;
        glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);

        if (linkStatus != GL_TRUE) {
            GLint bufLength = 0;
            glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);
            if (bufLength) {
                char *buf = (char *) malloc(bufLength);
                if (buf) {
                    glGetProgramInfoLog(program, bufLength, NULL, buf);
                    LOG("Could not link program: %s", buf);
                    free(buf);
                }
            }
            glDeleteProgram(program);
            program = 0;
        }
    }
    return program;
}


JNIEXPORT int JNICALL
Java_charlezz_com_videosampleapp_MainActivity_init(JNIEnv *, jobject obj, jint videoWidth, jint videoHeight) {

    mVideoWidth = videoWidth;
    mVideoHeight = videoHeight;

    //매트릭스 초기화
    setIdentityM(mModelMatrix, 0);
    setIdentityM(mViewMatrix, 0);
    setIdentityM(mProjectionMatrix, 0);
    setIdentityM(mMVPMatrix, 0);

    //뷰매트릭스 초기화
    float eyeX = 0.0f;
    float eyeY = 0.0f;
    float eyeZ = 1.0f;

    float lookX = 0.0f;
    float lookY = 0.0f;
    float lookZ = 0.0f;

    float upX = 0.0f;
    float upY = 1.0f;
    float upZ = 0.0f;

    setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

    mShaderId = createProgramFromBuffer(VERTEX_SHADER_QUAD, FRAGMENT_SHADER_QUAD);
    glUseProgram(mShaderId);

    mTextureHandle = glGetUniformLocation(mShaderId, "mTexture");
    mVertexPositionHandle = glGetAttribLocation(mShaderId, "vertexPosition");
    mTextureCoordHandle = glGetAttribLocation(mShaderId, "vertexTexCoord");
    mMVPMatrixHandle = glGetUniformLocation(mShaderId, "mvpMatrix");

    //사각형 그리는 부분, 최종적인 텍스쳐가 그려질 부분 영역 잡아주기
    GLuint buffers[1];
    glGenBuffers(1, buffers);
    mVertexBuffer = buffers[0];
    glBindBuffer(GL_ARRAY_BUFFER, mVertexBuffer);
    glBufferData(GL_ARRAY_BUFFER, 18 * 4, quadVertices, GL_STATIC_DRAW);

    //텍스쳐가 적용될 UV
    GLuint uvBuffers[1];
    glGenBuffers(1, uvBuffers);
    mUVBuffer = uvBuffers[0];
    glBindBuffer(GL_ARRAY_BUFFER, mUVBuffer);
    glBufferData(GL_ARRAY_BUFFER, 12 * 4, uvCoords, GL_STATIC_DRAW);

    //텍스쳐 생성
    GLuint textures[1];
    glGenTextures(1, textures);
    glActiveTexture(GL_TEXTURE0);
    mDestTextureId = textures[0];

    //텍스쳐 바인드
    glBindTexture(GL_TEXTURE_2D, mDestTextureId);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, videoWidth, videoHeight, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);


    return mDestTextureId;

}

JNIEXPORT int JNICALL
Java_charlezz_com_videosampleapp_MainActivity_initFBO(JNIEnv *, jobject obj) {
    //뷰매트릭스 초기화
    float eyeX = 0.0f;
    float eyeY = 0.0f;
    float eyeZ = 1.0f;

    float lookX = 0.0f;
    float lookY = 0.0f;
    float lookZ = 0.0f;

    float upX = 0.0f;
    float upY = 1.0f;
    float upZ = 0.0f;

    setIdentityM(mModelMatrix_FBO, 0);
    setIdentityM(mViewMatrix_FBO, 0);
    setIdentityM(mMVPMatrix_FBO, 0);
    setIdentityM(mProjectionMatrix_FBO, 0);

    setLookAtM(mViewMatrix_FBO, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

    mShaderId_OES = createProgramFromBuffer(VERTEX_SHADER_OES, FRAGMENT_SHADER_OES);
    __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "mShaderId_OES:%d", mShaderId_OES);

    glUseProgram(mShaderId_OES);

    mTextureHandleOES = glGetUniformLocation(mShaderId_OES, "mTexture");
    mVertexPositionHandleOES = glGetAttribLocation(mShaderId_OES, "vertexPosition");
    mTextureCoordHandleOES = glGetAttribLocation(mShaderId_OES, "vertexTexCoord");
    mMVPMatrixHandleOES = glGetUniformLocation(mShaderId_OES, "mvpMatrix");


    __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "mTextureHandleOES:%d", mTextureHandleOES);
    __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "mVertexPositionHandleOES:%d", mVertexPositionHandleOES);
    __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "mTextureCoordHandleOES:%d", mTextureCoordHandleOES);
    __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "mMVPMatrixHandleOES:%d", mMVPMatrixHandleOES);


    //사각형을 그리는부분 비디오텍스쳐를 이곳에 그림
    GLuint vertexBuffersFBO[1];
    glGenBuffers(1, vertexBuffersFBO);
    mVertexBufferOES = vertexBuffersFBO[0];
    glBindBuffer(GL_ARRAY_BUFFER, mVertexBufferOES);
    glBufferData(GL_ARRAY_BUFFER, 18 * 4, quadVertices, GL_STATIC_DRAW);
    checkGlError("vertexBuffersFBO");

    //텍스쳐가 적용될 UV 설정
    GLuint uvBuffersFBO[1];
    glGenBuffers(1, uvBuffersFBO);
    mUVBufferOES = uvBuffersFBO[0];
    glBindBuffer(GL_ARRAY_BUFFER, mUVBufferOES);
    glBufferData(GL_ARRAY_BUFFER, 12 * 4, uvCoords, GL_STATIC_DRAW);
    checkGlError("uvBuffersFBO");

    //비디오입힐 OES 텍스쳐 만들기
    GLuint mediaPlayerTextures[1];
    glGenTextures(1, mediaPlayerTextures);
    mediaPlayerTextureId = mediaPlayerTextures[0];
    checkGlError("mediaPlayerTextureId");
    __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "mediaPlayerTextureId:%d", mediaPlayerTextureId);

    glBindTexture(GL_TEXTURE_EXTERNAL_OES, mediaPlayerTextureId);
    glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    //프레임 버퍼 만들기
    GLuint frameBuffers[1];
    glGenFramebuffers(1, frameBuffers);
    mFrameBuffer = frameBuffers[0];
    glBindFramebuffer(GL_FRAMEBUFFER, mFrameBuffer);
    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, mDestTextureId, 0);

    //GL초기화
    glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
    glDisable(GL_CULL_FACE);
    glDisable(GL_DEPTH_TEST);
    return mediaPlayerTextureId;
}

JNIEXPORT void JNICALL
Java_charlezz_com_videosampleapp_MainActivity_initSurface(JNIEnv *env, jobject obj, jint surfaceWidth, jint surfaceHeight) {
    mSurfaceWidth = surfaceWidth;
    mSurfaceHeight = surfaceHeight;

    glViewport(0, 0, surfaceWidth, surfaceHeight);

    float left = -(float) surfaceWidth / (float) 2;
    float right = (float) surfaceWidth / (float) 2;
    float bottom = -(float) surfaceHeight / (float) 2;
    float top = (float) surfaceHeight / (float) 2;
    float near = 1;
    float far = 2000;

    orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);

    float leftFBO = -(float) mVideoWidth / (float) 2;
    float rightFBO = (float) mVideoWidth / (float) 2;
    float bottomFBO = -(float) mVideoHeight / (float) 2;
    float topFBO = (float) mVideoHeight / (float) 2;
    float nearFBO = 1;
    float farFBO = 2000;
    orthoM(mProjectionMatrix_FBO, 0, leftFBO, rightFBO, bottomFBO, topFBO, nearFBO, farFBO);
}

JNIEXPORT void JNICALL
Java_charlezz_com_videosampleapp_MainActivity_draw(JNIEnv *env, jobject obj) {

    //draw video texture to Frame Buffer

    glBindFramebuffer(GL_FRAMEBUFFER, mFrameBuffer);
    glViewport(0, 0, mVideoWidth, mVideoHeight);

    glClearColor(1, 0, 0, 1);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    glUseProgram(mShaderId_OES);

    setIdentityM(mModelMatrix_FBO, 0);
    scaleM(mModelMatrix_FBO, 0, mVideoWidth, mVideoHeight, 1);

    glBindTexture(GL_TEXTURE_2D, mDestTextureId);
    glViewport(0, 0, mVideoWidth, mVideoHeight);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, mVideoWidth, mVideoHeight, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    multiplyMM(mMVPMatrix_FBO, mViewMatrix_FBO, mModelMatrix_FBO);
    multiplyMM(mMVPMatrix_FBO, mProjectionMatrix_FBO, mMVPMatrix_FBO);

    glUniformMatrix4fv(mMVPMatrixHandleOES, 1, false, mMVPMatrix_FBO);
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_EXTERNAL_OES, mediaPlayerTextureId);

    glUniform1i(mTextureHandleOES, 0);

    glEnableVertexAttribArray(mVertexPositionHandleOES);
    glBindBuffer(GL_ARRAY_BUFFER, mVertexBufferOES);
    glVertexAttribPointer(mVertexPositionHandleOES, 3, GL_FLOAT, GL_FALSE, 0, (void *) 0);

    glEnableVertexAttribArray(mTextureCoordHandleOES);
    glBindBuffer(GL_ARRAY_BUFFER, mUVBufferOES);
    glVertexAttribPointer(mTextureCoordHandleOES, 2, GL_FLOAT, GL_FALSE, 0, (void *) 0);

    glDrawArrays(GL_TRIANGLES, 0, 6);

    glDisableVertexAttribArray(mVertexPositionHandleOES);
    glDisableVertexAttribArray(mTextureCoordHandleOES);



    //draw texture will be rendered to GlSurface

    glBindFramebuffer(GL_FRAMEBUFFER, 0);
    glViewport(0, 0, mSurfaceWidth, mSurfaceHeight);
    glClearColor(0, 1, 0, 1);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glUseProgram(mShaderId);

    setIdentityM(mModelMatrix, 0);
    scaleM(mModelMatrix, 0, mSurfaceWidth, -mSurfaceHeight, 1);
    multiplyMM(mMVPMatrix, mViewMatrix, mModelMatrix);
    multiplyMM(mMVPMatrix, mProjectionMatrix, mMVPMatrix);

    glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix);
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, mDestTextureId);

    glUniform1i(mTextureHandle, 0);

    glEnableVertexAttribArray(mVertexPositionHandle);
    glBindBuffer(GL_ARRAY_BUFFER, mVertexBuffer);
    glVertexAttribPointer(mVertexPositionHandle, 3, GL_FLOAT, GL_FALSE, 0, (void *) 0);

    glEnableVertexAttribArray(mTextureCoordHandle);
    glBindBuffer(GL_ARRAY_BUFFER, mUVBuffer);
    glVertexAttribPointer(mTextureCoordHandle, 2, GL_FLOAT, GL_FALSE, 0, (void *) 0);

    glDrawArrays(GL_TRIANGLES, 0, 6);

    glDisableVertexAttribArray(mVertexPositionHandle);
    glDisableVertexAttribArray(mTextureCoordHandle);

    checkGlError("finish");
    glFinish();


}
}