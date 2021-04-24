#version 410

in vec4 a_position;	// vertex position as a homogeneous 3D point in model 
in vec3 a_colour;	// RGB

uniform mat4 u_modelMatrix;			// MODEL -> WORLD
uniform mat4 u_viewMatrix;			// WORLD -> VIEW
uniform mat4 u_projectionMatrix;	// VIEW -> NDC

uniform float u_distort;

out vec3 v_colour;	// RGB

void main() {
	v_colour = a_colour;
	vec3 p = a_position.xyz;
	p.x = sign(p.x) * (abs(p.x) - u_distort * (p.y + 1.) / 2); 
    gl_Position = u_projectionMatrix * u_viewMatrix * u_modelMatrix * vec4(p, 1);
}

