#version 400 core

flat in int textureData;
in vec3 totalPosition;

out vec4 fragColor;

uniform sampler2D textureAtlas;

vec2 getUVOffset(int side) {
    switch (side) {
        case 0: return vec2(fract(totalPosition.x * 0.0625), 1 - fract(totalPosition.y * 0.0625)) * 0.0625;
        case 1: return fract(totalPosition.xz * 0.0625) * 0.0625;
        case 2: return 0.0625 - fract(totalPosition.zy * 0.0625) * 0.0625;
        case 3: return 0.0625 - fract(totalPosition.xy * 0.0625) * 0.0625;
        case 4: return fract(totalPosition.zx * 0.0625) * 0.0625;
        case 5: return vec2(fract(totalPosition.z * 0.0625), 1 - fract(totalPosition.y * 0.0625)) * 0.0625;
    }

    return fract(totalPosition.zx);
}

void main() {
    float u = (textureData & 15) * 0.0625;
    float v = (textureData >> 4 & 15) * 0.0625;
    vec4 color = texture(textureAtlas, vec2(u, v) + getUVOffset(textureData >> 8 & 7));
    if (color.a == 0.0f) {
        discard;
    }
    fragColor = color;
}
