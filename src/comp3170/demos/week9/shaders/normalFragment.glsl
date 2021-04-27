#version 410

layout(location = 0) out vec4 o_colour;

in vec3 v_normal;

void main() {
	vec3 n = v_normal;
	n = normalize(n);
    o_colour = vec4(n,1);
}

