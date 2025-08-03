#version 400 core

in vec2 textureCoordinates;

out vec4 fragColor;

uniform sampler2D textAtlas;
uniform vec3 color;
uniform int addTransparentBackground;

void main() {
    fragColor = texture(textAtlas, textureCoordinates);
    if (fragColor.a == 0.0 || fragColor.a != 1.0 && addTransparentBackground == 0) discard;
    if (fragColor.a == 1.0) fragColor *= vec4(color, 1.0);
}