import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShapeClassifierAITest {

    private ShapeClassifier classifier;

    @BeforeEach
    void setUp() {
        classifier = new ShapeClassifier();
    }

    @Nested
    @DisplayName("Line Tests")
    class LineTests {
        @Test
        @DisplayName("Valid Line")
        void testValidLine() {
            assertEquals("Yes", classifier.evaluateGuess("Line,Small,No,5"));
        }

        @Test
        @DisplayName("Line with incorrect size guess")
        void testLineIncorrectSize() {
            assertEquals("No: Wrong Size", classifier.evaluateGuess("Line,Large,No,5"));
        }

        @Test
        @DisplayName("Line with incorrect even/odd guess")
        void testLineIncorrectEvenOdd() {
            assertEquals("No: Wrong Even/Odd", classifier.evaluateGuess("Line,Small,Yes,5"));
        }
    }

    @Nested
    @DisplayName("Circle Tests")
    class CircleTests {
        @Test
        @DisplayName("Valid Small Circle")
        void testValidSmallCircle() {
            assertEquals("Yes", classifier.evaluateGuess("Circle,Small,Yes,1,1"));
        }

        @Test
        @DisplayName("Valid Large Circle")
        void testValidLargeCircle() {
            assertEquals("Yes", classifier.evaluateGuess("Circle,Large,No,17,17"));
        }

        @Test
        @DisplayName("Circle with incorrect shape guess")
        void testCircleIncorrectShape() {
            assertTrue(classifier.evaluateGuess("Ellipse,Small,Yes,1,1").startsWith("No: Suggestion=Circle"));
        }
    }

    @Nested
    @DisplayName("Ellipse Tests")
    class EllipseTests {
        @Test
        @DisplayName("Valid Ellipse")
        void testValidEllipse() {
            assertEquals("Yes", classifier.evaluateGuess("Ellipse,Large,No,10,20"));
        }

        @Test
        @DisplayName("Ellipse with incorrect shape guess")
        void testEllipseIncorrectShape() {
            assertTrue(classifier.evaluateGuess("Circle,Large,No,10,20").startsWith("No: Suggestion=Ellipse"));
        }
    }

    @Nested
    @DisplayName("Triangle Tests")
    class TriangleTests {
        @Test
        @DisplayName("Valid Equilateral Triangle")
        void testValidEquilateralTriangle() {
            assertEquals("Yes", classifier.evaluateGuess("Equilateral,Large,No,40,40,40"));
        }

        @Test
        @DisplayName("Valid Isosceles Triangle")
        void testValidIsoscelesTriangle() {
            assertEquals("Yes", classifier.evaluateGuess("Isosceles,Large,Yes,40,40,60"));
        }

        @Test
        @DisplayName("Valid Scalene Triangle")
        void testValidScaleneTriangle() {
            assertEquals("Yes", classifier.evaluateGuess("Scalene,Large,No,30,40,50"));
        }

        @Test
        @DisplayName("Invalid Triangle")
        void testInvalidTriangle() {
            assertTrue(classifier.evaluateGuess("Equilateral,Small,Yes,1,1,10").startsWith("No: Suggestion=Not A Triangle"));
        }
    }

    @Nested
    @DisplayName("Rectangle Tests")
    class RectangleTests {
        @Test
        @DisplayName("Valid Rectangle")
        void testValidRectangle() {
            assertEquals("Yes", classifier.evaluateGuess("Rectangle,Large,Yes,30,40,30,40"));
        }

        @Test
        @DisplayName("Rectangle with incorrect shape guess")
        void testRectangleIncorrectShape() {
            assertTrue(classifier.evaluateGuess("Square,Large,Yes,30,40,30,40").startsWith("No: Suggestion=Rectangle"));
        }
    }

    @Nested
    @DisplayName("Square Tests")
    class SquareTests {
        @Test
        @DisplayName("Valid Square")
        void testValidSquare() {
            assertEquals("Yes", classifier.evaluateGuess("Square,Large,Yes,25,25,25,25"));
        }

        @Test
        @DisplayName("Square with incorrect shape guess")
        void testSquareIncorrectShape() {
            assertTrue(classifier.evaluateGuess("Rectangle,Large,Yes,25,25,25,25").startsWith("No: Suggestion=Square"));
        }
    }

    @Nested
    @DisplayName("Boundary Value Tests")
    class BoundaryValueTests {
        @Test
        @DisplayName("Small shape boundary")
        void testSmallShapeBoundary() {
            assertEquals("Yes", classifier.evaluateGuess("Circle,Small,No,1,1"));
            assertEquals("No: Wrong Size", classifier.evaluateGuess("Circle,Small,Yes,2,2"));
        }

        @Test
        @DisplayName("Large shape boundary")
        void testLargeShapeBoundary() {
            assertEquals("No: Wrong Size", classifier.evaluateGuess("Circle,Large,No,15,15"));
            assertEquals("Yes", classifier.evaluateGuess("Circle,Large,Yes,16,16"));
        }

        @Test
        @DisplayName("Maximum parameter value")
        void testMaximumParameterValue() {
            assertEquals("Yes", classifier.evaluateGuess("Line,Large,No,4095"));
            assertEquals("No: Wrong Size", classifier.evaluateGuess("Line,Large,No,4096"));
        }

        @Test
        @DisplayName("Minimum parameter value")
        void testMinimumParameterValue() {
            assertEquals("Yes", classifier.evaluateGuess("Line,Small,Yes,0"));
            assertEquals("No: Wrong Size", classifier.evaluateGuess("Line,Small,Yes,-1"));
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {
        @Test
        @DisplayName("Invalid input format")
        void testInvalidInputFormat() {
            assertEquals("No", classifier.evaluateGuess("InvalidInput"));
        }

        @Test
        @DisplayName("Missing parameters")
        void testMissingParameters() {
            assertEquals("No", classifier.evaluateGuess("Circle,Small,Yes"));
        }

        @Test
        @DisplayName("Non-numeric parameters")
        void testNonNumericParameters() {
            assertEquals("No", classifier.evaluateGuess("Circle,Small,Yes,a,b"));
        }
    }
}