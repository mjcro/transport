package io.github.mjcro.transport.http;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "io.github.mjcro.transport")
public class HttpLocalityArchUnitTest {
    @ArchTest
    public static final ArchRule httpShouldNotBeReferencedOutside = ArchRuleDefinition.noClasses()
            .that()
            .resideOutsideOfPackages(
                    "io.github.mjcro.transport.http..",
                    "io.github.mjcro.transport.example.."
            )
            .should()
            .dependOnClassesThat()
            .resideInAPackage("io.github.mjcro.transport.http");

    @ArchTest
    public static final ArchRule nativeJavaHttpShouldNotBeReferencedOutside = ArchRuleDefinition.noClasses()
            .that()
            .resideOutsideOfPackages(
                    "io.github.mjcro.transport.http.java..",
                    "io.github.mjcro.transport.example.."
            )
            .should()
            .dependOnClassesThat()
            .resideInAPackage("io.github.mjcro.transport.http.java");
}
