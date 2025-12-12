package io.github.mjcro.transport.options;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import io.github.mjcro.interfaces.experimental.integration.Option;

@AnalyzeClasses(packages = "io.github.mjcro.transport")
class OptionsArchUnitTest {
    @ArchTest
    public static final ArchRule optionsShouldHaveSuffixOption = ArchRuleDefinition.classes()
            .that().implement(Option.class)
            .and().areNotEnums()
            .should()
            .haveSimpleNameEndingWith("Option");
}