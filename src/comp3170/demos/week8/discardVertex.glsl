#version 410

// note we are now using vec3 for possitions in 3D
in vec3 a_position;	
in vec3 a_barycentric;	

uniform mat4 u_worldMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

out vec3 v_barycentric;

void main() {
	/* turn 3D point into 4D homogeneous form by appending 1 */

	vec4 position = vec4(a_position, 1);
	
	/* transform */
	
	position = u_projectionMatrix * u_viewMatrix * u_worldMatrix * position;
	
    gl_Position = position;
    
    // pass the barycentric coordinate to the fragment shader
    v_barycentric = a_barycentric;
}

