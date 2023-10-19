plugins {
    id("application")
    id ("org.openjfx.javafxplugin") version "0.1.0"
}

application {
    mainClass.set("fr.blazanome.routeplanner.view.RoutePlannerApplication")
}
javafx {
    version = "17.0.8"
    modules("javafx.controls", "javafx.fxml")
}

group = "fr.blazanome"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.openjfx:javafx-fxml:17")
}

tasks.test {
    testLogging {
        events("PASSED", "SKIPPED", "FAILED", "STANDARD_OUT", "STANDARD_ERROR")
    }
    useJUnitPlatform()
}
