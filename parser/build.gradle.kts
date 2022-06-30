
//java {
//    toolchain {
//        languageVersion.set(JavaLanguageVersion.of(19))
//    }
//}

dependencies {
    implementation(project(":common"))
    implementation(project(":lexer"))
    implementation(project(":syntax"))
}

//tasks {
//    test {
//        jvmArgs("--enable-preview")
//    }
//
//    withType<JavaCompile> {
//        options.compilerArgs.add("--enable-preview")
//    }
//}
