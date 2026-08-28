#version 400 core

uniform vec3 position;
uniform mat4 projectionViewMatrix;

layout (location = 0) in vec3 positionOffset;
layout (location = 1) in vec2 textureCoordinate;

out vec2 fragTextureCoordinate;

void main() {
    gl_Position = projectionViewMatrix * vec4(position + positionOffset, 1);
    fragTextureCoordinate = textureCoordinate;
}