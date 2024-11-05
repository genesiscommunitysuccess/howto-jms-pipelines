plugins {
    kotlin("plugin.power-assert")
}

dependencies {
    compileOnly(genesis("script-dependencies"))
    genesisGeneratedCode(withTestDependency = true)
    testImplementation(genesis("dbtest"))
    testImplementation(genesis("testsupport"))
    testImplementation(genesis("pal-eventhandler"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")

    // this adds the dependency to the genesis pipeline module
    implementation(genesis("pal-datapipeline"))
    // this adds the dependency to the genesis JMS module which holds the JMS Source and Sink that we will be using in our pipeline
    implementation("global.genesis:jms-genesis:${findProperty("integrationsVersion")}")
    // this adds the dependency for IBM MQ which we need to generate the factory instance that we'll provide to JMS
    implementation("com.ibm.mq:com.ibm.mq.jakarta.client:9.4.5.1")
}

description = "jms-pipelines-app"

sourceSets {
    main {
        resources {
            srcDirs("src/main/resources", "src/main/genesis")
        }
    }
}
