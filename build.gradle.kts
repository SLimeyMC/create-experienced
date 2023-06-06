import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
	id ("fabric-loom") version "1.0.+"
    kotlin("jvm") version "1.8.0" apply false
	id ("io.github.juuxel.loom-quiltflower") version "1.+" // Quiltflower, a better decompiler
	id ("maven-publish")
}

val sourceCompatibility = JavaVersion.VERSION_17
val targetCompatibility = JavaVersion.VERSION_17

val archivesBaseName = rootProject.property("archives_base_name").toString()
val group = rootProject.property("maven_group").toString()

// Formats the mod version to include the Minecraft version and build number (if present)
// example: 1.0.0+1.18.2-100
val buildNumber = System.getenv("GITHUB_RUN_NUMBER")
version = "${property("mod_version")}+${property("archives_base_name")}${property("minecraft_version")}" + if(buildNumber != null)  "-${buildNumber}" else ""

repositories {
	maven { url = uri("https://dvs1.progwml6.com/files/maven/") }
	maven { url = uri("https://maven.parchmentmc.org") } // Parchment mappings
	maven { url = uri("https://maven.quiltmc.org/repository/release") } // Quilt Mappings
	maven { url = uri("https://api.modrinth.com/maven") }
	maven { url = uri("https://maven.terraformersmc.com/releases/") } // Mod Menu
	maven { url = uri("https://mvn.devos.one/snapshots/") } // Create, Porting Lib, Forge Tags, Milk Lib, Registrate
	maven { url = uri("https://cursemaven.com") } // Forge Config API Port
	maven { url = uri("https://maven.jamieswhiteshirt.com/libs-release") } // Reach Entity Attributes
	maven { url = uri("https://jitpack.io/") } // Mixin Extras, Fabric ASM
	maven { url = uri("https://maven.tterrag.com/") } // Flywheel
}

dependencies {
	// Setup
	minecraft(libs.minecraft)
	mappings(loom.layered {
		mappings("org.quiltmc:quilt-mappings:${property("minecraft_version")}+build.${property("qm_version")}:intermediary-v2")
		parchment("org.parchmentmc.data:parchment-${property("minecraft_version")}:${property("parchment_version")}@zip")
		officialMojangMappings { nameSyntheticMembers = false }
	})
	modImplementation(libs.fabric.loader)

	// dependencies
	modImplementation(libs.fabric.api)
	modImplementation(libs.fabric.kotlin)

	// Create - dependencies are added transitively
	modImplementation(libs.create)

	// Development QOL
	modLocalRuntime(libs.emi)
	modLocalRuntime(libs.modmenu)
	// incase i want to add custom recipes
//    modCompileOnly("dev.emi:emi:${emi_version}")
}

//processResources {
//	// require dependencies to be the version compiled against or newer
//	Map<String, String> properties = new HashMap<>()
//	properties.put("version", version)
//	properties.put("fabric_loader_version", fabric_loader_version)
//	properties.put("fabric_api_version", fabric_api_version)
//	properties.put("create_version", create_version)
//	properties.put("minecraft_version", minecraft_version)
//
//	properties.forEach((k, v) -> inputs.property(k, v))
//
//	filesMatching("fabric.mod.json") {
//		expand properties
//	}
//}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}

java {
	withSourcesJar()
}
