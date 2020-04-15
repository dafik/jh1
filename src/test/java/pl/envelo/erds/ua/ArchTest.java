package pl.envelo.erds.ua;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("pl.envelo.erds.ua");

        noClasses()
            .that()
            .resideInAnyPackage("pl.envelo.erds.ua.service..")
            .or()
            .resideInAnyPackage("pl.envelo.erds.ua.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..pl.envelo.erds.ua.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
