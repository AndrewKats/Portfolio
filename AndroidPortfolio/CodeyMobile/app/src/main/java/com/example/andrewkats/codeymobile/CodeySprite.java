package com.example.andrewkats.codeymobile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by AndrewKats on 12/18/2016.
 */
public class CodeySprite
{
    static final private int POSITION_ARRAY = 0;
    static final private int TEXTURE_COORDINATE_ARRAY = 1;

    static final private float[] _quadGeometry = {
            -0.5f, -0.5f,
            -0.5f, 0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,
    };

    static final private float[] _quadTextureCoordinates = {
            0.0f, 1.0f, // Bottom-left
            0.0f, 0.0f, // Top-left
            1.0f, 1.0f, // Bottom-right
            1.0f, 0.0f, // Top-right
    };



    private int _program = -1;
    static private int _translateLocation = -1;
    static private int _scaleLocation = -1;
    static private int _textureUnitLocation = -1;

    float _translateX = 0.0f;
    float _translateY = 0.0f;
    float _scaleX = 1.0f;
    float _scaleY = 1.0f;

    public Bitmap _texture = null;
    private int _textureName = -1;
    private boolean _changeIt = true;

    public void setTexture(Bitmap texture)
    {
        _texture = texture;
        _changeIt = true;
    }


    private void setup()
    {
        String vertexShaderSource = "" +
                "\n" +
                "uniform vec2 translate; \n" +
                "uniform vec2 scale; \n" +
                "attribute vec2 position; \n" +
                "attribute vec2 textureCoordinate; \n" +
                "varying vec2 textureCoordinateInterpolated; \n" +
                "\n" +
                "void main() { \n" +
                "   gl_Position = vec4(position.x * scale.x + translate.x, position.y * scale.y + translate.y, 0.0, 1.0); \n" +
                "   textureCoordinateInterpolated = textureCoordinate; \n" +
                "} \n" +
                "\n" +
                "\n";

        String fragmentShaderSource = "" +
                "\n" +
                "varying vec2 textureCoordinateInterpolated; \n" +
                "uniform sampler2D textureUnit; \n" +
                "\n" +
                "void main() { \n" +
                "   gl_FragColor = texture2D(textureUnit, textureCoordinateInterpolated); \n" +
                "} \n" +
                "\n" +
                "\n";


        // Create and compile vertex shader
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, vertexShaderSource);
        GLES20.glCompileShader(vertexShader);
        //String vertexShaderInfoLog = GLES20.glGetShaderInfoLog(vertexShader);
        //Log.i("Vertex Shader", "Output: " + vertexShaderInfoLog);

        // Create and compile fragment shader
        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fragmentShaderSource);
        GLES20.glCompileShader(fragmentShader);
       // String fragmentShaderInfoLog = GLES20.glGetShaderInfoLog(fragmentShader);
       // Log.i("Fragment Shader", "Output: " + fragmentShaderInfoLog);

        // Link program
        _program = GLES20.glCreateProgram();
        GLES20.glAttachShader(_program, vertexShader);
        GLES20.glAttachShader(_program, fragmentShader);
        GLES20.glBindAttribLocation(_program, POSITION_ARRAY, "position");
        GLES20.glBindAttribLocation(_program, TEXTURE_COORDINATE_ARRAY, "textureCoordinate");
        GLES20.glLinkProgram(_program);
        String programInfoLog = GLES20.glGetProgramInfoLog(_program);
        Log.i("Linker", "Output: " + programInfoLog);

        GLES20.glUseProgram(_program);
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glEnableVertexAttribArray(1);

        _translateLocation = GLES20.glGetUniformLocation(_program, "translate");
        _scaleLocation = GLES20.glGetUniformLocation(_program, "scale");
        _textureUnitLocation = GLES20.glGetUniformLocation(_program, "textureUnit");

        // and more
        ByteBuffer quadGeometryByteBuffer = ByteBuffer.allocateDirect(4 * _quadGeometry.length);
        quadGeometryByteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer quadGeometryBuffer = quadGeometryByteBuffer.asFloatBuffer();
        quadGeometryBuffer.put(_quadGeometry);
        quadGeometryBuffer.rewind();
        GLES20.glVertexAttribPointer(POSITION_ARRAY, 2, GLES20.GL_FLOAT, false, 0, quadGeometryBuffer);


        ByteBuffer quadTextureCoordinateByteBuffer = ByteBuffer.allocateDirect(4 * _quadTextureCoordinates.length);
        quadTextureCoordinateByteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer quadTextureCoordinateBuffer = quadTextureCoordinateByteBuffer.asFloatBuffer();
        quadTextureCoordinateBuffer.put(_quadTextureCoordinates);
        quadTextureCoordinateBuffer.rewind();
        GLES20.glVertexAttribPointer(TEXTURE_COORDINATE_ARRAY, 2, GLES20.GL_FLOAT, false, 0, quadTextureCoordinateBuffer);

        // set the texture once
        GLES20.glUniform1i(_textureUnitLocation, 0);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);

        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);
    }

    public void draw()
    {
        if (_program <= 0)
        {
            setup();
        }
        if(_textureName <= 0 || _changeIt)
        {
            // Generate and single texture name identifier
            int[] textureNames = new int[1];
            textureNames[0] = -1;
            GLES20.glGenTextures(1, textureNames, 0);
            _textureName = textureNames[0];

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, _textureName);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, _texture, 0);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

            _changeIt = false;
        }
        else
        {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, _textureName);

            _changeIt = false;
        }

        /*
        _translateX += 0.005f;
        _translateY -= 0.002f;
        _scaleX += 0.005f;
        _scaleY -= 0.002f;
        */

        GLES20.glUniform2f(_translateLocation, _translateX, _translateY);
        GLES20.glUniform2f(_scaleLocation, _scaleX, _scaleY);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

}
