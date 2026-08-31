#version 400 core

uniform vec3 position;
uniform mat4 projectionViewMatrix;
uniform mat4[8] transformations;

layout (location = 0) in vec3 positionOffset;
layout (location = 1) in vec2 textureCoordinate;
layout (location = 2) in int transformIndex;

out vec3 voxelPosition;
out vec2 fragTextureCoordinate;

void main() {
    vec3 transformedPosition = (transformations[transformIndex] * vec4(positionOffset, 1)).xyz;
    gl_Position = projectionViewMatrix * vec4(transformedPosition + position, 1);
    fragTextureCoordinate = textureCoordinate;
    voxelPosition = transformedPosition + position;
}