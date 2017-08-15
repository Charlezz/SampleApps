//
// Created by Charles on 01/06/2017.
//

#ifndef ANDROID_SHADERCODE_H
#define ANDROID_SHADERCODE_H

static const char VERTEX_SHADER_OES[] = ""
        "uniform mat4 mvpMatrix;\n"
        "attribute vec4 vertexPosition;\n"
        "attribute vec2 vertexTexCoord;\n"
        "varying vec2 textureCoord;\n"
        "void main() {\n"
        "  gl_Position = mvpMatrix * vertexPosition;\n"
        "  textureCoord = vertexTexCoord;\n"
        "}\n";

static const char FRAGMENT_SHADER_OES[] = ""
        "#extension GL_OES_EGL_image_external : require\n"
        "precision mediump float;\n"
        "varying vec2 textureCoord;\n"
        "uniform samplerExternalOES mTexture;\n"
        "void main() {\n"
        "  gl_FragColor = texture2D(mTexture, textureCoord);\n"
        "}\n";


static const char VERTEX_SHADER_QUAD[] = ""
        "uniform mat4 mvpMatrix;"
        "attribute vec4 vertexPosition;"
        "attribute vec2 vertexTexCoord;"
        "varying vec2 textureCoord;"
        "void main() {"
        "  gl_Position = mvpMatrix * vertexPosition;"
        "  textureCoord = vertexTexCoord;"
        "}";

static const char FRAGMENT_SHADER_QUAD[] = ""
        "precision mediump float;"
        "varying vec2 textureCoord;"
        "uniform sampler2D mTexture;"
        "void main() {"
        "  gl_FragColor = texture2D( mTexture, textureCoord );"
        "}";

#endif //ANDROID_SHADERCODE_H


