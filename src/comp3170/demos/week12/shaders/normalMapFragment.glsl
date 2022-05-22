#version 410

uniform sampler2D u_diffuseTexture;
uniform sampler2D u_normalMapTexture;

uniform mat3 u_normalMatrix;			// MODEL -> WORLD (normals)

in vec4 v_position;		// WORLD
in vec2 v_texcoord;		// UV 
in vec3 v_normal;		// MODEL
in vec3 v_tangent;		// MODEL
in vec3 v_bitangent;	// MODEL

layout(location = 0) out vec4 o_colour;

void main() {

	vec3 t = normalize(v_tangent);
	vec3 b = normalize(v_bitangent);
	vec3 n = normalize(v_normal);
	mat3 tbnMatrix = mat3(t, b, n);

	n = texture(u_normalMapTexture, v_texcoord).xyz;
	n = 2. * n - 1.;
	n = tbnMatrix * n;

	mat3 m = u_normalMatrix;

    o_colour = texture(u_diffuseTexture, v_texcoord);
    o_colour = vec4(n.x,n.x,n.x,1);
}

