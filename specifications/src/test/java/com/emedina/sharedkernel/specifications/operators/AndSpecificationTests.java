package com.emedina.sharedkernel.specifications.operators;

import com.emedina.sharedkernel.specifications.CompositeSpecification;
import com.emedina.sharedkernel.specifications.FluentSpecification;
import com.emedina.sharedkernel.specifications.Specification;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Test all the methods of the AndSpecification class
public class AndSpecificationTests {

    // Test the constructor
    @Test
    public void testConstructor() {
        // Test the constructor
        // Create a new AndSpecification object
        AndSpecification<Object> andSpecification = new AndSpecification<Object>(null, null);

        // Check that the object is not null
        assertNotNull(andSpecification);
        // Check that the object is an instance of AndSpecification
        assertInstanceOf(AndSpecification.class, andSpecification);
        // Check that the object is an instance of CompositeSpecification
        assertInstanceOf(CompositeSpecification.class, andSpecification);
        // Check that the object is an instance of Specification
        assertInstanceOf(Specification.class, andSpecification);
        // Check that the object is an instance of FluentSpecification
        assertInstanceOf(FluentSpecification.class, andSpecification);
    }

    // Test the isSatisfiedBy method
    @Test
    public void testIsSatisfiedBy() {
        // Test the isSatisfiedBy method
        // Create a new AndSpecification object
        AndSpecification<Object> andSpecification = new AndSpecification<>(candidate -> true, candidate -> true);

        // Check that the object is not null
        assertNotNull(andSpecification);

        // Check that the isSatisfiedBy method returns true
        assert(andSpecification.isSatisfiedBy(null));
    }

}