#version 400 core

uniform vec3 position;
uniform mat4 projectionViewMatrix;
uniform mat4[8] transformations;

layout (location = 0) in vec3 positionOffset;
layout (location = 1) in vec2 textureCoordinate;
layout (location = 2) in int transformIndex;

out vec2 fragTextureCoordinate;

void main() {
    gl_Position = projectionViewMatrix * transformations[transformIndex] * vec4(position + positionOffset, 1);
    fragTextureCoordinate = textureCoordinate;
}