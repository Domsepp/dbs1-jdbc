package java_introduction;

import java_introduction.Coordinate;

import java.sql.Array;
import java.util.ArrayList;

public class JavaBasicsMain {

    public static void main(String[] args) {
        //---------------------------------------------------------------------
        //----------------------Simple Hello World program---------------------
        //---------------------------------------------------------------------
        System.out.println("Hello World"); //<--- every statement in java has to end with a semicolon
        //basic data types (primitives):

        //---------------------------------------------------------------------
        //----------------------Variables and primitives-----------------------
        //---------------------------------------------------------------------
        //variable Declaration and initialization: <type> <name> = <value>
        boolean myBool = true;
        int myInt = 42;
        float myFloat = 3.1f;
        double myDouble = 3.14;
        //The above standard data types are "primitives". Primitives are not objects - you cannot call methods on them and cannot use them instead of objects

        //---------------------------------------------------------------------
        //----------------------Objects and Classes----------------------------
        //---------------------------------------------------------------------
        //Objects are Initialized by calling class constructors:
        Coordinate point1 = new Coordinate(2,2);
        Coordinate point2 = new Coordinate(3,3);
        //methods on objects are called like this:
        double distance = point1.euclidianDistance(point2);
        System.out.println(distance);
        //In case you need to use primitives in place of objects (for example in collections), you need to use Wrapper-Objects:
        Integer myIntObject = new Integer(42); //new keyword initializes Objects (calls their constructor)
        //---------------------------------------------------------------------
        //-----------------------------------Strings---------------------------
        //---------------------------------------------------------------------
        String myString = "Hello";
        String anotherString = "World";
        String concatenated = myString + anotherString;
        //NEVER DO THIS:
        boolean stringsAreEqual = myString == anotherString;
        System.out.println(stringsAreEqual);
        //Instead do this:
        stringsAreEqual = myString.equals(anotherString);
        System.out.println(stringsAreEqual);
        // == checks for reference equality, which may or may not be true for equal strings!
        // .equals checks for semantic equality (classes can override this method). Usually this is what we want.
        //a special case are primitives, for which we check equality by using ==
        //---------------------------------------------------------------------
        //-----------------------------------Collections-----------------------
        //---------------------------------------------------------------------
        //basic collections (stacks, lists, sets, maps, priority queues) are available in the java.util package
        ArrayList<String> myList = new ArrayList<String>();
        myList.add("first elem");
        myList.add("second elem");
        //arrays are rarely used, usually ArrayLists are used instead
        String[] myArray = new String[3]; //array of size 3
        myArray[0] = "first elem";
        System.out.println(myArray[0]);
        System.out.println(myArray[1]);
        //---------------------------------------------------------------------
        //-----------------------------------Control Statements----------------
        //---------------------------------------------------------------------
        //if-else
        if( myString.equals("New String")){
            System.out.println("equal");
        } else {
            System.out.println("unequal");
        }
        //for i-loop
        for (int i = 0; i < myList.size(); i++) {
            System.out.println(myList.get(i));
        }
        //for each loop
        for (String elem : myList) {
            System.out.println(elem);
        }
        //while loop
        while (myList.size() < 10){
            myList.add("new String");
        }
        //---------------------------------------------------------------------
        //-----------------------------------Advanced: Streams-----------------
        //---------------------------------------------------------------------
        //Since java 8 streams and anonymous functions are supported:
        int lengthSummed = myList.stream()
                .map(string -> string.length())
                .reduce((a,b) -> a+b)
                .get();
        System.out.println(lengthSummed);


    }
}
