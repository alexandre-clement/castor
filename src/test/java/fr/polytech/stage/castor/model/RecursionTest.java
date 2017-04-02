package fr.polytech.stage.castor.model;

import fr.polytech.stage.castor.interpreter.variables.Vector;
import javafx.util.Pair;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.*;

/**
 * @author Alexandre Clement
 *         Created the 01/04/2017.
 */
public class RecursionTest
{
    @Test
    public void multiplyBy3() throws Exception
    {
        RecursiveFunction composition = new Composition(new Composition(new Successor(), new Successor()), new Successor());
        RecursiveFunction recursion = new Recursion(new Zero(), new Left(composition));
        Vector vector = new Vector(10);
        Vector expected = new Vector(30);
        assertEquals(expected, recursion.apply(vector));
    }

    @Test
    public void sum() throws Exception
    {
        RecursiveFunction sum = new Recursion(new Zero(), new Recursion(new Successor(), new Left(new Right(new Successor()))));
        Vector vector = new Vector(10);
        Vector expected = new Vector(10 * 11 / 2);
        assertEquals(expected, sum.apply(vector));

        vector = new Vector(20);
        expected = new Vector(20 * 21 / 2);
        assertEquals(expected, sum.apply(vector));

        vector = new Vector(30);
        expected = new Vector(30 * 31 / 2);
        assertEquals(expected, sum.apply(vector));
    }

    @Test
    public void square() throws Exception
    {
        RecursiveFunction square = new Recursion(new Zero(), new Recursion(new Successor(), new Left(new Right(new Composition(new Successor(), new Successor())))));
        Vector vector = new Vector(10);
        Vector expected = new Vector(10 * 10);
        assertEquals(expected, square.apply(vector));

        vector = new Vector(20);
        expected = new Vector(20 * 20);
        assertEquals(expected, square.apply(vector));

        vector = new Vector(30);
        expected = new Vector(30 * 30);
        assertEquals(expected, square.apply(vector));
    }

    @Test
    public void unknown() throws Exception
    {
        RecursiveFunction unknown = new Composition(new Recursion(new Successor(), new Right(new Right(new Identity()))), new Left(new Identity()), new Right(new Successor()));
        Vector vector = new Vector(0, 0);
        Vector expected = new Vector(2);
        assertEquals(expected, unknown.apply(vector));

        vector = new Vector(0, 1);
        expected = new Vector(0);
        assertEquals(expected, unknown.apply(vector));

        vector = new Vector(1, 0);
        expected = new Vector(3);
        assertEquals(expected, unknown.apply(vector));

        vector = new Vector(1, 1);
        expected = new Vector(0);
        assertEquals(expected, unknown.apply(vector));

        vector = new Vector(1, 2);
        expected = new Vector(1);
        assertEquals(expected, unknown.apply(vector));

        vector = new Vector(2, 1);
        expected = new Vector(0);
        assertEquals(expected, unknown.apply(vector));

        vector = new Vector(2, 2);
        expected = new Vector(1);
        assertEquals(expected, unknown.apply(vector));

        vector = new Vector(10, 8);
        expected = new Vector(7);
        assertEquals(expected, unknown.apply(vector));

        vector = new Vector(8, 10);
        expected = new Vector(9);
        assertEquals(expected, unknown.apply(vector));

        vector = new Vector(20, 10);
        expected = new Vector(9);
        assertEquals(expected, unknown.apply(vector));

        vector = new Vector(10, 20);
        expected = new Vector(19);
        assertEquals(expected, unknown.apply(vector));

        vector = new Vector(2, 20);
        expected = new Vector(19);
        assertEquals(expected, unknown.apply(vector));

        vector = new Vector(2, 200);
        expected = new Vector(199);
        assertEquals(expected, unknown.apply(vector));

        vector = new Vector(200, 2);
        expected = new Vector(1);
        assertEquals(expected, unknown.apply(vector));
    }

