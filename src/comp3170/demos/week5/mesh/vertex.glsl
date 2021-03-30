#version 410

in vec4 a_position;	// vertex position as a homogeneous 2D point in model 

uniform mat4 u_modelMatrix;	// MODEL -> WORLD (= NDC)
uniform float u_sphericity;

void main() {
	vec3 p = normalize(a_position.xyz);
	p = mix(a_position.xyz, p, u_sphericity);
	 
	// pad to a homogeneous 3D point
    gl_Position = u_modelMatrix * vec4(p, 1);
}

