#version 410

in vec3 v_barycentric;

layout(location = 0) out vec4 color;

float edgeFactor(){
    vec3 d = fwidth(v_barycentric);
    vec3 a3 = smoothstep(vec3(0.0), d, v_barycentric);
    return min(min(a3.x, a3.y), a3.z);
}

void main() {
	color = mix(vec4(1.0), vec4(0.0), edgeFactor());
}

