#version 400 core

uniform sampler2D textureAtlas;

in vec2 fragTextureCoordinate;

out vec4 fragColor;

const float RIM_THICKNESS = 0.03;

float getTextureCoord(float fragTextureCoord) {
    fragTextureCoord = min(fragTextureCoord, 1.0 - fragTextureCoord);
    if (fragTextureCoord >= RIM_THICKNESS) return 0.5;
    return fragTextureCoord * (0.5 / RIM_THICKNESS);
}

void main() {
    vec2 textureCoord = vec2(getTextureCoord(fragTextureCoordinate.x), getTextureCoord(fragTextureCoordinate.y));

    vec4 color = texture(textureAtlas, textureCoord);
    if (color.a == 0.0) discard;
    fragColor = color;
}