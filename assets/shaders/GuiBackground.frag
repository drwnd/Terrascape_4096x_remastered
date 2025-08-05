#version 400 core

uniform sampler2D image;
uniform int rimWidth;
uniform ivec2 screenSize;
uniform vec2 size;

in vec2 fragTextureCoordinate;

out vec4 fragColor;

float getTextureCoord(float fragTextureCoord, float size, int sceenSize) {
    fragTextureCoord = min(fragTextureCoord, 1.0 - fragTextureCoord) * size;

    float thickness = float(rimWidth) / sceenSize;

    if (fragTextureCoord >= thickness) return 0.5;
    return fragTextureCoord * (0.5 / thickness);
}

void main() {
    vec2 textureCoord = vec2(
    getTextureCoord(fragTextureCoordinate.x, size.x, screenSize.x),
    getTextureCoord(fragTextureCoordinate.y, size.y, screenSize.y)
    );

    vec4 color = texture(image, textureCoord);
    if (color.a == 0.0) discard;
    fragColor = color;
}