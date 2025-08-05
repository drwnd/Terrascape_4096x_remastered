#version 400 core

in int data;

out vec2 textureCoordinates;

uniform ivec2 screenSize;
uniform ivec2 charSize;
uniform int[128] string;
uniform vec2 position;

void main() {
    int deltaX = data >> 7 & 1;
    int deltaY = data >> 8 & 1;
    vec2 scale = 2 * vec2(charSize) / screenSize;

    float x = 2 * position.x + ((data & 127) + deltaX) * scale.x;
    float y = 2 * position.y + deltaY * scale.y;
    gl_Position = vec4(x, y, 0.5, 1);

    int character = string[data & 127];
    float u = ((character & 15) + deltaX) * 0.0625;
    float v = ((character >> 4 & 15) + 1 - deltaY) * 0.0625;
    textureCoordinates = vec2(u, v);
}