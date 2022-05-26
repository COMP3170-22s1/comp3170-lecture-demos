#version 410

uniform sampler2D u_diffuseTexture;
uniform sampler2D u_normalMapTexture;

uniform vec3 u_lightDirection;
uniform vec3 u_lightIntensity;		// RGB - intensity
uniform vec3 u_ambientIntensity;	// RGB - intensity

uniform mat3 u_normalMatrix;		// MODEL -> WORLD (normals)

in vec4 v_position;		// WORLD
in vec2 v_texcoord;		// UV 
in vec3 v_normal;		// MODEL
in vec3 v_tangent;		// MODEL
in vec3 v_bitangent;	// MODEL

const vec3 GAMMA = vec3(2.2f);

layout(location = 0) out vec4 o_colour;

void main() {
	vec3 t = normalize(v_tangent);
	vec3 b = normalize(v_bitangent);
	vec3 n = normalize(v_normal);
	mat3 tbnMatrix = mat3(t, b, n);

	n = texture(u_normalMapTexture, v_texcoord).xyz;
	n = 2. * n - 1.;
	n = u_normalMatrix * tbnMatrix * n;

	vec3 material = texture(u_diffuseTexture, v_texcoord).rgb; 
	material = pow(material, GAMMA);
	
	vec3 s = normalize(u_lightDirection);
	vec3 ambient = u_ambientIntensity * material;
	vec3 diffuse = u_lightIntensity * material * max(0, dot(n, s));
	
	vec3 intensity = ambient + diffuse;
	vec3 brightness = pow(intensity, 1. / GAMMA);

    o_colour = vec4(brightness, 1);
}

