package dev.clutcher.modulith.archunit.rules.app.domain.services.library;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;

import java.util.Set;

public final class ArchRulePredicates {

    private ArchRulePredicates() {
    }

    public static DescribedPredicate<JavaClass> areNotAnnotatedWithAnyOf(String[] annotationNames) {
        return new DescribedPredicate<>("are not annotated with any of the generated class annotations") {
            @Override
            public boolean test(JavaClass javaClass) {
                Set<? extends JavaAnnotation<? extends JavaClass>> annotations = javaClass.getAnnotations();
                for (String annotationName : annotationNames) {
                    for (JavaAnnotation<? extends JavaClass> annotation : annotations) {
                        if (annotation.getRawType().getName().equals(annotationName)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        };
    }
}