    @Test
    public void findXValue() throws Exception
    {
        RecursiveFunction addPlusOne = new Recursion(new Successor(), new Left(new Right(new Successor())));
        RecursiveFunction sum = new Recursion(new Zero(), addPlusOne);
        RecursiveFunction sumPlusOne = new Composition(sum, new Successor());
        RecursiveFunction predecessor = new Recursion(new Zero(), new Right(new Identity()));
        RecursiveFunction sous = new Recursion(new Identity(), new Right(new Left(predecessor)));
        RecursiveFunction sousRightSuccessorByLeftSumPlusOne = new Composition(sous, new Left(sumPlusOne), new Right(new Successor()));
        RecursiveFunction add = new Recursion(new Right(new Zero()), new Left(new Left(new Successor())));
        RecursiveFunction composition = new Composition(add, sousRightSuccessorByLeftSumPlusOne, new Left(new Identity()));
        RecursiveFunction findXValue = new Recursion(new Zero(), composition);

        Function<Integer, Integer> expectedFunction = x -> {
            int n = (int) Math.sqrt(2*x);
            if (n * (n + 1) / 2 > x)
                n -= 1;
            int p = n * (n+1) / 2;
            return x - p;
        };

        Vector vector = new Vector(0);
        Vector expected = new Vector(0);
        assertEquals(expected, findXValue.apply(vector));

        for (int i = 0; i < 30; i++)
        {
            vector = new Vector(i);
            expected = new Vector(expectedFunction.apply(i));
            assertEquals(expected, findXValue.apply(vector));
        }
    }

    @Test
    public void composition() throws Exception
    {
        RecursiveFunction addPlusOne = new Recursion(new Successor(), new Left(new Right(new Successor())));
        RecursiveFunction sum = new Recursion(new Zero(), addPlusOne);
        RecursiveFunction sumPlusOne = new Composition(sum, new Successor());
        RecursiveFunction predecessor = new Recursion(new Zero(), new Right(new Identity()));
        RecursiveFunction sous = new Recursion(new Identity(), new Right(new Left(predecessor)));
        RecursiveFunction sousRightSuccessorByLeftSumPlusOne = new Composition(sous, new Left(sumPlusOne), new Right(new Successor()));
        RecursiveFunction add = new Recursion(new Right(new Zero()), new Left(new Left(new Successor())));
        RecursiveFunction composition = new Composition(add, sousRightSuccessorByLeftSumPlusOne, new Left(new Identity()));
        Vector vector = new Vector(0, 0);
        Vector expected = new Vector(0);
        assertEquals(expected, composition.apply(vector));

        vector = new Vector(2, 4);
        expected = new Vector(5);
        assertEquals(expected, add.apply(vector));

        vector = new Vector(10, 20);
        expected = new Vector(21);
        assertEquals(expected, add.apply(vector));

        vector = new Vector(20, 4);
        expected = new Vector(5);
        assertEquals(expected, add.apply(vector));

        vector = new Vector(100, 20);
        expected = new Vector(21);
        assertEquals(expected, add.apply(vector));

        vector = new Vector(20, 100);
        expected = new Vector(101);
        assertEquals(expected, add.apply(vector));
    }

    @Test
    public void add2() throws Exception
    {
        RecursiveFunction add = new Recursion(new Right(new Zero()), new Left(new Left(new Successor())));
        Vector vector = new Vector(0, 0);
        Vector expected = new Vector(0);
        assertEquals(expected, add.apply(vector));

        vector = new Vector(2, 4);
        expected = new Vector(5);
        assertEquals(expected, add.apply(vector));

        vector = new Vector(10, 20);
        expected = new Vector(21);
        assertEquals(expected, add.apply(vector));
    }

    @Test
    public void sousRightSuccessorByLeftSumPlusOne() throws Exception
    {
        RecursiveFunction addPlusOne = new Recursion(new Successor(), new Left(new Right(new Successor())));
        RecursiveFunction sum = new Recursion(new Zero(), addPlusOne);
        RecursiveFunction sumPlusOne = new Composition(sum, new Successor());
        RecursiveFunction predecessor = new Recursion(new Zero(), new Right(new Identity()));
        RecursiveFunction sous = new Recursion(new Identity(), new Right(new Left(predecessor)));
        RecursiveFunction sousRightSuccessorByLeftSumPlusOne = new Composition(sous, new Left(sumPlusOne), new Right(new Successor()));
        Function<Integer, Integer> sumPlusOneFunction = x -> (x+1) * (x+2) / 2;
        Function<Pair<Integer, Integer>, Integer> expectedFunction = x -> Math.max(0, x.getKey() + 1 - sumPlusOneFunction.apply(x.getValue()));
        Vector vector;
        Vector expected;

        for (int i = 0; i < 30; i++)
        {
            for (int j = 0; j < 30; j++)
            {
                vector = new Vector(i, j);
                expected = new Vector(expectedFunction.apply(new Pair<>(i, j)));
                assertEquals(expected, sousRightSuccessorByLeftSumPlusOne.apply(vector));
            }
        }
    }

