#version 410

uniform vec4 u_colour;
uniform float u_width;

in vec3 v_barycentric;
in vec3 v_position;

layout(location = 0) out vec4 color;

float edgeFactor(){
    vec3 d = fwidth(v_barycentric);
    vec3 a3 = smoothstep(vec3(0.0), d * u_width, v_barycentric);
    return min(min(a3.x, a3.y), a3.z);
}

void main() {
	color = mix(u_colour, vec4(0.0), edgeFactor());
}

