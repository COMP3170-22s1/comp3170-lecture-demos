#version 410

// note we are now using vec3 for possitions in 3D
in vec2 a_position;	

void main() {
    gl_Position = vec4(a_position, 0, 1);
}

