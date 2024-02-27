## Common mistakes (jv-hash-map)

#### Don't begin class or method implementation with empty line.  
Remove all redundant empty lines, be careful :)
#### Any magic numbers should be constants
Your code should be easy to read. Move all hardcoded values 
to [constant fields](https://mate-academy.github.io/style-guides/java/java.html#s5.2.4-constant-names) and give them informative names.
For example: constants can be `defaultCapacity`, `loadFactor`...
#### Don't use getters and setters in class Node
We have access to private fields of inner class, so using getters or setters is redundant.
#### Remember to parameterize the Node class
Class `Node` should take key and value, so we should parameterized it to prevent problems with data types.
#### Private methods and classes should be after public ones in your class.
#### Using public access modifiers everywhere
If the method has only a utility purpose and is used only inside the same class, it should not be 
`public`. Keep your code as close as possible to follow the encapsulation principle.
#### Remember about access modifiers.
All fields and methods should have access modifiers. Remember about the encapsulation principle.
#### Diamond operator
Be attentive with the diamond operator in java. 
What is the difference between: `new Node<>(...)` and `new Node<K,V>(...)`and `new Node(...)`. 
Read more about it [here](https://www.baeldung.com/java-diamond-operator)
#### Create informative variable and method names.
Writing proper variable names can be a highly valuable investment to the quality of your code. 
Not only you and your teammates understand your code better, but it can also improve code sustainability in the future later on. 
When you go back to the same code base and re-read it over again, you should understand what is going on.
Do not use abstract words like `string` or `array` as variable name. Do not use one-letter names. The name of the method should make it clear what it does.

- Bad example:
    ```java
    String[] arr = new String[]{"Alex", "Bob", "Alice"};
    for (String s : arr) {
        System.out.println(s);
    }
    ```
- Refactored code:
    ```java
    String[] usernames = new String[]{"Alex", "Bob", "Alice"};
    for (String username : usernames) {
        System.out.println(username);
    }
    ```
    
