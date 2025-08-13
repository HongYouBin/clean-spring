    package tobyspring.learningtest.archunit;

    import com.tngtech.archunit.core.domain.JavaClasses;
    import com.tngtech.archunit.junit.AnalyzeClasses;
    import com.tngtech.archunit.junit.ArchTest;

    import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
    import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

    @AnalyzeClasses(packages = "tobyspring.learningtest.archunit")
    public class ArchUnitTest {

        /**
         * Application 의존하는 클래스는 application, adapter에만 존재해야 한다.
         */
        @ArchTest
        void application(JavaClasses javaClass) {
            classes().that().resideInAnyPackage("..application..")
                    .should().onlyHaveDependentClassesThat().resideInAnyPackage("..application..", "..adapter..")
                    .check(javaClass);
        }

        /**
         * Application 클래스는 adapter에 의존하면 안된다.
         */
        @ArchTest
        void adapter(JavaClasses javaClasses) {
            noClasses().that().resideInAnyPackage("..application..")
                    .should().dependOnClassesThat().resideInAPackage("..adapter..")
                    .check(javaClasses);
        }

        /***
         * Domain 클래스는 domain, java에만 의존해야 한다.
         */
        @ArchTest
        void domain(JavaClasses javaClasses) {
            classes().that().resideInAPackage("..domain..")
                    .should().onlyDependOnClassesThat()
                    .resideInAnyPackage("..domain..", "..java..");
        }
    }
