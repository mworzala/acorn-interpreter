
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(19))
    }
}

tasks {
    test {
        jvmArgs("--enable-preview")
    }

    withType<JavaCompile> {
        options.compilerArgs.add("--enable-preview")
    }
}