plugins {
    id("application")
    id ("org.openjfx.javafxplugin") version "0.1.0"
}

application {
    mainClass.set("fr.blazanome.routeplanner.RoutePlannerApplication")
}
javafx {
    version = "17.0.8"
    modules("javafx.controls")
}

group = "fr.blazanome"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
