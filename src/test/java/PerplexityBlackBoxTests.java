import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class PerplexityBlackBoxTests {
    private ShapeClassifier classifier = new ShapeClassifier();

    // Boundary Value Analysis
    @Test
    public void testBoundaryValuesForTriangle() {
        assertEquals("Yes", classifier.evaluateGuess("Equilateral,Large,Yes,1,1,1"));
        assertEquals("Yes", classifier.evaluateGuess("Equilateral,Large,Yes,4095,4095,4095"));
        assertTrue(classifier.evaluateGuess("Equilateral,Small,Yes,2,2,2").startsWith("Yes: Wrong Size"));
    }

    // Robustness Testing
    @Test
    public void testRobustnessForInvalidInputs() {
        assertEquals("No", classifier.evaluateGuess("Equilateral,Large,Yes,0,0,0"));
        assertEquals("No", classifier.evaluateGuess("Equilateral,Large,Yes,4096,4096,4096"));
    }

    // Equivalence Partitioning
    @Test
    public void testEquivalenceClassesForTriangles() {
        assertEquals("Yes", classifier.evaluateGuess("Equilateral,Large,Yes,100,100,100"));
        assertEquals("Yes", classifier.evaluateGuess("Isosceles,Large,Yes,100,100,50"));
        assertEquals("Yes", classifier.evaluateGuess("Scalene,Large,Yes,100,90,80"));
    }

    @Test
    public void testEquivalenceClassesForQuadrilaterals() {
        assertEquals("Yes", classifier.evaluateGuess("Square,Large,Yes,100,100,100,100"));
        assertEquals("Yes", classifier.evaluateGuess("Rectangle,Large,Yes,100,50,100,50"));
    }

    // Combinatorial Testing
    @Test
    public void testCombinationsOfShapeSizeAndEvenOdd() {
        String[][] combinations = {
            {"Equilateral", "Small", "Yes"},
            {"Isosceles", "Large", "No"},
            {"Rectangle", "Small", "Yes"},
            {"Circle", "Large", "No"}
        };
        
        for (String[] combo : combinations) {
            String input = String.format("%s,%s,%s,100,100,100", combo[0], combo[1], combo[2]);
            assertTrue(classifier.evaluateGuess(input).startsWith("Yes") || 
                       classifier.evaluateGuess(input).startsWith("No"));
        }
    }

    // Decision Table Testing
    @Test
    public void testDecisionTableForShapeAndSize() {
        assertTrue(classifier.evaluateGuess("Circle,Small,Yes,5,5").startsWith("Yes"));
        assertTrue(classifier.evaluateGuess("Circle,Large,Yes,50,50").startsWith("Yes"));
        assertTrue(classifier.evaluateGuess("Square,Small,Yes,5,5,5,5").startsWith("Yes"));
        assertTrue(classifier.evaluateGuess("Square,Large,Yes,50,50,50,50").startsWith("Yes"));
    }

    // Error Guessing
    @Test
    public void testErrorGuessing() {
        assertEquals("No", classifier.evaluateGuess("Triangle,Large,Yes,1,1,1"));
        assertEquals("No", classifier.evaluateGuess("Circle,Large,Yes,50"));
        assertEquals("No", classifier.evaluateGuess("Square,Large,Yes,50,50,50"));
    }

    // Testing for incorrect guesses
    @Test
    public void testIncorrectGuesses() {
        assertTrue(classifier.evaluateGuess("Rectangle,Small,Yes,100,50,100,50").contains("Wrong Size"));
        assertTrue(classifier.evaluateGuess("Circle,Large,No,50,50").contains("Wrong Even/Odd"));
        assertTrue(classifier.evaluateGuess("Isosceles,Large,Yes,100,100,100").contains("Suggestion=Equilateral"));
    }
}