    @Test
    public void sous() throws Exception
    {
        RecursiveFunction predecessor = new Recursion(new Zero(), new Right(new Identity()));
        RecursiveFunction sous = new Recursion(new Identity(), new Right(new Left(predecessor)));
        Vector vector = new Vector(0, 1);
        Vector expected = new Vector(1);
        assertEquals(expected, sous.apply(vector));

        vector = new Vector(2, 3);
        expected = new Vector(1);
        assertEquals(expected, sous.apply(vector));

        vector = new Vector(10, 20);
        expected = new Vector(10);
        assertEquals(expected, sous.apply(vector));

        vector = new Vector(100, 20);
        expected = new Vector(0);
        assertEquals(expected, sous.apply(vector));

        vector = new Vector(20, 100);
        expected = new Vector(80);
        assertEquals(expected, sous.apply(vector));
    }

    @Test
    public void predecessor() throws Exception
    {
        RecursiveFunction predecessor = new Recursion(new Zero(), new Right(new Identity()));
        Vector vector = new Vector(0);
        Vector expected = new Vector(0);
        assertEquals(expected, predecessor.apply(vector));

        vector = new Vector(2);
        expected = new Vector(1);
        assertEquals(expected, predecessor.apply(vector));

        vector = new Vector(10);
        expected = new Vector(9);
        assertEquals(expected, predecessor.apply(vector));
    }

    @Test
    public void add() throws Exception
    {
        RecursiveFunction add = new Recursion(new Identity(), new Left(new Right(new Successor())));
        Vector vector = new Vector(0, 0);
        Vector expected = new Vector(0);
        assertEquals(expected, add.apply(vector));

        vector = new Vector(2, 4);
        expected = new Vector(6);
        assertEquals(expected, add.apply(vector));

        vector = new Vector(10, 20);
        expected = new Vector(30);
        assertEquals(expected, add.apply(vector));
    }

    @Test
    public void sumPlusOne() throws Exception
    {
        RecursiveFunction addPlusOne = new Recursion(new Successor(), new Left(new Right(new Successor())));
        RecursiveFunction sum = new Recursion(new Zero(), addPlusOne);
        RecursiveFunction sumPlusOne = new Composition(sum, new Successor());
        Vector vector = new Vector(0);
        Vector expected = new Vector(1);
        Function<Integer, Integer> expectedFunction = x -> (x+1) * (x+2) / 2;
        assertEquals(expected, sumPlusOne.apply(vector));

        vector = new Vector(1);
        expected = new Vector(expectedFunction.apply(1));
        assertEquals(expected, sumPlusOne.apply(vector));

        vector = new Vector(3);
        expected = new Vector(expectedFunction.apply(3));
        assertEquals(expected, sumPlusOne.apply(vector));

        vector = new Vector(7);
        expected = new Vector(expectedFunction.apply(7));
        assertEquals(expected, sumPlusOne.apply(vector));

        vector = new Vector(10);
        expected = new Vector(expectedFunction.apply(10));
        assertEquals(expected, sumPlusOne.apply(vector));
    }

    @Test
    public void addPlusOne() throws Exception
    {
        RecursiveFunction addPlusOne = new Recursion(new Successor(), new Left(new Right(new Successor())));
        Vector vector = new Vector(0, 0);
        Vector expected = new Vector(1);
        assertEquals(expected, addPlusOne.apply(vector));

        vector = new Vector(2, 4);
        expected = new Vector(7);
        assertEquals(expected, addPlusOne.apply(vector));

        vector = new Vector(10, 20);
        expected = new Vector(31);
        assertEquals(expected, addPlusOne.apply(vector));
    }

}